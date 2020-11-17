package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    public abstract int addActivity(Activity activity);

    public abstract List<Activity> pageList(Map paramMap);

    public abstract int getTotalCondition(Map paramMap);

    public abstract int deleteById(String[] ids);

    public abstract Activity getUserListAndActivity(String id);

    public abstract int updateActivity(Map<String, String> map);

    Activity getDetail(String id);
}
