package com.demo.vo;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 10:11
 */
public class RequestIp {
    private String ip;
    private Long createTime;
    private Integer reCount;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getReCount() {
        return reCount;
    }

    public void setReCount(Integer reCount) {
        this.reCount = reCount;
    }
}
