package io.ibj.JLib.format;

import io.ibj.JLib.utils.Colors;
import org.bukkit.ChatColor;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joe on 12/30/2014.
 */
public class TextPart implements MPart {

    public TextPart(String text){
        this.text = text;
    }

    String text;
    String clickAction;
    String clickValue;
    String[] hover;
    String insert;


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
    
    @Override
    public List<ChatPart> flatten(ChatPart prime) {
        //OK. So we need to go through the string and strip the color codes, then split them up into different chat parts.
        //We already have the initial color state, given the initial MPart priming us.
        char[] chars = text.toCharArray();
        int pointer = 0;
        StringBuilder currentBuilder = null;
        ChatColor currentColor = prime.color;
        ChatColor[] currentFormats = prime.formatting;
        String clickAction;
        if(this.clickAction != null){
            clickAction = this.clickAction;
        }
        else
        {
            clickAction = prime.clickAction;
        }
        String clickValue;
        if(this.clickValue != null){
            clickValue = this.clickValue;
        }
        else{
            clickValue = prime.clickValue;
        }
        String[] hover;

        if(this.hover != null){
            hover = this.hover;
        }
        else
        {
            hover = prime.hover;
        }
        String insert;
        if(this.insert != null){
            insert = this.insert;
        }
        else
        {
            insert = prime.insert;
        }

        if(hover != null) {
            for (int i = 0; i < hover.length; i++) {
                hover[i] = Colors.colorify(hover[i]);
            }
        }
        ChatPart currentPart = null;
        List<ChatPart> ret = new LinkedList<ChatPart>();
        while(pointer < chars.length){
            if(currentPart == null){
                currentPart = new ChatPart();
                currentPart.color = currentColor;
                currentPart.formatting = currentFormats;
                currentPart.clickAction = clickAction;
                currentPart.clickValue = clickValue;
                currentPart.insert = insert;
                currentPart.hover = hover;
                currentBuilder = new StringBuilder();
            }

            if(chars[pointer] == ChatColor.COLOR_CHAR){
                if(pointer+1 < chars.length) { //Got another char to read a colorcode from
                    ChatColor chatColor = ChatColor.getByChar(chars[pointer+1]);
                    if(chatColor != null){
                        boolean split = false;
                        //Clip off current chatpart, append to list, start a new
                        if(chatColor == ChatColor.RESET){
                            currentFormats = new ChatColor[0];
                            currentColor = null;
                            split = true;
                        }
                        else if(chatColor.isColor()){
                            if(currentColor != chatColor) {
                                currentColor = chatColor;
                                currentFormats = new ChatColor[0];
                                split = true;
                            }
                            else
                            {
                                if(currentFormats != null && currentFormats.length != 0){
                                    split = true;
                                    currentFormats = new ChatColor[0];
                                }
                            }
                        }
                        else
                        {
                            if(!arrayContains(currentFormats, chatColor)){
                                split = true;
                                ChatColor[] temp = new ChatColor[currentFormats.length+1];
                                System.arraycopy(currentFormats,0,temp,0, currentFormats.length);
                                temp[temp.length-1] = chatColor;
                                currentFormats = temp;
                            }
                        }
                        if(split){
                            if(currentBuilder.length()==0){
                                currentPart.color = currentColor;
                                currentPart.formatting = currentFormats;
                            }
                            else {
                                //Ok, gotta split this thing
                                currentPart.text = currentBuilder.toString();
                                ret.add(currentPart);
                                currentPart = null;
                            }
                        }

                        pointer +=2; //Skip the next on pointer too, covered by identifier char. Continue.
                        continue;
                    }
                }
            }
            currentBuilder.append(chars[pointer]);
            pointer++;
        }
        if(currentPart != null) {
            currentPart.text = currentBuilder.toString();
            ret.add(currentPart);
        }
        return ret;
    }

    @Override
    public void replace(String s, String o) {
        text = text.replace(s, o);
        if(clickValue != null)
            clickValue = clickValue.replace(s, o);
        if(hover != null)
            for(int i = 0; i<hover.length; i++){
                hover[i] = hover[i].replace(s, o);
            }
        if(insert != null)
            insert = insert.replace(s, o);
    }

    private boolean arrayContains(Object[] array, Object test){
        for(Object o : array){
            if(o == test){
                return true;
            }
        }
        return false;
    }

    @Override
    public TextPart clone(){
        TextPart ret = new TextPart(text);
        ret.clickAction = clickAction;
        ret.clickValue = clickValue;
        if(hover != null) {
            ret.hover = new String[hover.length];        
            System.arraycopy(hover, 0, ret.hover, 0, hover.length);

        }
        else
        {
            ret.hover = null;
        }
        ret.insert = insert;
        return ret;
    }
}
