package com.rex.common.utils;

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
}
