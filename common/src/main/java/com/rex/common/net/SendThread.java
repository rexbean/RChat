package com.rex.common.net;

import android.util.Log;

import com.rex.common.utils.PacketUtil;

import java.io.BufferedOutputStream;
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
    private ISocketStatus mListener;

    public SendThread(Socket socket, ISocketStatus listener){
        mSocket = socket;
        mListener = listener;
        init();
    }


    private void init(){
        try {
            mOut = mSocket.getOutputStream();
        } catch (IOException e) {
            onFailed("Get outputStream failed");
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
                        byte[] packet = PacketUtil.constructPacket(msg.getBytes());

                        BufferedOutputStream bos = new BufferedOutputStream(mOut);
                        bos.write(packet);
                        bos.flush();
                        Log.d("SendThread","after send out message: " + msg);
                        // todo: serialize the message
                        // todo: send message out
                    }
//                    mSocket.shutdownOutput();//关闭输出流
                    msgQueue.wait();
                } catch( IOException |InterruptedException e){
                    onFailed("Send Msg Failed");
                    e.printStackTrace();
                }
            }

        }

    }

    private void onFailed(String errorMsg){
        if(mListener != null){
            mListener.onFailed(errorMsg);
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
        try {
            while(msgQueue.size() != 0){
                Thread.sleep(50);
            }
            mOut.close();
        } catch (InterruptedException | IOException e) {
            onFailed("Send Close Failed");
            e.printStackTrace();
        }
    }
}
