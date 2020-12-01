package com.bjpowernode.crm.workbench.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactsActivityRelationDao {

    public abstract int relate(@Param("id") String id, @Param("contactId") String contactId, @Param("activityId") String aid);
}
