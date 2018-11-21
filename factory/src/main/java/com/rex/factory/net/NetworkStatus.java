package com.rex.factory.net;

public enum NetworkStatus {
    CONNECTED(1, "Connected"),
    DISCONNECTED(0, "Disconnected"),
    FAILED(-1, "Connected Failed");

    private int code;
    private String status;

    NetworkStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
