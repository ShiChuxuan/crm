package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.TransactionInvocationHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户模块控制器");
        String path = request.getServletPath();//获取path 例如：/settings/user/login.do
        if("/settings/user/login.do".equals(path)){

            login(request,response);

        }else if("/settings/user/xxx.do".equals(path)){

            //xxx(request,response);
            System.out.println("other...");

        }
    }

    public static void login(HttpServletRequest request, HttpServletResponse response){
        String ip = request.getRemoteAddr();//获取ip地址
        String loginAct = request.getParameter("loginAct");//获取账号
        String loginPwd = request.getParameter("loginPwd");//获取密码
        //获取代理对象
        UserService target = new UserServiceImpl();
        UserService service = (UserService) ServiceFactory.getService(target);

        try {
            User user = service.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            System.out.println("session_id:"+request.getSession().getId());
            PrintJson.printJsonFlag(response,true);//将成功信息以json格式返回，{"success":true}

        }catch (Exception e){
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);//将失败信息以json格式返回，{"success":false,"msg":msg}
        }

    }
}
