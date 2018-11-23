package com.rex.common.net;

public interface INetwork {


    void connect();

    void sendMessage(String data);

    void close();

    void reconnect();

}
