package com.zk.common;

public class BaseRequest<T> {

    private String channel;
    private String requestId;
    private T data;

    public String getChannel () {
        return channel;
    }

    public void setChannel (String channel) {
        this.channel = channel;
    }

    public String getRequestId () {
        return requestId;
    }

    public void setRequestId (String requestId) {
        this.requestId = requestId;
    }

    public T getData () {
        return data;
    }

    public void setData (T data) {
        this.data = data;
    }
}
