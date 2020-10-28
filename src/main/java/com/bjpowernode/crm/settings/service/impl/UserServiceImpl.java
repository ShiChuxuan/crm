package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.MD5Util;
import com.bjpowernode.crm.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;


public class UserServiceImpl implements UserService {
    private SqlSession session  = SqlSessionUtil.getSqlSession();
    private UserDao userDao = session.getMapper(UserDao.class);


    @Override
    public User login(String loginAct, String loginPwd,String ip)  {
        loginPwd = MD5Util.getMD5(loginPwd);//密码加密
        User user = userDao.login(loginAct,loginPwd);
//---------------------------------------------------------------------------
        if(user==null){
            //判断账号是否为空
            throw new LoginException("账号未注册");
        }
//---------------------------------------------------------------------------
        String expireTime = user.getExpireTime();       //有效时间
        String currentTime = DateTimeUtil.getSysTime(); //系统时间
        if(expireTime.compareTo(currentTime)>=0){       //根据数据字典比较
            //账户在有效期内
        }else{
            throw new LoginException("账户已过期");
        }
//---------------------------------------------------------------------------
        //判断是否锁定
        String lockState = user.getLockState();
        if("1".equals(lockState)){
            //未被锁定
        }else{
            throw new LoginException("账户已被锁定");
        }
//---------------------------------------------------------------------------
        //判断ip地址是否合法
        String allowIps = user.getAllowIps(); //允许访问的ip
        if(allowIps.contains(ip)){
            //ip合法
        }else{
            throw new LoginException("此ip不允许登录");
        }
//---------------------------------------------------------------------------
        return user;
    }
}
