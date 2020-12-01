package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    public abstract int getCountByCid(String[] ids);

    public abstract int deleteByCid(String[] ids);

    public List<ClueRemark> getCluesByClueId(String clueId);
}
