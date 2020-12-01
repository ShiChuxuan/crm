package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    SqlSession session = SqlSessionUtil.getSqlSession();
    //线索相关表
    private ClueDao clueDao = session.getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = session.getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = session.getMapper(ClueActivityRelationDao.class);
    //客户相关表
    private CustomerDao customerDao = session.getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = session.getMapper(CustomerRemarkDao.class);
    //交易相关表
    private TranDao tranDao = session.getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = session.getMapper(TranHistoryDao.class);
    //联系人相关表
    private ContactsDao contactsDao = session.getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = session.getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = session.getMapper(ContactsActivityRelationDao.class);
    //用户相关表
    private UserDao userDao = session.getMapper(UserDao.class);

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
        //查询要删除的备注条数
        boolean flag = false;
        int count1 =  clueRemarkDao.getCountByCid(ids);
        //删除备注
        int count2 = clueRemarkDao.deleteByCid(ids);
        if(count1!=count2){
            return flag = false;
        }
        //查询要删除的关联市场活动记录条数
        int count3 = clueActivityRelationDao.getCountByCid(ids);
        //删除关联的活动记录
        int count4 = clueActivityRelationDao.deleteByCid(ids);
        if(count3!=count4){
            return  flag = false;
        }
        //删除线索
        int count5 = clueDao.deleteClue(ids);
        if(count5==ids.length){
            flag = true;
        }
        return flag ;

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

    @Override
    public boolean relate(String clueId,String[] aids) {
        int length = aids.length;
        List <ClueActivityRelation> relations = new LinkedList<>();

        //创建uuid、并封装
        for(int i=0;i<length;i++){
            String id = UUIDUtil.getUUID();
            ClueActivityRelation relation = new ClueActivityRelation();
            relation.setId(id);
            relation.setClueId(clueId);
            relation.setActivityId(aids[i]);
            relations.add(relation);
        }
        //执行
        int count =clueActivityRelationDao.relate(relations);
        if(count==length){
            return true;
        }
        return false;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        /*
        *   线索转换：
        *
        *       客户加一
        *
        *       联系人加一
        *
        *       交易加一(取决于t是否为空)
        *
        *
        *       线索减一：与该线索关联的市场活动记录减要全部删除
        *           该线索的相关备注要全部删除
        *           线索删除
        *
        * */
        boolean flag = false;
        String createTime = DateTimeUtil.getSysTime();
        String customerId = UUIDUtil.getUUID();
        String contactId  = UUIDUtil.getUUID();
        String tranId = UUIDUtil.getUUID();
        //通过线索id获取线索对象(线索对象当中封装了线索的信息,公司相关的信息生成客户,人的信息生成联系人)
        Clue clue = clueDao.getClueById2(clueId);

//===============================创建联系人===========================================================
        //联系人创建需要的相关的信息
        String owner = clue.getOwner();
        String source = clue.getSource();
        String fullname = clue.getFullname();
        String appellation = clue.getAppellation();
        String email = clue.getEmail();
        String mphone = clue.getMphone();
        String job    = clue.getJob();
        String description = clue.getDescription();
        String contactSummary = clue.getContactSummary();
        String nextContactTime = clue.getNextContactTime();
        String address = clue.getAddress();

        Contacts contact = new Contacts();
        contact.setId(contactId);
        contact.setOwner(owner);
        contact.setSource(source);
        contact.setCustomerId(customerId);
        contact.setFullname(fullname);
        contact.setAppellation(appellation);
        contact.setEmail(email);
        contact.setMphone(mphone);
        contact.setJob(job);
        contact.setCreateBy(createBy);
        contact.setCreateTime(createTime);
        contact.setDescription(description);
        contact.setContactSummary(contactSummary);
        contact.setNextContactTime(nextContactTime);
        contact.setAddress(address);

        //创建联系人
        int count = contactsDao.createContact(contact);
        if(count!=1){
            return false;
        }
//===============================关联市场活动===========================================================
        //获取关联的市场活动的id
        List<String> aids = clueActivityRelationDao.getActivityIdByCid(clueId);
         //联系人市场活动关联关系表的id
        int count1 = 0;
        for(int i = 0;i < aids.size();i++){
            String id    = UUIDUtil.getUUID();
            String aid   = aids.get(i);
            //创建联系人市场活动关联
            int count2 = contactsActivityRelationDao.relate(id,contactId,aid);
            if(count2==1){
                count1++;
            }
        }

        if(count1!=aids.size()){
            return false;
        }

//===============================转换线索备注成为联系人备注============================================
        //获取线索的备注
        List<ClueRemark> clueRemarks = clueRemarkDao.getCluesByClueId(clueId);
        int count4 = 0;
        for(ClueRemark clueRemark:clueRemarks){
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());//id
            contactsRemark.setNoteContent(clueRemark.getNoteContent());//备注
            contactsRemark.setCreateBy(clueRemark.getCreateBy());//创建者
            contactsRemark.setCreateTime(clueRemark.getCreateTime());//创建时间
            contactsRemark.setEditFlag(clueRemark.getEditFlag());//修改标志
            contactsRemark.setContactsId(contactId);

            //创建线索
            int count3 = contactsRemarkDao.relateRemark(contactsRemark);
            if(count3==1){
                count4++;
            }
        }
        if(count4!=clueRemarks.size()){
            return flag = false;
        }

//===============================创建客户===========================================================
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setOwner(owner);
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(createBy);
        customer.setCreateTime(createTime);
        customer.setContactSummary(contactSummary);
        customer.setNextContactTime(nextContactTime);
        customer.setDescription(description);
        customer.setAddress(address);
        //创建客户
        int count5 = customerDao.createCustomer(customer);
        if(count5!=1){
            return flag = false;
        }


//===============================转换线索备注成为客户备注(删除线索备注，未写)================================================
        int count6 = 0;
        for(ClueRemark clueRemark:clueRemarks){

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());//id
            customerRemark.setNoteContent(clueRemark.getNoteContent());//备注
            customerRemark.setCreateBy(clueRemark.getCreateBy());//创建者
            customerRemark.setCreateTime(clueRemark.getCreateTime());//创建时间
            customerRemark.setEditFlag(clueRemark.getEditFlag());//修改标志
            customerRemark.setCustomerId(customerId);

            //创建线索
            int count7 =customerRemarkDao.relate(customerRemark);
            if(count7==1){
                count6++;
            }
        }
        if(count6!=clueRemarks.size()){
            return flag = false;
        }

//===============================创建交易，交易历史？============================================================
        if(t!=null){
            //t不是空，创建交易
            int count8 =tranDao.createTran(t);
            if(count8!=1){
                return flag = false;
            }
        }

//===============================删除线索=======================================================================






        flag=true;
        return flag;
    }
}
