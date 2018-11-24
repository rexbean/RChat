package com.rex.common.net;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class SocketManager implements INetwork, ISocketStatus{
    private volatile static SocketManager instance;
    private static Socket mSocket;
    private static final int PORT = 6666;
    //private static final String IP_ADDR = "192.168.1.172";
    private static final String IP_ADDR = "10.0.2.2";

    private static OutputThread mOut;
    private static InputThread mIn;

    private boolean hasSendError = false;

    private NetworkStatus mStatus = NetworkStatus.DISCONNECTED;

    private ISocketStatus mListener;

    private SocketManager() {

    }


    public static SocketManager getInstance(){
        if(instance == null){
            synchronized (SocketManager.class){
                if(instance == null){
                    instance = new SocketManager();
                }
            }
        }
        return instance;
    }

    public SocketManager setListener(ISocketStatus mListener) {
        this.mListener = mListener;
        return this;
    }

    @Override
    public void connect() {
        new Thread (){
            @Override
            public void run() {
                try {
                    Log.d("SocketManager", "before connect to server");
                    mSocket = new Socket(IP_ADDR, PORT);
                    Log.d("SocketManager","Socket has connected");
                    init();
                    startThread();
                } catch (IOException e) {
                    e.printStackTrace();
                    if(mListener != null){
                        mListener.onFailed("Establish Connection Failed");
                    }
                }
            }
        }.start();


    }

    public NetworkStatus getConnectionStatus(){
        if(mSocket == null){
            return NetworkStatus.DISCONNECTED;
        }
        return mSocket.isConnected()? mStatus: NetworkStatus.DISCONNECTED;
    }

    private void init() {
        if(mSocket != null){
            mOut = new OutputThread(mSocket, this);
            mIn = new InputThread(mSocket, this);
        }

    }

    private void startThread() {
        mOut.start();
        mIn.start();
        onConnected();
    }

    @Override
    public void sendMessage(String data) {
        if(getConnectionStatus() == NetworkStatus.CONNECTED){
            OutputThread.appendMsg(data);
        } else {
            mListener.onFailed("Send Message Failed, Please Connect First");
        }
    }

    public void sendHeartBeat(){
        //todo: construct heat beat here
        String heartBeat = "heartBeat";
        OutputThread.appendMsg(heartBeat);
    }

    @Override
    public void close() {
        mOut.close();
        mIn.close();
    }

    @Override
    public void reconnect() {
//        for (int i = 0; i < 3; i++){
//            if(getConnectionStatus() != NetworkStatus.CONNECTED){
//                Log.d(this.getClass().getSimpleName(),"reconnect "+ (i+1)+" times");
//                connect();
//            } else {
//                return;
//            }
//        }
        onFailed("reconnection failed");
    }


    @Override
    public void onFailed(String errorMsg) {
        mStatus = NetworkStatus.FAILED;

        if(mListener != null && !hasSendError){
            hasSendError = true;
            mListener.onFailed(errorMsg);
        }

    }

    @Override
    public void onConnected() {
        mStatus = NetworkStatus.CONNECTED;

        hasSendError = false;
        if(mListener != null){
            mListener.onConnected();
        }
    }
}
