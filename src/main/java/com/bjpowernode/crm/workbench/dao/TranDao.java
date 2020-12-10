package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TranDao {

    public abstract int createTran(Tran t);

    public abstract int getTotal();

    public abstract List<Tran> pageList(Map map);

    public int save(Map<String, String> paramMap);

    public abstract Tran getDetail(String id);

    public abstract Tran getTranById(String id);

    public abstract int changeStage(Tran tran);

    public abstract List<Map<String,Object>> getCharts();


}
