package com.rex.common.net;

public class packet {
    public static final int BUFFER_MAX_LEN = 1024;
    public static final byte[] HEAD = new byte[]{(byte)0xFF,(byte)0xF3};
    public static final int HEAD_LEN = 2;
    public static final int DATA_LEN = 2;
    public static final byte[] END = new byte[]{(byte)0xFF, (byte)0xF4};
    public static final int END_LEN = 2;
}
