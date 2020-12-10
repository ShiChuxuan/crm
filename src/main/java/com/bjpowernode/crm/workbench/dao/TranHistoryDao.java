package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    public abstract int createTranHistory(TranHistory history);

    public abstract List<TranHistory> getHistoryListByTranId(String tranId);
}
