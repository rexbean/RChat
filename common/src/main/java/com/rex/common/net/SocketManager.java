package com.rex.common.net;


import com.rex.common.utils.PacketUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class SocketManager implements INetwork, ISocketStatus{
    private volatile static SocketManager instance;

    private static final Logger logger = LoggerFactory.getLogger(PacketUtil.class.getSimpleName());
    private static final int PORT = 6666;
    private static final int RECONNECT_INTERVAL = 3000;
    private static final int SOCKET_TIMETOUT = 2000;
    private static final String IP_ADDR = "192.168.1.173";
    private static Socket mSocket;
    //private static final String IP_ADDR = "10.0.2.2";

    private static OutputThread mOut;
    private static InputThread mIn;

    private NetworkStatus mStatus = NetworkStatus.DISCONNECTED;
    private boolean reconnect = false;

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
                    logger.debug("before connect to server");
                    mSocket = new Socket();
                    mSocket.connect(new InetSocketAddress(IP_ADDR,PORT),SOCKET_TIMETOUT);
                    logger.debug("Socket has connected");
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
        try {
            if(mSocket != null){
                mOut = new OutputThread(new BufferedOutputStream(mSocket.getOutputStream()), this);
                mIn = new InputThread(new BufferedInputStream(mSocket.getInputStream()), this);
            }
        } catch (IOException e) {
            if(mListener != null) {
                mListener.onFailed("new thread error");
            }
            e.printStackTrace();
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
        try{
            if(mSocket != null && !mSocket.isClosed()){
                if(!mSocket.isInputShutdown()){
                    mSocket.shutdownInput();
                }
                if(!mSocket.isOutputShutdown()){
                    mSocket.shutdownOutput();
                }
            }

            mIn.close();
            mOut.close();

            mIn = null;
            mOut = null;

            if(reconnect) {
                reconnect();
            }
        } catch (IOException e){
            onFailed("disconnect error");
            e.printStackTrace();
        }
    }

    @Override
    public void reconnect() {
        try {
            for (int i = 0; i < 3; i++){
                if(getConnectionStatus() != NetworkStatus.CONNECTED){
                    wait(RECONNECT_INTERVAL);
                    logger.debug("reconnect "+ (i+1)+" times");
                    connect();
                } else {
                    return;
                }
            }
            onFailed("Reconnect failed");
        } catch (InterruptedException e) {
            onFailed("Error happened during waiting for reconnecting 3 seconds");
        }

    }


    @Override
    public void onFailed(String errorMsg) {
        mStatus = NetworkStatus.FAILED;
        reconnect = true;
    }

    @Override
    public void onTerminated() {
        close();
    }

    @Override
    public void onConnected() {
        mStatus = NetworkStatus.CONNECTED;

        if(mListener != null){
            mListener.onConnected();
        }
    }
}
