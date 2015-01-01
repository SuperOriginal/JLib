package io.ibj.JLib.format;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by joe on 12/30/2014.
 */
public class MSection implements MPart{

    public MSection(){
    }

    @Override
    public MSection clone(){
        MSection ret = new MSection();
        ret.subParts = new LinkedList<>();
        for(MPart p : this.subParts){
            subParts.add(p.clone());
        }
        ret.clickAction = clickAction;
        ret.clickValue = clickValue;
        ret.hover = new String[hover.length];
        System.arraycopy(hover, 0, ret.hover, 0, hover.length);
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
        if(clickAction != null)
            prime.clickAction = clickAction;
        if(clickValue != null)
            prime.clickValue = clickValue;
        if(hover != null)
            prime.hover = hover;
        if(insert != null)
            prime.insert = insert;
        for(MPart part : subParts){
            List<ChatPart> flatten = part.flatten(prime);
            ret.addAll(flatten);
            prime = flatten.get(flatten.size()-1);
        }
        return ret;
    }

    @Override
    public void replace(String s, String o) {
        for(MPart p : subParts){
            p.replace(s,o);
        }
        if(clickValue!= null)
            clickValue = clickValue.replace(s,o.toString());
        if(hover != null)
            for(int i = 0; i<hover.length; i++){
                hover[i] = hover[i].replace(s,o.toString());
            }
        if(insert != null)
            insert = insert.replace(s,o.toString());
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