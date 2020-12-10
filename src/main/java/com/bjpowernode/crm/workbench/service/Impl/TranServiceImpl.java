package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private SqlSession session = SqlSessionUtil.getSqlSession();
    private TranDao tranDao = session.getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = session.getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = session.getMapper(CustomerDao.class);

    @Override
    public Map<String, Object> pageList(Map map) {
        Map<String,Object>result = new HashMap();

        //total
        int total = tranDao.getTotal();
        //tranList
        List<Tran>tranList = tranDao.pageList(map);

        map.put("total",total);
        map.put("tranList",tranList);
        return map;
    }

    @Override
    public boolean save(Map<String,String>paramMap) {
        /*
        *
        *   交易添加业务：
        *
        *       在做添加之前，参数t里面就少了一项信息，就是客户的主键，customerId，
        *
        *       先处理客户的相关的需求
        *
        *       (1) 判断customerName,根据客户名称在客户表进行精确查询
        *               如果有这个客户，则取出这个客户的id，封装到t对象中
        *               如果没有这个客户，则在客户表新建一条客户信息，然后将新建的客户的id取出，封装到t对象
        *
        *       (2)经过以上操作后，t对象中的信息就全了，需要执行添加交易操作
        *
        *       (3)添加交易完毕后，需要创建一条交易历史
        *
        *
        *
        * */

        boolean flag = false;
        //创建交易
            //查询customerId
            String customerName = paramMap.get("customerName");
            String customerId = customerDao.getCustomerIdByName(customerName);
            if(customerId==null || customerId.length()==0){
                //没有这个客户,创建新的客户
                Customer customer = new Customer();
                customer.setId(UUIDUtil.getUUID());//id
                customer.setOwner(paramMap.get("owner"));//owner
                customer.setName(customerName);//name
                customer.setCreateTime(paramMap.get("createTime"));//createTime
                customer.setCreateBy(paramMap.get("createBy"));//createBy
                int count = customerDao.createCustomer(customer);
                if(count!=1){
                    return false;
                }
                paramMap.put("customerId",customer.getId());
            }else {
                paramMap.put("customerId",customerId);
            }


        int count1 = tranDao.save(paramMap);
        if(count1!=1){
            return flag =false;
        }
        //创建交易历史
        Map paramMap2 = new HashMap();

        TranHistory history = new TranHistory();
        history.setId(UUIDUtil.getUUID());
        history.setStage(paramMap.get("stage"));
        history.setMoney(paramMap.get("money"));
        history.setExpectedDate(paramMap.get("expectedDate"));
        history.setCreateTime(paramMap.get("createTime"));
        history.setCreateBy(paramMap.get("createBy"));
        history.setTranId(paramMap.get("id"));

        int count2 =tranHistoryDao.createTranHistory(history);
        if(count2!=1){
            return flag = false;
        }
        flag=true;
        return flag;
    }

    @Override
    public Tran getDetail(String id) {
        Tran tran = tranDao.getDetail(id);
        return tran;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> tranHistoryListList = tranHistoryDao.getHistoryListByTranId(tranId);
        return tranHistoryListList;
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = false;

        //改变交易阶段
        int count1 = tranDao.changeStage(tran);
        if(count1!=1){
            return flag = false;
        }
        //添加交易历史
        TranHistory tranHistory = new TranHistory();

        tranHistory.setId(UUIDUtil.getUUID());//交易历史id
        tranHistory.setStage(tran.getStage());//交易历史阶段
        tranHistory.setMoney(tran.getMoney());//交易历史金额
        tranHistory.setExpectedDate(tran.getExpectedDate());//交易历史预计成交日期
        tranHistory.setCreateTime(tran.getEditTime());//交易历史创建时间
        tranHistory.setCreateBy(tran.getEditBy());//交易历史创建人
        tranHistory.setTranId(tran.getId());//交易

        int count2 = tranHistoryDao.createTranHistory(tranHistory);
        if(count2!=1){
            return flag = false;
        }
        flag = true;

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        Map<String, Object>  map = new HashMap<>();

        int total = tranDao.getTotal();
        List<Map<String,Object>> dataList = tranDao.getCharts();

        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }

}
