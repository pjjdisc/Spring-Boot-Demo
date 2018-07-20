package com.demo.controller.tool;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 14:43
 */
public class RestResponse {
    private Integer resultCode;
    private String resultMsg;
    private Object resultData;

    public RestResponse(Integer resultCode, String resultMsg){
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public RestResponse(Object resultData){
        this.resultData = resultData;
        this.resultCode = 0;
        this.resultMsg = "SUCCESS";
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }
}
