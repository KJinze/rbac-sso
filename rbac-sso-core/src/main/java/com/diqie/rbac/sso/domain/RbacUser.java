package com.diqie.rbac.sso.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

public class RbacUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userId; // (用户ID)
    private String userName; // (用户名)
    private String userPassword; // (登陆密码)
    private String userNickname; // (昵称)
    private String userCode; // (用户编号)
    private String userMobile; // (手机)
    private String userEmail; // (邮箱)
    private String userSex; // (性别)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date userBirthday; // (生日)
    private String isDelete; // (是否删除)

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname == null ? null : userNickname.trim();
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile == null ? null : userMobile.trim();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex == null ? null : userSex.trim();
    }

    public java.util.Date getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(java.util.Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }

}
