package io.ibj.JLib.format;

import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joe on 12/30/2014.
 */
@ToString
public class MSection implements MPart{

    public MSection(){
    }

    @Override
    public MSection clone(){
        MSection ret = new MSection();
        ret.subParts = new LinkedList<>();
        for(MPart p : this.subParts){
            ret.subParts.add(p.clone());
        }
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


    List<MPart> subParts = new LinkedList<MPart>();
    String clickAction;
    String clickValue;
    String[] hover;
    String insert;
    

    @Override
    public List<ChatPart> flatten(ChatPart prime) {
        List<ChatPart> ret = new LinkedList<ChatPart>();
        ChatPart localizeChatPart = prime.clone();

        if(clickAction != null)
            localizeChatPart.clickAction = clickAction;
        if(clickValue != null)
            localizeChatPart.clickValue = clickValue;
        if(hover != null)
            localizeChatPart.hover = hover;
        if(insert != null)
            localizeChatPart.insert = insert;

        for(MPart part : subParts){
            List<ChatPart> flatten = part.flatten(localizeChatPart);
            ret.addAll(flatten);
            if(ret.size()>0) {
                ChatPart lastPart = ret.get(ret.size() - 1);
                localizeChatPart.color = lastPart.color;
                localizeChatPart.formatting = lastPart.formatting;
            }
        }
        return ret;
    }

    @Override
    public void replace(String s, String o) {
        for(MPart p : subParts){
            p.replace(s,o);
        }
        if(clickValue!= null)
            clickValue = clickValue.replace(s, o);
        if(hover != null)
            for(int i = 0; i<hover.length; i++){
                hover[i] = hover[i].replace(s, o);
            }
        if(insert != null)
            insert = insert.replace(s, o);
    }

    @Override
    public String getClickAction() {
        return clickAction;
    }

    public void setClickParams(String action, String value){
        this.clickValue = value;
        this.clickAction = action;
    }

    @Override
    public String getClickValue() {
        return clickValue;
    }

    @Override
    public String[] getHover() {
        return hover;
    }

    public void setHover(String[] strings){
        hover= strings;
    }

    @Override
    public String getInsert() {
        return insert;
    }

    public MSection appendSection(){
        MSection section = new MSection();
        subParts.add(section);
        return section;
    }

    public TextPart appendText(String string){
        TextPart part = new TextPart(string);
        subParts.add(part);
        return part;
    }
}