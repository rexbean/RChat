package com.rex.common.net;

import com.rex.common.utils.PacketUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class InputThread extends Thread{
    private static InputStream mIn;
    private static Socket mSocket;
    private ISocketStatus mListener;

    private byte[] packet = new byte[0];
    private int dataLen;

    public InputThread(Socket socket, ISocketStatus listener){
        mSocket = socket;
        mListener = listener;
        init();
    }

    private void init(){
        try {
            mIn = mSocket.getInputStream();
        } catch (IOException e) {
            onFailed("Get inputStream Failed");
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while(mSocket != null && mSocket.isConnected()){
            try {
                BufferedInputStream bis = new BufferedInputStream(mIn);
                if(bis.available() <= 2){
                    continue;
                }
                dataLen = PacketUtil.mergeAndCut(packet, dataLen, bis);

                // todo: dispatch the messages out using EventBus
            } catch (IOException  e) {
                onFailed("Receive Msg Failed");
                e.printStackTrace();
            }
        }

    }

    private void onFailed(String errorMsg){
        if(mListener != null){
            mListener.onFailed(errorMsg);
        }
    }

    public void close(){
        // todo: wait for all received
        try {
            mIn.close();
        } catch (IOException e) {
            onFailed("Receive Close Failed");
            e.printStackTrace();
        }
    }
}
