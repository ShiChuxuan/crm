package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.Map;

public interface ClueService {

    public abstract boolean saveClue(Clue clue);


    public abstract PaginationVO<Clue> pageList(Map<String, Object> paramMap);

    public abstract boolean deleteClue(String[] ids);

    public abstract Map<String, Object> getUserListAndClue(String id);

    public abstract Clue getDetail(String id);

    public abstract boolean relieveById(String id);
}
