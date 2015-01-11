package io.ibj.JLib.format;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.ibj.JLib.JLib;
import io.ibj.JLib.chat.FancyMessage;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by joe on 12/6/14.
 */
@ToString
public class Format implements Cloneable {

    public Format(String... msg){
        this(null, msg);
    }

    public Format(Format tag, String... msg) {
        if(tag != null) {
            this.tag = tag.getInternalMPart().size() == 0 ? new MSection() : tag.getInternalMPart().get(0);
        }
        else
        {
            this.tag = new TextPart("");
        }
        for(String s : msg){
            //Gotta parse it fast!
            char[] chars = s.toCharArray();
            Integer pointer = 0;
            StringBuilder currentString = new StringBuilder();
            boolean ignoreNext = false;
            Deque<MSection> partQueue = new LinkedBlockingDeque<>();
            partQueue.add(new MSection());
            while(pointer < chars.length){
                char currentChar = chars[pointer];
                if(!ignoreNext){
                    if(currentChar == '\\'){
                        ignoreNext = true;
                        pointer++;
                        continue;
                    }
                    else if(currentChar == '{'){
                        //Starting of inter config section
                        pointer = internalsParse(chars, pointer, partQueue.peekLast()) + 1;
                        continue;
                    }
                    else if(currentChar == '['){
                        if(currentString.length() > 0){
                            partQueue.peekLast().appendText(currentString.toString());
                            currentString = new StringBuilder();
                        }
                        partQueue.addLast(partQueue.getLast().appendSection());
                        pointer++;
                        continue;
                    }
                    else if(currentChar == ']'){
                        MSection section = partQueue.pollLast(); //Remove last item from section
                        if(currentString.length() > 0){
                            section.appendText(currentString.toString());
                            currentString = new StringBuilder();
                        }
                        pointer++;
                        continue;
                    }
                }
                else
                {
                    ignoreNext = false;
                }
                currentString.append(currentChar);
                pointer++;
            }
            if(partQueue.isEmpty()){
                throw new RuntimeException("Illegal formatting!");
            }
            partQueue.peekLast().appendText(currentString.toString());
            messages.add(partQueue.pollFirst());
        }
    }

    MPart tag = null;
    List<MPart> messages = new ArrayList<>();
    TagStyle tagStyle = TagStyle.FIRST_LINE;

    public Format setTag(Format tag){
        this.tag = tag.getInternalMPart().get(0);
        return this;
    }

    public Format setTagStyle(TagStyle style){
        this.tagStyle = style;
        return this;
    }

    public Format replace(String key, Object replacement){
        String strReplacement = replacement.toString();
        for(MPart part : messages){
            part.replace(key,strReplacement);
        }
        return this;
    }

    private int internalsParse(char[] chars, Integer pointer, MSection toApply){
        StringBuilder jsonRaw = new StringBuilder();
        int jsonLevels = 0;
        boolean ignoreBackslash = false;
        boolean ignoreQuotes = false;
        char lastQuoteChar = ' ';
        while(pointer < chars.length){
            if(!ignoreBackslash) {
                if (chars[pointer] == '{') {
                    jsonLevels++;
                } else if (chars[pointer] == '}') {
                    jsonLevels--;
                    if (jsonLevels == 0) {
                        jsonRaw.append('}');
                        break;
                    }
                } else if (chars[pointer] == '"' || chars[pointer] == '\'') {
                    if (ignoreQuotes) {
                        if (lastQuoteChar == chars[pointer]) {
                            ignoreQuotes = false;
                        }
                    } else {
                        lastQuoteChar = chars[pointer];
                        ignoreQuotes = true;
                    }
                }
                else if(chars[pointer] == '\\'){
                    ignoreBackslash = true;
                }
            }
            else{
                ignoreBackslash = false;
            }
            jsonRaw.append(chars[pointer]);
            pointer++;
        }
        String json = jsonRaw.toString();
        JsonObject object = JLib.getI().getGson().fromJson(json, JsonObject.class);
        if(object.has("open_url")){
            toApply.setClickParams("open_url",object.get("open_url").getAsString());
        }
        else if(object.has("open_file")){
            toApply.setClickParams("open_file",object.get("open_file").getAsString());
        }
        else if(object.has("run_command")){
            toApply.setClickParams("run_command",object.get("run_command").getAsString());
        }
        else if (object.has("suggest_command")) {
            toApply.setClickParams("suggest_command",object.get("suggest_command").getAsString());
        }

        if(object.has("hover")){
            JsonArray array = object.getAsJsonArray("hover");
            String[] strings = new String[array.size()];
            for(int i = 0; i<array.size(); i++){
                strings[i] = array.get(i).getAsString();
            }
            toApply.setHover(strings);
        }
        return pointer;
    }

    public Format sendTo(CommandSender... senders){
        for(FancyMessage message : constructFancyMessage()){
            for(CommandSender sender : senders){
                message.send(sender);
            }
        }
        return this;
    }
    
    public Format sendTo(UUID... players){
        Set<CommandSender> senders = new HashSet<>();
        for(UUID id : players){
            Player player = Bukkit.getPlayer(id);
            if(player != null){
                senders.add(player);
            }
        }
        sendTo(senders.toArray(new CommandSender[senders.size()]));
        return this;
    }
    
    public List<FancyMessage> constructFancyMessage(){
        List<FancyMessage> ret = new ArrayList<>(messages.size());
        boolean sentFirst = false;
        for(MPart part : messages) {
            List<ChatPart> total = new LinkedList<>();
            if(tagStyle == TagStyle.EVERY_LINE || (tagStyle == TagStyle.FIRST_LINE && !sentFirst)) {
                total.addAll(tag.flatten(new ChatPart()));
            }
            if (total.size() > 0) {
                total.addAll(part.flatten(total.get(total.size() - 1)));
            } else {
                total.addAll(part.flatten(new ChatPart()));
            }
            FancyMessage message = new FancyMessage("");
            for (ChatPart chatPart : total) {
                chatPart.appendToFancyMessage(message);
            }
            ret.add(message);
            sentFirst = true;
        }
        return ret;
    }
    
    public List<MPart> getInternalMPart(){
        return ImmutableList.copyOf(messages);
    }

    @Override
    public Format clone() {
        Format ret = new Format();
        ret.messages = new LinkedList<>();
        for(MPart part : messages){
            ret.messages.add(part.clone());
        }
        ret.tag = tag == null ? null : tag.clone();
        ret.tagStyle = tagStyle;
        return ret;
    }
}
