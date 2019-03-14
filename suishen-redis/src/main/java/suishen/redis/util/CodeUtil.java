package suishen.redis.util;

import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;

public class CodeUtil {

    public static final <T extends Serializable> byte[] objectEncode(T obj) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(obj);
            bytes = bout.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error serializing object" + obj + " => " + e);
        }
        return bytes;
    }

    public static <T> T objectDecode(byte[] serialDate) {

        if (serialDate == null || serialDate.length == 0) {
            return null;
            //throw new JedisDataException("object serialize data cannot be null");
        }
        try {
            return (T) new ObjectInputStream(new ByteArrayInputStream(serialDate)).readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error read serializing object from byte array => " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error read serializing object from byte array => " + e);
        }

    }

    public static byte[] keyEncode(final String str) {
        try {
            if (str == null || str.length() == 0) {
                throw new JedisDataException("  key value set to redis cannot be null");
            }
            return str.getBytes(Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new JedisException(e);
        }
    }

    public static byte[][] keyEncode(final String... strs) {
        try {
            if (strs == null) {
                throw new JedisDataException("value sent to redis cannot be null");
            }

            byte[][] bytes = new byte[strs.length][];

            for (int i = 0; i < strs.length; i++) {
                bytes[i] = strs[i].getBytes(Protocol.CHARSET);
            }

            return bytes;
        } catch (UnsupportedEncodingException e) {
            throw new JedisException(e);
        }
    }

}
