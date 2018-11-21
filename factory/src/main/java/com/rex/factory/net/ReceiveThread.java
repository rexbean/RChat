package com.rex.factory.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;


public class ReceiveThread<Data> extends Thread{
    private static InputStream mIn;
    private static Socket mSocket;
    private Queue<Data> msgQueue;

    private static ReceiveThread instance=null;
    public ReceiveThread(Socket socket){
        mSocket = socket;
        init();
    }

    private void init(){
        try {
            mIn = mSocket.getInputStream();
            msgQueue = new LinkedList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while(mSocket != null && mSocket.isConnected()){
            try {
                InputStreamReader inputStreamReader=new InputStreamReader(mIn);//提高效率，将自己字节流转为字符流
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);//加入缓冲区
                String temp=null;
                String info="";
                while((temp=bufferedReader.readLine())!=null){
                    info =temp;
                    System.out.println("Receive msg："+info);
                }
                // todo: dispatch the messagees out using EventBus
            } catch (IOException  e) {
                e.printStackTrace();
            }
        }

    }

    public void close(){
        // todo: wait for all received
        try {
            mIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
