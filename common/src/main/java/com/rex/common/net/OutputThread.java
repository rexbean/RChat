package com.rex.common.net;

import android.util.Log;

import com.rex.common.utils.PacketUtil;
import com.rex.common.utils.TypeUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class OutputThread extends Thread{
    private static Socket mSocket;
    private static final Queue<String> msgQueue = new LinkedList<>();
    private ISocketStatus mListener;
    private static BufferedOutputStream bos;

    public OutputThread(Socket socket, ISocketStatus listener){
        mSocket = socket;
        mListener = listener;
        init();
    }


    private void init(){
        try {
             bos = new BufferedOutputStream(mSocket.getOutputStream());
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
                        Log.d("OutputThread","before send out message: " + msg);
                        // todo: serialize data using protocol buffer
                        byte[] data = TypeUtil.ObjectToByte(msg);
                        byte[] packet = PacketUtil.constructPacket(data);

                        bos.write(packet);
                        bos.flush();
                        Log.d("OutputThread","after send out message: " + msg);
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
            bos.close();
        } catch (InterruptedException | IOException e) {
            onFailed("Send Close Failed");
            e.printStackTrace();
        }
    }
}
