package com.rex.common.message;

import java.io.BufferedReader;

public class MessageManager {
    private static MessageManager instance;

    private MessageManager(){

    }


    public static MessageManager getInstance() {
        return instance;
    }

    public void add(byte[] packet){

    }
}
