package io.ibj.JLib.format;

import java.util.List;

/**
 * Created by joe on 12/30/2014.
 */
public interface MPart extends ChatActionable, Cloneable{

    List<ChatPart> flatten(ChatPart prime);
    void replace(String s, String o);
    MPart clone();
}