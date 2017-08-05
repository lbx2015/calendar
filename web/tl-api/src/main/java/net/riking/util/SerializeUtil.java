package net.riking.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/***
 * 对象序列化
 * @author zm.you
 * @version crateTime：2017年8月5日 下午5:25:09
 * @used TODO redis服务器本身支持二进制安全的类型，所以可以把一个Java对象序列化后存储到redis中
 */
public class SerializeUtil {
	/** 
     * 序列化 
     *  
     * @param object 
     * @return 
     */  
    public static byte[] serialize(Object object) {  
        ObjectOutputStream oos = null;  
        ByteArrayOutputStream baos = null;  
        try {  
            // 序列化  
            baos = new ByteArrayOutputStream();  
            oos = new ObjectOutputStream(baos);  
            oos.writeObject(object);  
            byte[] bytes = baos.toByteArray();  
            return bytes;  
        } catch (Exception e) {  
  
        }  
        return null;  
    }  
  
    /** 
     * 反序列化 
     *  
     * @param bytes 
     * @return 
     */  
    public static Object unserialize(byte[] bytes) {  
        ByteArrayInputStream bais = null;  
        try {  
            // 反序列化  
            bais = new ByteArrayInputStream(bytes);  
            ObjectInputStream ois = new ObjectInputStream(bais);  
            return ois.readObject();  
        } catch (Exception e) {  
  
        }  
        return null;  
    }
}
