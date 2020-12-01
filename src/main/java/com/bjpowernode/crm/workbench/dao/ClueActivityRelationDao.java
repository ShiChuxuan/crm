package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    public abstract int relieveById(String id);

    public abstract int relate(List<ClueActivityRelation> relations);

    public abstract int getCountByCid(String[] ids);

    public abstract int deleteByCid(String[] ids);

    public abstract List<String> getActivityIdByCid(String clueId);
}
