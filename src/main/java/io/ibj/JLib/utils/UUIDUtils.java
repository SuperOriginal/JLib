package io.ibj.JLib.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/**
 * Created by Joe on 10/20/2014.
 */
public class UUIDUtils {
    public static byte[] toBytes(UUID id){
        if(id == null){
            return null;
        }
        byte[] bytes = new byte[16];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(id.getMostSignificantBits());
        buffer.putLong(id.getLeastSignificantBits());
        return bytes;
    }

    public static UUID fromBytes(byte[] bytes){
        if(bytes == null){
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(),buffer.getLong());
    }
}
