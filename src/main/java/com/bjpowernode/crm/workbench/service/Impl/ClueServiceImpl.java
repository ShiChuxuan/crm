package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    SqlSession session = SqlSessionUtil.getSqlSession();
    private ClueDao clueDao = session.getMapper(ClueDao.class);
    private UserDao userDao = session.getMapper(UserDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = session.getMapper(ClueActivityRelationDao.class);

    @Override
    public boolean saveClue(Clue clue) {
        int count  = clueDao.saveClue(clue);
        if(count==1){
            return true;
        }
        return false;
    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> paramMap) {
        PaginationVO<Clue> vo = new PaginationVO<Clue>();

        List<Clue> clues = clueDao.pageList(paramMap);
        int total = clueDao.getTotalCondition(paramMap);

        vo.setDataList(clues);
        vo.setTotal(total);

        return vo;
    }

    @Override
    public boolean deleteClue(String[] ids) {
        int count  = clueDao.deleteClue(ids);
        if(count>=1){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> getUserListAndClue(String id) {
        Map<String, Object> map = new HashMap<>();
        List<User> userList   = userDao.getUserList();
        Clue clue = clueDao.getClueById(id);

        map.put("userList",userList);
        map.put("clue",clue);
        return map;

    }

    @Override
    public Clue getDetail(String id) {

        Clue clue = clueDao.getClueById(id);
        return clue;
    }

    @Override
    public boolean relieveById(String id) {
        int count = clueActivityRelationDao.relieveById(id);
        if(count==1){
            return true;
        }
        return false;
    }


}
