package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    public abstract int addActivity(Activity activity);

    public abstract List<Activity> pageList(Map paramMap);

    public abstract int getTotalCondition(Map paramMap);

    public abstract int deleteById(String[] ids);

    public abstract Activity getUserListAndActivity(String id);

    public abstract int updateActivity(Map<String, String> map);

    public abstract Activity getDetail(String id);

    public abstract List<Activity> getActivityListByClueId(String clueId);

    public abstract List<Activity> getActivityListWithoutClueId(String clueId);

    public abstract List<Activity> getActivityListByNameAndNotByClueId(@Param("clueId")String clueId, @Param("aname") String aname);

    public abstract List<Activity> getActivityListByName(@Param("clueId") String clueId, @Param("aname")String aname);

    public abstract List<Activity> getAllActivities();

    List<Activity> getActivityListByName2(String aname);
}
