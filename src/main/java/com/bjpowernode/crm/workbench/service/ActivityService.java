package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    public abstract boolean addActivity(String owner,String name,String startDate,String endDate,String cost,String description,String createBy);
    public abstract PaginationVO<Activity> pageList(Map ParamMap);
    public abstract boolean deleteActivity(String[] ids);
    public abstract Map<String,Object> getUserListAndActivity(String id);
    public abstract boolean updateActivity(Map<String, String> map);
    public abstract Activity getDetail(String id);
    public abstract List<ActivityRemark> showRemarkList(String aid);
    public abstract boolean deleteRemark(String id);
    public abstract Map<String,Object> addRemark(Map map);

    public abstract boolean updateRemark(ActivityRemark ar);
}
