package com.rex.factory.net;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class SocketManager implements INetwork{
    private volatile static SocketManager instance;
    private static Socket mSocket;
    private static final int PORT = 6666;
//    private static final String IP_ADDR = "127.0.0.1";
    private static final String IP_ADDR = "10.0.2.2";

    private static SendThread mOut;
    private static ReceiveThread mIn;

    private IConnection mListener;

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

    public SocketManager setListener(IConnection mListener) {
        this.mListener = mListener;
        return this;
    }

    @Override
    public void connect() {
        new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("SocketManager", "before connect to server");
                    mSocket = new Socket(IP_ADDR, PORT);
                    Log.d("SocketManager","Socket has connected");
                    init();
                    start();
                } catch (IOException e) {
                    e.printStackTrace();
                    if(mListener != null){
                        mListener.onFailed();
                    }
                }
            }
        }.run();

    }

    public NetworkStatus getConnectionStatus(){
        return mSocket.isConnected()? NetworkStatus.CONNECTED: NetworkStatus.DISCONNECTED;
    }
    private void init() {
        if(mSocket != null){
            mOut = new SendThread(mSocket);
            mIn = new ReceiveThread(mSocket);
            if(mListener != null){
                mListener.onConnect();
            }

        }
    }

    private void start() {
        mOut.start();
        mIn.start();
        sendHeartBeat();
    }

    @Override
    public void sendMessage(String data) {
        if(getConnectionStatus() == NetworkStatus.CONNECTED){
            SendThread.appendMsg(data);
        } else {
            mListener.onFailed();
        }
    }

    // todo: using AsnTask to send heart beat
    private void sendHeartBeat(){
        //todo: construct heat beat here
        String heartBeat = "heartBeat";
        SendThread.appendMsg(heartBeat);
    }

    @Override
    public void close() {
        mOut.close();
        mIn.close();
    }


    // todo listen to the internet status using broadcast reciver

    public interface IConnection{
        void onConnect();

        void onDisconnect();

        void onFailed();
    }

}
