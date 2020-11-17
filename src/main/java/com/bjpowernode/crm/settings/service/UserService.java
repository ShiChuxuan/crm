package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    public abstract User login(String loginAct, String loginPwd, String ip);
    public abstract List<User> getUserList();
}
