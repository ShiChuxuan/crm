package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;

public interface UserService {
    public abstract User login(String loginAct, String loginPwd, String ip);
}
