package io.ibj.JLib.format;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by joe on 12/6/14.
 */
public class Format {

    public Format(String... msg){
        this.messages = msg;
    }

    public Format(String tag, String... msg){
        this.tag = tag;
        this.messages = msg;
    }

    String tag = null;
    String[] messages;
    TagStyle tagStyle = TagStyle.FIRST_LINE;

    public Format setTag(String tag){
        this.tag = tag;
        return this;
    }

    public Format setTagStyle(TagStyle style){
        this.tagStyle = style;
        return this;
    }

    public Format replace(String key, Object replacement){
        for(int i = 0; i< messages.length; i++){
            messages[i] = messages[i].replace(key,replacement.toString());
        }
        return this;
    }

    public Format sendTo(CommandSender... senders){
        for(CommandSender sender : senders){
            switch(tagStyle){
                case EVERY_LINE:
                    for(String message : messages){
                        sender.sendMessage(tag+message);
                    }
                    break;
                case FIRST_LINE:
                    boolean sent = false;
                    for(String message: messages){
                        sender.sendMessage(!sent ? tag + message : message);
                        sent  = true;
                    }
                    break;
                case HIDDEN:
                    for(String message : messages){
                        sender.sendMessage(message);
                    }
                    break;

            }

        }
        return this;
    }

    public Format sendTo(UUID... players){
        for(UUID id : players){
            Player player = Bukkit.getPlayer(id);
            if(player != null){
                switch(tagStyle) {
                    case EVERY_LINE:
                        for (String message : messages) {
                            player.sendMessage(tag + message);
                        }
                        break;
                    case FIRST_LINE:
                        boolean sent = false;
                        for (String message : messages) {
                            player.sendMessage(!sent ? tag + message : message);
                            sent = true;
                        }
                        break;
                    case HIDDEN:
                        for (String message : messages) {
                            player.sendMessage(message);
                        }
                        break;
                }

            }
        }
        return this;
    }

    public String[] getMessages(){
        return messages;
    }

}
