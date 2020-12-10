package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.ContactsServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.TranServiceImpl;
import com.bjpowernode.crm.workbench.service.TranService;
import jdk.swing.interop.SwingInterOpUtils;
import org.apache.ibatis.session.SqlSession;

import javax.print.DocFlavor;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入交易控制器");
        String path =  request.getServletPath();
        if("/workbench/tran/pageList.do".equals(path)){
            PageList(request,response);
        }else if("/workbench/tran/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/tran/getActivities.do".equals(path)){
            getActivities(request,response);
        }else if("/workbench/tran/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if("/workbench/tran/add.do".equals(path)){
            add(request,response);
        }else if("/workbench/tran/getAllContacts.do".equals(path)){
            getAllContacts(request,response);
        }else if("/workbench/tran/getContactsByName.do".equals(path)){
            getContactsByName(request,response);
        }else if("/workbench/tran/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if("/workbench/tran/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/tran/getDetail.do".equals(path)){
            getDetail(request,response);
        }else if("/workbench/tran/getHistoryListByTranId.do".equals(path)){
            getHistoryListByTranId(request,response);
        }else if("/workbench/tran/changeStage.do".equals(path)){
            changeStage(request,response);
        }else if("/workbench/tran/getCharts.do".equals(path)){
            getCharts(request,response);
        }
    }


    //pageList完善完毕了
    private void PageList(HttpServletRequest request,HttpServletResponse response) {
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customer = request.getParameter("customer");
        String stage = request.getParameter("stage");
        String transactionType = request.getParameter("transactionType");
        String source = request.getParameter("source");
        String contact = request.getParameter("contact");

        //每页展示的记录
        Integer pageSize = Integer.parseInt(pageSizeStr);
        //计算略过的条数
        Integer skipCount = (Integer.parseInt(pageNoStr)-1)*pageSize;

        Map<String,Object>map = new HashMap();
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        map.put("owner",owner);
        map.put("name",name);
        map.put("customer",customer);
        map.put("stage",stage);
        map.put("transactionType",transactionType);
        map.put("source",source);
        map.put("contact",contact);


        TranService service = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String, Object> result = service.pageList(map);
        PrintJson.printJsonObj(response,map);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = service.getUserList();
        PrintJson.printJsonObj(response,userList);
    }

    private void getActivities(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activities = service.getAllActivities();
        PrintJson.printJsonObj(response,activities);
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String aname = request.getParameter("aname");
        ActivityService service = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activities = service.getActivityListByName2(aname);
        PrintJson.printJsonObj(response,activities);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService service  = (UserService)ServiceFactory.getService(new UserServiceImpl());
        List <User> userList = service.getUserList();
        request.setAttribute("uList",userList);
        RequestDispatcher report = request.getRequestDispatcher("/workbench/transaction/save.jsp");
        report.forward(request,response);
    }

    private void getAllContacts(HttpServletRequest request, HttpServletResponse response) {
        ContactsService service = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        List<Contacts> contacts = service.getAllContacts();
        PrintJson.printJsonObj(response,contacts);
    }

    private void getContactsByName(HttpServletRequest request, HttpServletResponse response) {
        String cname = request.getParameter("cname");
        ContactsService service = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        List<Contacts> contacts = service.getContactsByName(cname);
        PrintJson.printJsonObj(response,contacts);
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        CustomerService service = (CustomerService)ServiceFactory.getService(new CustomerServiceImpl());
        List<String> nameList = service.getCustomerName(name);
        PrintJson.printJsonObj(response,nameList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        TranService  service= (TranService)ServiceFactory.getService(new TranServiceImpl());
        Map<String,String>paramMap = new HashMap();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()){
            String name = names.nextElement();
            String value = request.getParameter(name);
            paramMap.put(name,value);
        }
        //交易id
        String id = UUIDUtil.getUUID();
        //创建人
        String createBy = user.getName();
        //创建时间
        String createTime = DateTimeUtil.getSysTime();

        paramMap.put("id",id);
        paramMap.put("createBy",createBy);
        paramMap.put("createTime",createTime);

        boolean flag = service.save(paramMap);
        if(flag){
            /*RequestDispatcher report = request.getRequestDispatcher("/workbench/transaction/index.jsp");
            report.forward(request,response);*/
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }else{

        }


    }

    private void getDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id =request.getParameter("id");
        TranService service = (TranService)ServiceFactory.getService(new TranServiceImpl());
        Tran tran = service.getDetail(id);
        //处理可能性
        String stage = tran.getStage();
        Map pMap	 = (Map)request.getServletContext().getAttribute("pMap");
        String possibility = (String) pMap.get(stage);
        tran.setPossibility(possibility);
        request.setAttribute("tran",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        TranService service = (TranService)ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> tranHistoryList= service.getHistoryListByTranId(tranId);
        Map<String,String>pMap = (Map<String,String>)request.getServletContext().getAttribute("pMap");
        //处理可能性
        for(TranHistory tranHistory:tranHistoryList){
            String stage = tranHistory.getStage();
            String possibility = pMap.get(stage);
            tranHistory.setPossibility(possibility);
        }
        PrintJson.printJsonObj(response,tranHistoryList);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        TranService service = (TranService)ServiceFactory.getService(new TranServiceImpl());

        String id = request.getParameter("tranId");//id
        String stage = request.getParameter("stage");//修改的阶段
        String money = request.getParameter("money");//金额
        String expectedDate = request.getParameter("expectedDate");//预计成交日期
        String possibility = ((Map<String,String>)request.getServletContext().getAttribute("pMap")).get(stage);//可能性
        String editBy = ((User)request.getSession().getAttribute("user")).getName();//修改人
        String editTime = DateTimeUtil.getSysTime();//修改时间

        Tran tran = new Tran();
        tran.setId(id);
        tran.setStage(stage);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        tran.setPossibility(possibility);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);

        boolean flag = service.changeStage(tran);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        if(flag){
            map.put("tran",tran);
        }
        PrintJson.printJsonObj(response,map);


    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        /*
        *   业务层为我们返回
        *       total
        *       dataList
        *
        *       通过map打包以上两项进行返回
        *
        *
        * */
        TranService service = (TranService)ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = service.getCharts();
        PrintJson.printJsonObj(response,map);
    }

}
