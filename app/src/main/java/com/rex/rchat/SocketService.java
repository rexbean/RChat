package com.rex.rchat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.rex.common.net.ISocketStatus;
import com.rex.common.net.NetworkStatus;
import com.rex.common.net.SocketManager;

public class SocketService extends Service implements ISocketStatus {
    private boolean hasReconnect = false;
    private boolean hasLost = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // isConnected -> true: has connected
    //                      or disconnected
    //             -> false: hasn't connected
    // if(!isConnected)
    //     connect
    // else
    //     send HeartBeat wait callback
    @Override
    public void onCreate() {
        super.onCreate();
        if(SocketManager.getInstance().getConnectionStatus() != NetworkStatus.CONNECTED){
            SocketManager.getInstance()
                    .setListener(this)
                    .connect();
        }
        listenToNetworkChange();

    }

    private void sendHeartBeat(){
//        Handler myHandler = new Handler(getMainLooper());
//        myHandler.postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                try{
//                    SocketManager.getInstance().sendHeartBeat();
//                    myHandler.postDelayed(this,2000);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }, 2000);
    }


    private void listenToNetworkChange(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder().build(),
                    new ConnectivityManager.NetworkCallback() {
                        /**
                         * @param network network
                         */
                        @Override
                        public void onAvailable(Network network) {
                            hasLost = false;
                            if(SocketManager.getInstance().getConnectionStatus() != NetworkStatus.CONNECTED){
                                SocketManager.getInstance()
                                        .setListener(SocketService.this)
                                        .connect();
                            }
                        }

                        /**
                         * @param network network
                         */
                        @Override
                        public void onLost(Network network) {
                            hasLost = true;
                            show("network has lost");
                            // todo: event bus to activities
                        }
                    });
        }
    }

    public void close(){
        if(SocketManager.getInstance().getConnectionStatus() == NetworkStatus.CONNECTED){
            SocketManager.getInstance().close();
        }
    }

    public void onConnected(){
        hasReconnect = false;
        sendHeartBeat();
        // todo: eventbus other activities

    }

    @Override
    public void onDestroy() {
        close();
        super.onDestroy();
    }

    @Override
    public void onFailed(String errorMsg) {
        if(!hasLost){
            if(!hasReconnect){
                hasReconnect = true;
                SocketManager.getInstance().reconnect();
            } else {
                hasReconnect = false;
                show(errorMsg);
            }
        }

    }

    // todo: move this into the application for reuse
    private void show(String message){
        try {
            Handler myHandler = new Handler(getMainLooper());
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SocketService.this,
                            message,
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
