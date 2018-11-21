package com.rex.factory.net;

public interface INetwork {


    void connect();

    void sendMessage(String data);

    void close();

}
