package com.bjpowernode.crm.workbench.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.Impl.ClueServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Provider;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入线索控制器");
        String path =  request.getServletPath();
        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/clue/saveClue.do".equals(path)){
            saveClue(request,response);
        }else if("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/clue/deleteClue.do".equals(path)){
            deleteClue(request,response);
        }else if("/workbench/clue/getUserListAndClue.do".equals(path)){
            getUserListAndClue(request,response);
        }else if("/workbench/clue/getDetail.do".equals(path)){
            getDetail(request,response);
        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }else if("/workbench/clue/relieveById.do".equals(path)){
            relieveById(request,response);
        }else if("/workbench/clue/getActivityListWithoutClueId.do".equals(path)){
            getActivityListWithoutClueId(request,response);
        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
            getActivityListByNameAndNotByClueId(request,response);
        }else if("/workbench/clue/relate.do".equals(path)){
            relate(request,response);
        }else if("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }

    }


    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = service.getUserList();
        PrintJson.printJsonObj(response,userList);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("保存市场活动");
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = false;
        User user = (User) request.getSession().getAttribute("user");
        Clue clue = new Clue();

        String fullname  =request.getParameter("fullname");
        String appellation=request.getParameter("appellation");
        String owner=request.getParameter("owner");
        String company=request.getParameter("company");
        String job=request.getParameter("job");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String website=request.getParameter("website");
        String mphone=request.getParameter("mphone");
        String state=request.getParameter("state");
        String source=request.getParameter("source");
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String address=request.getParameter("address");

        String id = UUIDUtil.getUUID();//id
        String createTime = DateTimeUtil.getSysTime();//创建时间
        String createBy   = user.getName();//创建人

        clue.setId(id);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);


        flag = service.saveClue(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Map<String,Object>ParamMap  = new HashMap<String,Object>();
        ParamMap.put("fullname",request.getParameter("fullname"));
        ParamMap.put("company",request.getParameter("company"));
        ParamMap.put("phone",request.getParameter("phone"));
        ParamMap.put("source",request.getParameter("source"));
        ParamMap.put("owner",request.getParameter("owner"));
        ParamMap.put("mPhone",request.getParameter("mPhone"));
        ParamMap.put("state",request.getParameter("state"));

        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        //每页展示的记录
        Integer pageSize = Integer.parseInt(pageSizeStr);
        //计算略过的条数
        Integer skipCount = (Integer.parseInt(pageNoStr)-1)*pageSize;

        ParamMap.put("skipCount",skipCount);
        ParamMap.put("pageSize",pageSize);

        PaginationVO<Clue> vo = service.pageList(ParamMap);
        PrintJson.printJsonObj(response,vo);

    }

    private void deleteClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入删除线索模块");
        String[] ids = request.getParameterValues("id");

        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag  = service.deleteClue(ids);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndClue(HttpServletRequest request, HttpServletResponse response) {
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String id = request.getParameter("id");
        Map <String,Object> map = service.getUserListAndClue(id);
        PrintJson.printJsonObj(response,map);
    }

    private void getDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String id = request.getParameter("id");
        Clue clue = service.getDetail(id);
        request.setAttribute("clue",clue);
        RequestDispatcher reporter = request.getRequestDispatcher("/workbench/clue/detail.jsp");
        reporter.forward(request,response);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String clueId = request.getParameter("clueId");

        Map<String,Object>map = service.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,map);

    }

    private void relieveById(HttpServletRequest request, HttpServletResponse response) {
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String id  = request.getParameter("id");
        boolean flag = service.relieveById(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListWithoutClueId(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String clueId = request.getParameter("clueId");
        List<Activity> activityList = service.getActivityListWithoutClueId(clueId);
        PrintJson.printJsonObj(response,activityList);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String clueId = request.getParameter("clueId");
        String aname = request.getParameter("aname");
        List<Activity> activityList = service.getActivityListByNameAndNotByClueId(clueId,aname);
        PrintJson.printJsonObj(response,activityList);
    }

    private void relate(HttpServletRequest request, HttpServletResponse response) {
        String [] aids = request.getParameterValues("aid");
        String clueId  = request.getParameter("clueId");
        ClueService service = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = service.relate(clueId,aids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String clueId = request.getParameter("clueId");
        String aname  = request.getParameter("aname");

        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity>activities = service.getActivityListByName(clueId,aname);
        PrintJson.printJsonObj(response,activities);

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("transFlag");//是否创建交易的标记
        String createBy = ((User)request.getSession().getAttribute("user")).getName();//创建者
        Tran t = null;
        //如果需要创建交易
        if("a".equals(flag)){
            //接收表单中的参数
            String money = request.getParameter("amountOfMoney");//交易金额
             String name  = request.getParameter("tradeName");//交易名
             String expectedDate = request.getParameter("expectedClosingDate");//成交日期
             String stage = request.getParameter("stage");//交易阶段
             String activityId = request.getParameter("activityId");//市场活动id
             String id = UUIDUtil.getUUID();//id
             String createTime = DateTimeUtil.getSysTime();//创建时间


             t = new Tran();
             t.setActivityId(activityId);
             t.setMoney(money);
             t.setName(name);
             t.setExpectedDate(expectedDate);
             t.setStage(stage);
             t.setId(id);
             t.setCreateTime(createTime);
             t.setCreateBy(createBy);
        }
        ClueService service = (ClueService)ServiceFactory.getService(new ClueServiceImpl());
        /*
        *
        *   为业务层传递的参数：
        *       1.必须传递的参数clueId,有了这个clueId之后我们才知道要转换哪条记录
        *       2.必须传递的参数t，因为在线索转换的过程中，有可能会临时创建一笔交易（业务层接受的t也有可能是个null）
        *       3.
        *
        * */
        boolean flag1 = service.convert(clueId,t,createBy);
        if(flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

}
