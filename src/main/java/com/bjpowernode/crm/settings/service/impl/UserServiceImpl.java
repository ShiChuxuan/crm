package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class UserServiceImpl implements UserService {
    private SqlSession session  = SqlSessionUtil.getSqlSession();
    private UserDao userDao = session.getMapper(UserDao.class);


    @Override
    public void login(String loginAct, String loginPwd) {
        User user = userDao.login(loginAct,loginPwd);

        String expireTime = user.getExpireTime();       //有效时间
        String currentTime = DateTimeUtil.getSysTime(); //系统时间
        if(expireTime.compareTo(currentTime)>=0){       //根据数据字典比较
            System.out.println("账户在有效期内");
        }else{
            System.out.println("账户已过期");
        }

        //判断是否锁定
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            System.out.println("账户已经锁定");
        }else if("1".equals(lockState)){
            System.out.println("允许登录");
        }

        //判断ip地址是否合法
        String allowIps = user.getAllowIps(); //允许访问的ip
        String ip = "192.168.1.1";           //浏览器的ip
        if(allowIps.contains(ip)){
            System.out.println("ip合法");
        }else{
            System.out.println("此ip不允许登录");
        }

    }
}
