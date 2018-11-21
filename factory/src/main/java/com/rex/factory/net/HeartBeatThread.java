package com.rex.factory.net;

public class HeartBeatThread<Data> extends Thread{
    private static HeartBeatThread instance=null;
    private HeartBeatThread(){

    }
    public static HeartBeatThread getInstance(){
        if(instance==null){
            synchronized(HeartBeatThread.class){
                if(instance==null){
                    instance=new HeartBeatThread();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        String heartBeat = "heart Beat";
        SendThread.appendMsg(heartBeat+"\n");
    }
}


