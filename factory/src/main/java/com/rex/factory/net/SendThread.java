package com.rex.factory.net;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class SendThread extends Thread{
    private static OutputStream mOut;
    private static Socket mSocket;
    private static final Queue<String> msgQueue = new LinkedList<>();

    public SendThread(Socket socket){
        mSocket = socket;
        init();
    }


    private void init(){
        try {
            mOut = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(mSocket != null && mSocket.isConnected()){
            synchronized(msgQueue){
                try {
                    while(!msgQueue.isEmpty()){
                        String msg = msgQueue.poll();
                        Log.d("SendThread","before send out message: " + msg);
                        PrintWriter printWriter=new PrintWriter(mOut);//将输出流包装成打印流
                        printWriter.print(msg);
                        Log.d("SendThread","after send out message: " + msg);

                        printWriter.flush();

                        // todo: serialize the message
                        // todo: send message out
                    }
//                    mSocket.shutdownOutput();//关闭输出流
                    msgQueue.wait();
                } catch( InterruptedException e){
                    e.printStackTrace();
                }
            }

        }

    }

    public static void appendMsg(String data){
        synchronized(msgQueue){
            msgQueue.offer(data+"\n");
            msgQueue.notify();
        }
    }

    // todo: wait for finishing sending message
    public void close(){
        while(msgQueue.size() != 0){

        }
        try {
            mOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
