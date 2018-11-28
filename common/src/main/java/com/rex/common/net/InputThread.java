package com.rex.common.net;

import com.rex.common.utils.PacketUtil;

import java.io.BufferedInputStream;
import java.io.IOException;

public class InputThread extends Thread{
    private final BufferedInputStream mBis;
    private ISocketStatus mListener;
    private volatile boolean isStop = false;

    private byte[] packet = new byte[0];
    private int dataLen;

    public InputThread(BufferedInputStream bis, ISocketStatus listener){
        mBis = bis;
        mListener = listener;
    }

    private boolean isStop(){
        return isStop;
    }

    @Override
    public void run() {
        try {
            while(!isStop()){
                if(mBis.available() <= 2){
                    continue;
                }
                dataLen = PacketUtil.mergeAndCut(packet, dataLen, mBis);
                if(dataLen == -1){
                    if(mListener != null){
                        mListener.onFailed("Connection failed");
                    }
                }
            }
        } catch(IOException e) {
            onFailed("Receive Msg Failed");
            e.printStackTrace();
        } finally {
            if(mListener != null) {
                mListener.onTerminated();
            }
        }
    }

    private void onFailed(String errorMsg){
        if(mListener != null){
            mListener.onFailed(errorMsg);
        }
    }

    public void close(){
        try {
            isStop = true;
            interrupt();
            if(mBis != null) {
                synchronized (mBis) {
                    mBis.close();
                }
            }
        } catch (IOException e) {
            onFailed("Receive Close Failed");
            e.printStackTrace();
        }
    }
}
