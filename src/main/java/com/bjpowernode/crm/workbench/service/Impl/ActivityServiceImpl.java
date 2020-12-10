package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;

import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.ibatis.session.SqlSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private SqlSession session = SqlSessionUtil.getSqlSession();
    private ActivityDao activityDao = session.getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = session.getMapper(ActivityRemarkDao.class);
    private UserDao userDao = session.getMapper(UserDao.class);


    @Override
    public boolean addActivity(String owner, String name, String startDate, String endDate, String cost, String description,String createBy) {
        Activity activity = new Activity();
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        //设置ID
        activity.setId(UUIDUtil.getUUID());
        //创建时间
        String createTime = DateTimeUtil.getSysTime();
        activity.setCreateTime(createTime);
        //创建人
        activity.setCreateBy(createBy);

        int flag = session.getMapper(ActivityDao.class).addActivity(activity);
        if (flag == 1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public PaginationVO<Activity> pageList(Map paramMap) {
        //创建vo
        PaginationVO<Activity> vo = new PaginationVO();
        //获得dataList
        List <Activity> activities = activityDao.pageList(paramMap);
        //获得total
        int total = activityDao.getTotalCondition(paramMap);
        //放入数据
        vo.setTotal(total);
        vo.setDataList(activities);

        return vo;
    }

    @Override
    public boolean deleteActivity(String[] ids) {
        boolean flag = false;
        //查询应该删除的备注条数
        int count1 = activityRemarkDao.getCountByAid(ids);

        //删除备注
        int count2 = activityRemarkDao.deleteByAid(ids);

        if(count1!=count2){
            return flag = false;
        }

        //删除完备注以后，开始删除市场活动表
        int count3 = activityDao.deleteById(ids);

        if(count3==ids.length){
            flag = true;
        }
        return flag;
    }

    @Override
    public Map<String,Object> getUserListAndActivity(String id) {
        Map<String,Object> map = new HashMap();
        //获得用户列表
        List <User> userList= userDao.getUserList();
        //获得市场活动表
        Activity activity = activityDao.getUserListAndActivity(id);
        map.put("uList",userList);
        map.put("a",activity);
        return map;
    }

    @Override
    public boolean updateActivity(Map<String, String> map) {
        int flag = activityDao.updateActivity(map);

        if(flag==1){
            return true;
        }

        return false;
    }

    @Override
    public Activity getDetail(String id) {
        Activity activity = activityDao.getDetail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> showRemarkList(String aid) {
        List<ActivityRemark> remarks = activityRemarkDao.showRemarkList(aid);
        return remarks;
    }

    @Override
    public boolean deleteRemark(String id) {

        int flag = activityRemarkDao.deleteRemark(id);
        if(flag==1){
            return true;
        }
        return false;

    }

    @Override
    public Map<String,Object> addRemark(Map map)   {
        Map result = new HashMap();
        //新建备注
        boolean flag = false;
        int count  = activityRemarkDao.addRemark(map);
        if(count==1){
            flag=true;
        }
        //返回新的备注
        String id = (String) map.get("id");
        ActivityRemark remark = activityRemarkDao.getRemarkById(id);

        //塞入map
        result.put("success",flag);
        result.put("remark",remark);


        return result;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        int result = activityRemarkDao.updateRemark(ar);
        if(result==1){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> getActivityListByClueId(String clueId) {
        Map<String, Object> map = new HashMap<>();
        List<Activity> activityList = activityDao.getActivityListByClueId(clueId);
        if(activityList.size()!=0){
            map.put("success",true);
            map.put("activityList",activityList);
        }else{
            map.put("success",false);
        }
        return map;
    }

    @Override
    public List<Activity> getActivityListWithoutClueId(String clueId) {
        List<Activity> activities = activityDao.getActivityListWithoutClueId(clueId);
        return activities;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(String clueId, String aname) {
        List<Activity> activities = activityDao.getActivityListByNameAndNotByClueId(clueId,aname);
        return activities;
    }

    @Override
    public List<Activity> getActivityListByName(String clueId, String aname) {
        List<Activity> activities=activityDao.getActivityListByName(clueId,aname);
        return activities;
    }

    @Override
    public List<Activity> getAllActivities() {
        List<Activity> activities = activityDao.getAllActivities();
        return activities;
    }

    @Override
    public List<Activity> getActivityListByName2(String aname) {
        List<Activity> activities = activityDao.getActivityListByName2(aname);
        return activities;
    }


}
