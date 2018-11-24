package com.rex.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TypeUtil {
    public static int getInt(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null");
        }

        return ((bytes[0] & 0xFF) | ((bytes[1] << 8) & 0xFF00));
    }

    public static byte[] intToByte(int i){
        byte[] res = new byte[8];
        res[0] = (byte)(i & 0xFF);
        res[1] = (byte)(i >>8 & 0xFF);
        System.out.println("res = " + res[0]+" "+res[1 ]);
        return res;
    }

    public static Object byteToObject(byte[] bytes){
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static byte[] ObjectToByte(Object object) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
}
