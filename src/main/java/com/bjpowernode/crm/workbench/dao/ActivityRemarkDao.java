package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityRemarkDao {

    public abstract int getCountByAid(String[] aids);

    public abstract int deleteByAid(String[] aids);

    public abstract List<ActivityRemark> showRemarkList(String aid);

    public abstract int deleteRemark(String id);

    public abstract int addRemark(Map map);

    public abstract ActivityRemark getRemarkById(String id);

    public abstract int updateRemark(ActivityRemark ar);
}
