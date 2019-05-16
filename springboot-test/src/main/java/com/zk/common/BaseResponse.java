package com.zk.common;

public class BaseResponse<T> {

    private boolean isSuccess = true;
    private String errorMsg;
    private T content;

    public static <T> BaseResponse<T> success (T content) {
        BaseResponse<T> baseResponse = new BaseResponse<T>();
        baseResponse.setContent(content);
        return baseResponse;
    }

    public static BaseResponse error (String errorMsg) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setErrorMsg(errorMsg);
        return baseResponse;
    }

    private BaseResponse () {
    }

    public boolean isSuccess () {
        return isSuccess;
    }

    public void setSuccess (boolean success) {
        isSuccess = success;
    }

    public String getErrorMsg () {
        return errorMsg;
    }

    public void setErrorMsg (String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getContent () {
        return content;
    }

    public void setContent (T content) {
        this.content = content;
    }
}
