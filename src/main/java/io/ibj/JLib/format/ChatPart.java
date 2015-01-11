package io.ibj.JLib.format;

import io.ibj.JLib.chat.FancyMessage;
import lombok.ToString;
import org.bukkit.ChatColor;

/**
 * Created by joe on 12/30/2014.
 */
@ToString
public class ChatPart implements ChatActionable, Cloneable
{
    String text;
    String clickAction;
    String clickValue;
    String[] hover;
    String insert;
    ChatColor color;
    ChatColor[] formatting;

    public String getText(){
        return text;
    }

    @Override
    public String getClickAction() {
        return clickAction;
    }

    @Override
    public String getClickValue() {
        return clickValue;
    }

    @Override
    public String[] getHover() {
        return hover;
    }

    @Override
    public String getInsert() {
        return insert;
    }

    public ChatColor getColor(){
        return color;
    }

    public ChatColor[] getFormatting(){
        return formatting;
    }

    public void appendToFancyMessage(FancyMessage fancyMessage){
        fancyMessage.then(text == null ? "" : text);
        if(color != null){
            fancyMessage.color(color);
        }
        if(clickAction != null){
            if(clickAction.equals("open_url")){
                fancyMessage.link(clickValue);
            }
            else if(clickAction.equals("open_file")){
                fancyMessage.file(clickValue);
            }
            else if(clickAction.equals("run_command")){
                fancyMessage.command(clickValue);
            }
            else if(clickAction.equals("suggest_command")){
                fancyMessage.suggest(clickValue);
            }
        }
        if(hover != null){
            fancyMessage.tooltip(getHover());
        }
        if(getFormatting() != null && getFormatting().length > 0){
            fancyMessage.style(getFormatting());
        }
    }

    @Override
    public ChatPart clone() {
        ChatPart part = new ChatPart();
        part.text = text;
        part.clickAction = clickAction;
        part.clickValue = clickValue;
        if(hover != null) {
            part.hover = new String[hover.length];
            System.arraycopy(hover, 0, part.hover, 0, hover.length);
        }
        else
        {
            part.hover = null;
        }
        part.insert = insert;
        part.color = color;
        if(formatting != null) {
            part.formatting = new ChatColor[formatting.length];
            System.arraycopy(formatting, 0, part.formatting, 0, formatting.length);
        }
        else
        {
            part.formatting = null;
        }
        return part;
    }
}