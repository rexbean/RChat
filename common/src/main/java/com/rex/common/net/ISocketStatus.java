package com.rex.common.net;

public interface ISocketStatus{
    void onFailed(String errorMsg);

    void onConnected();
}
