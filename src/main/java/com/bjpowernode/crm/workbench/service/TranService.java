package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    public abstract Map<String, Object> pageList(Map map);

    public abstract boolean save(Map<String,String>paramMap);

    public abstract Tran getDetail(String id);

    public abstract List<TranHistory> getHistoryListByTranId(String tranId);

    public abstract boolean changeStage(Tran tran);

    public abstract Map<String, Object> getCharts();
}
