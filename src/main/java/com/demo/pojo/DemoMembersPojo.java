package com.demo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 14:46
 */
@Table(name = "demo_members")
public class DemoMembersPojo implements Serializable {
    @Id
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "tel_phone")
    private String telPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "wx_openId")
    private String wxOpenId;

    @Column(name = "wb_openId")
    private String wbOpenId;

    @Column(name = "qq_openId")
    private String qqOpenId;

    @Column(name = "alipay_openId")
    private String alipayOpenId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Column(name = "last_login_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWbOpenId() {
        return wbOpenId;
    }

    public void setWbOpenId(String wbOpenId) {
        this.wbOpenId = wbOpenId;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getAlipayOpenId() {
        return alipayOpenId;
    }

    public void setAlipayOpenId(String alipayOpenId) {
        this.alipayOpenId = alipayOpenId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
