package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.*;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      System.out.println("进入市场活动控制器");
      String path =  request.getServletPath();
      if("/workbench/activity/getUserList.do".equals(path)){
          getUserList(request,response);
      }else if("/workbench/activity/addActivity.do".equals(path)){
          addActivity(request,response);
      }else if("/workbench/activity/pageList.do".equals(path)){
          pageList(request,response);
      }else if("/workbench/activity/deleteActivity.do".equals(path)){
          deleteActivity(request,response);
      }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
          getUserListAndActivity(request,response);
      }else if("/workbench/activity/updateActivity.do".equals(path)){
          updateActivity(request,response);
      }else if("/workbench/activity/getDetail.do".equals(path)){
          getDetail(request,response);
      }else if("/workbench/activity/showRemarkList.do".equals(path)){
          showRemarkList(request,response);
      }else if("/workbench/activity/DeleteRemark.do".equals(path)){
          deleteRemark(request,response);
      }else if("/workbench/activity/addRemark.do".equals(path)){
          addRemark(request,response);
      }else if("/workbench/activity/updateRemark.do".equals(path)){
          updateRemark(request,response);
      }

    }


    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User>userList = service.getUserList();
        PrintJson.printJsonObj(response,userList);
    }

    private void addActivity(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        User user = (User) request.getSession().getAttribute("user");

         String owner = request.getParameter("owner");
         String name  = request.getParameter("name");
         String startDate = request.getParameter("startDate");
         String endDate = request.getParameter("endDate");
         String cost = request.getParameter("cost");
         String description = request.getParameter("description");
         String createBy =  user.getName();

         boolean flag = service.addActivity(owner,name,startDate,endDate,cost,description,createBy);
         PrintJson.printJsonFlag(response,flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object>ParamMap  = new HashMap<String,Object>();

        ParamMap.put("searchName",request.getParameter("searchName"));
        ParamMap.put("searchOwner",request.getParameter("searchOwner"));
        ParamMap.put("searchStartDate",request.getParameter("searchStartDate"));
        ParamMap.put("searchEndDate",request.getParameter("searchEndDate"));

        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        //每页展示的记录
        Integer pageSize = Integer.parseInt(pageSizeStr);
        //计算略过的条数
        Integer skipCount = (Integer.parseInt(pageNoStr)-1)*pageSize;

        ParamMap.put("skipCount",skipCount);
        ParamMap.put("pageSize",pageSize);

        /*
        *
        *   前端要：市场活动信息列表
        *          查询的总条数
        *
        *       业务层拿到了以上两项信息之后，如果做返回
        *       map
        *       map.put("dataList":dataList)
        *       map.put("total":total)
        *       PrintJSON map -->json
        *       {"total":100,"dataList":[{市场活动1},{2},{3}]}
        *
        *     vo
        *     PaginationVO<T>               将来给T传入的是什么，List就是什么
        *         private int total;
        *         private List<T> dtaList;
        *
        *     PaginationVO<Activity> vo = new PaginationVo();
        *     vo.setTotal(total);
        *     vo.setDataList(dataList);
        *     PrintJSON vo -->json
        *     {"total":100,"dataList":[{市场活动1},{2},{3}]}
        *
        *     将来分页查询，每个模块都有，所以我们选择使用一个通用vo，操作起来方便
        * */
        PaginationVO<Activity>vo = service.pageList(ParamMap);
        PrintJson.printJsonObj(response,vo);
    }

    private void deleteActivity(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        String [] ids = request.getParameterValues("id");
        boolean result = service.deleteActivity(ids);
        PrintJson.printJsonFlag(response,result);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        //获得id
        String id  = request.getParameter("id");
        ActivityService service = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        /*
        *       总结：
        *           controller调用service的方法，返回值应该是什么
        *           你得想一想前端要什么，就要从service层取什么
        *
        *       前端需要的，管业务层去要
        *
        *       uList
        *       a
        *
        *       以上两项信息，复用率不高，我们选择使用map打包这两项信息即可
        *       map
        * */
        Map<String,Object>map = service.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);
    }

    private void updateActivity(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        String editBy   = user.getName();

        Map<String,String>map = new HashMap();
        map.put("id",id);
        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("cost",cost);
        map.put("description",description);
        map.put("editTime",editTime);
        map.put("editBy",editBy);

        boolean flag = service.updateActivity(map);
        PrintJson.printJsonFlag(response,flag);


    }

    private void getDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ActivityService service = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity =service.getDetail(id);
        request.setAttribute("activity",activity);
        //重定向
        RequestDispatcher report = request.getRequestDispatcher("/workbench/activity/detail.jsp");
        report.forward(request,response);
    }

    private void showRemarkList(HttpServletRequest request, HttpServletResponse response) {
        String Aid = request.getParameter("Aid");
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarks= service.showRemarkList(Aid);
        PrintJson.printJsonObj(response,remarks);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void addRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createBy = request.getParameter("createBy");
        String aid = request.getParameter("aid");
        String createTime = DateTimeUtil.getSysTime();
        String editFlag = "0";
        Map map = new HashMap();
        map.put("id",id);
        map.put("noteContent",noteContent);
        map.put("createBy",createBy);
        map.put("aid",aid);
        map.put("createTime",createTime);
        map.put("editFlag",editFlag);
        Map<String,Object> result = service.addRemark(map);

        PrintJson.printJsonObj(response,result);

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        User user = (User)request.getSession().getAttribute("user");

        String id = request.getParameter("id");//id
        String noteContent = request.getParameter("noteContent");//修改备注信息
        String editTime = DateTimeUtil.getSysTime();//修改时间
        String editBy =user.getName();//修改人
        String editFlag= "1";//修改标志

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        boolean flag = service.updateRemark(ar);

        Map map = new HashMap();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);

    }
}
