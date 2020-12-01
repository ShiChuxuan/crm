package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    public abstract int saveClue(Clue clue);

    public abstract List<Clue> pageList(Map<String, Object> paramMap);

    public abstract int getTotalCondition(Map<String, Object> paramMap);

    public abstract int deleteClue(String[] ids);

    public abstract Clue getClueById(String id);

    public abstract Clue getClueById2(String id);
}
