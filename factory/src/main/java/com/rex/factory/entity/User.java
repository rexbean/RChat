package com.rex.factory.entity;

import java.util.Date;
import java.util.List;

public class User {
    private int userId;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String avatar;
    private List<Integer> friendList;
    private List<Integer> groupList;
    private Date createdTime;
    private Date updateTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Integer> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Integer> friendList) {
        this.friendList = friendList;
    }

    public List<Integer> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Integer> groupList) {
        this.groupList = groupList;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
