package io.ibj.JLib;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by Joe on 8/8/2014.
 */
public class PlayerLookup{

    public UUID getFromName(String name) {

        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        if(player != null){
            return player.getUniqueId();
        }

        try {
            ProfileData profC = new ProfileData(name);
            String uuid = null;
            for (int i = 1; i <= 100; i++) {
                PlayerProfile[] result = post(new URL("https://api.mojang.com/profiles/page/" + i), Proxy.NO_PROXY, JLib.getI().getGson().toJson(profC).getBytes());
                if (result.length == 0) {
                    break;
                }
                uuid = result[0].getId();
            }
            return UUID.fromString(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFromUUID(UUID uuid) {
        String name = null;
        OfflinePlayer cachedPlayer = Bukkit.getOfflinePlayer(uuid);
        if(cachedPlayer != null){
            return cachedPlayer.getName();
        }
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            URLConnection connection = url.openConnection();
            Scanner jsonScanner = new Scanner(connection.getInputStream(), "UTF-8");
            String json = jsonScanner.next();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            name = (String) ((JSONObject) obj).get("name");
            jsonScanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return name;
    }

    private static PlayerProfile[] post(URL url, Proxy proxy, byte[] bytes) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(bytes);
        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        reader.close();
        return JLib.getI().getGson().fromJson(response.toString(), SearchResult.class).getProfiles();
    }

    private static class PlayerProfile {
        private String id;

        public String getId() {
            return id;
        }
    }

    private static class SearchResult {
        private PlayerProfile[] profiles;

        public PlayerProfile[] getProfiles() {
            return profiles;
        }
    }

    private static class ProfileData {

        @SuppressWarnings("unused")
        private String name;
        @SuppressWarnings("unused")
        private String agent = "minecraft";

        public ProfileData(String name) {
            this.name = name;
        }
    }
}