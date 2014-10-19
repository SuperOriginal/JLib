package io.ibj.JLib.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.ibj.JLib.utils.exceptions.WorldNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * io.ibj.utils
 * Created by Joe
 * Project: Utils
 */
public class LocationUtils {
    public static DBObject toBSON(Location l){
        if(l == null){
            return null;
        }
        BasicDBObject obj = new BasicDBObject("world",l.getWorld().getName());
        obj.append("x",l.getX());
        obj.append("y",l.getY());
        obj.append("z",l.getZ());
        obj.append("yaw",l.getYaw());
        obj.append("pitch",l.getPitch());
        return obj;
    }

    public static Location fromBSON(DBObject obj) throws WorldNotFoundException {
        if(obj == null){
            return null;
        }
        World w = Bukkit.getWorld((String) obj.get("world"));
        if(w == null){
            throw new WorldNotFoundException((String) obj.get("world"));
        }
        return new Location(w, ((Double) obj.get("x")), ((Double) obj.get("y")), ((Double) obj.get("z")), ((Double) obj.get("yaw")).floatValue(), ((Double) obj.get("pitch")).floatValue());
    }
}
