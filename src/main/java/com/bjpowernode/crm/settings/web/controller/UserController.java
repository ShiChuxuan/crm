package com.bjpowernode.crm.settings.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户模块控制器");
        String path = request.getServletPath();//获取path 例如：/settings/user/login.do
        if("/settings/user/login.do".equals(path)){

            login(request,response);

        }else if("/settings/user/xxx.do".equals(path)){

            //xxx(request,response);

        }
    }

    public static void login(HttpServletRequest request, HttpServletResponse response){
        System.out.println("用户登录模块");
    }
}
