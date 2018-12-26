package com.rex.common.net;

import com.rex.common.utils.PacketUtil;
import com.rex.common.utils.TypeUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import timber.log.Timber;

public class OutputThread extends Thread{
    private static final ConcurrentLinkedQueue<String> msgQueue = new ConcurrentLinkedQueue<>();
    private ISocketStatus mListener;
    private final BufferedOutputStream mBos;
    private volatile boolean isStop = false;

    public OutputThread(BufferedOutputStream bos, ISocketStatus listener){
        mListener = listener;
        mBos = bos;

    }

    private boolean isStop(){
        return isStop;
    }

    @Override
    public void run() {
        try{
            while(!isStop()){
                String msg = msgQueue.poll();
                synchronized (msgQueue){
                    if(msg == null) {
                        msgQueue.wait();
                    }
                    msg = msgQueue.poll();
                    Timber.d("before send out message: %s", msg);
                    byte[] data = TypeUtil.ObjectToByte(msg);
                    byte[] packet = PacketUtil.constructPacket(data);

                    mBos.write(packet);
                    mBos.flush();
                    Timber.d("after send out message: %s", msg);
                }
            }
        } catch (InterruptedException | IOException e){
            onFailed("Send Msg Failed");
            e.printStackTrace();
        } finally {
            if (mListener != null) {
                mListener.onTerminated();
            }
        }

    }

    private void onFailed(String errorMsg){
        if(mListener != null){
            mListener.onFailed(errorMsg);
        }
    }

    public static void appendMsg(String data){
        synchronized (msgQueue){
            msgQueue.offer(data+"\n");
            msgQueue.notify();
        }

    }

    // todo: wait for finishing sending message
    public void close(){
        try {
            isStop = true;
            interrupt();
            if (mBos != null) {
                // prevent stopping writing
                synchronized (mBos) {
                    mBos.close();
                }
            }
            msgQueue.clear();
        } catch (IOException e) {
            onFailed("Send Close Failed");
            e.printStackTrace();
        }
    }
}
