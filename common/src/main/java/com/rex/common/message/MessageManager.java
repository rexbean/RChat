package com.rex.common.message;

import com.rex.common.utils.TypeUtil;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private static MessageManager instance;
    public List<String> messages;

    private MessageManager() {
        messages = new ArrayList<>();
    }


    public static MessageManager getInstance() {
        if(instance == null){
            instance = new MessageManager();
        }
        return instance;
    }

    public void add(byte[] packet) {
        String d = (String) TypeUtil.byteToObject(packet);
        System.out.println("receive Message:" + d);
        messages.add(d);
    }
}
