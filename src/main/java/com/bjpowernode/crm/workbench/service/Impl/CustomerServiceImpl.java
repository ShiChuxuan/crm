package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private SqlSession session = SqlSessionUtil.getSqlSession();
    private CustomerDao customerDao = session.getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {
        List<String> nameList = customerDao.getCustomerName(name);
        return nameList;
    }
}
