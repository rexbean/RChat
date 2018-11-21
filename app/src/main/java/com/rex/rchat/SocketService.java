package com.rex.rchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.rex.factory.net.NetworkStatus;
import com.rex.factory.net.SocketManager;

public class SocketService extends Service implements SocketManager.IConnection{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(SocketManager.getInstance().getConnectionStatus() != NetworkStatus.CONNECTED){
            SocketManager.getInstance()
                    .setListener(this)
                    .connect();
        }
    }

    public void close(){
        if(SocketManager.getInstance().getConnectionStatus() == NetworkStatus.CONNECTED){
            SocketManager.getInstance().close();
        }

    }


    @Override
    public void onDestroy() {
        close();
        super.onDestroy();
    }

    @Override
    public void onConnect() {
    }



    @Override
    public void onDisconnect() {
        Toast.makeText(SocketService.this,
                NetworkStatus.DISCONNECTED.getStatus(),
                Toast.LENGTH_LONG)
             .show();
    }

    @Override
    public void onFailed() {
        Toast.makeText(SocketService.this,
                NetworkStatus.FAILED.getStatus(),
                Toast.LENGTH_LONG)
                .show();
    }
}
