package com.bjpowernode.workbench.test;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.Impl.ActivityServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/*
*
*   命名规范 类：模块名+Test
*          单元测试：test+方法名
*
* */
public class ActivityTest {
    @Test
    public void testSave(){
        String id = UUIDUtil.getUUID();
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.addActivity("a","a","a","a","a","a","a");
        Assert.assertEquals(flag,true);
        //断言，预测结果。如果上面代码执行没有问题，但是和断言额结果有出入还是会抛异常
    }

    @Test
    public void testUpdate(){

    }

    @Test
    public void testInsert(){

    }

    @Test
    public void test1(){
       SqlSession session  = SqlSessionUtil.getSqlSession();
        ClueRemarkDao dao = session.getMapper(ClueRemarkDao.class);
        String ids[] = {"48d329582c724c5b84fa24f2c6510db4","c028aa6317094acd8eed94ce9e97ade5"};
        int count = dao.getCountByCid(ids);
        System.out.println(count);
    }

    @Test
    public void test2(){
        SqlSession session  = SqlSessionUtil.getSqlSession();
        ClueRemarkDao dao = session.getMapper(ClueRemarkDao.class);
        String ids[] = {"48d329582c724c5b84fa24f2c6510db4","e8140b6621d245e78bc422ccda0fd49a"};
        int count = dao.deleteByCid(ids);
        session.commit();
        System.out.println(count);
    }

    @Test
    public void test3(){
        SqlSession session  = SqlSessionUtil.getSqlSession();
        ClueActivityRelationDao dao = session.getMapper(ClueActivityRelationDao.class);
        String ids[] = {"48d329582c724c5b84fa24f2c6510db4","c028aa6317094acd8eed94ce9e97ade5"};
        int count =dao.getCountByCid(ids);
        System.out.println(count);
    }

    @Test
    public void test4(){
        SqlSession session  = SqlSessionUtil.getSqlSession();
        ClueActivityRelationDao dao = session.getMapper(ClueActivityRelationDao.class);
        String ids[] = {"48d329582c724c5b84fa24f2c6510db4","c028aa6317094acd8eed94ce9e97ade5"};
        int count =dao.deleteByCid(ids);
        System.out.println(count);
    }

    @Test
    public void test5(){
        SqlSession session = SqlSessionUtil.getSqlSession();
        ClueDao clueDao = session.getMapper(ClueDao.class);
        ContactsDao contactsDao = session.getMapper(ContactsDao.class);

        String createTime = DateTimeUtil.getSysTime();
        String customerId = UUIDUtil.getUUID();
        String contactId  = UUIDUtil.getUUID();

        Clue clue  = clueDao.getClueById2("c028aa6317094acd8eed94ce9e97ade5");
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
        contact.setCreateBy("张三");
        contact.setCreateTime(createTime);
        contact.setDescription(description);
        contact.setContactSummary(contactSummary);
        contact.setNextContactTime(nextContactTime);
        contact.setAddress(address);
        int count = contactsDao.createContact(contact);
        session.commit();
        System.out.println(count);
    }

    @Test
    public void test6(){
        SqlSession session = SqlSessionUtil.getSqlSession();
        ClueActivityRelationDao clueActivityRelationDao = session.getMapper(ClueActivityRelationDao.class);
        ContactsActivityRelationDao contactsActivityRelationDao = session.getMapper(ContactsActivityRelationDao.class);
        List<String> aids = clueActivityRelationDao.getActivityIdByCid("c028aa6317094acd8eed94ce9e97ade5");

        String contactId = UUIDUtil.getUUID();
        //联系人市场活动关联关系表的id
        int count1 = 0;
        for(int i = 0;i < aids.size();i++){
            String id    = UUIDUtil.getUUID();
            String aid   = aids.get(i);
            int count = contactsActivityRelationDao.relate(id,contactId,aid);
            if(count==1){
                count1++;
            }
        }
        System.out.println(count1);

        session.commit();
    }

    @Test
    public void test7(){
        SqlSession session = SqlSessionUtil.getSqlSession();
        ClueRemarkDao clueRemarkDao = session.getMapper(ClueRemarkDao.class);
        ContactsRemarkDao contactsRemarkDao=  session.getMapper(ContactsRemarkDao.class);

        //获取线索的备注
        List<ClueRemark> clueRemarks = clueRemarkDao.getCluesByClueId("c028aa6317094acd8eed94ce9e97ade5");
        int count4 = 0;
        for(ClueRemark clueRemark:clueRemarks){
            ContactsRemark contactsRemark = new ContactsRemark();

            contactsRemark.setId(UUIDUtil.getUUID());//id
            contactsRemark.setNoteContent(clueRemark.getNoteContent());//备注
            contactsRemark.setCreateBy(clueRemark.getCreateBy());//创建者
            contactsRemark.setCreateTime(clueRemark.getCreateTime());//创建时间
            contactsRemark.setEditFlag(clueRemark.getEditFlag());//修改标志
            contactsRemark.setContactsId("26d354aee98c46c9b465344ce2d9414c");

            //创建线索
            int count3 = contactsRemarkDao.relateRemark(contactsRemark);
            if(count3==1){
                count4++;
            }
        }
        if(count4!=clueRemarks.size()){
            return;
        }
        session.commit();
        System.out.println(count4);
    }

    @Test
    public void test8(){
        SqlSession session = SqlSessionUtil.getSqlSession();
        ClueDao clueDao = session.getMapper(ClueDao.class);
        ContactsDao contactsDao = session.getMapper(ContactsDao.class);
        CustomerDao customerDao = session.getMapper(CustomerDao.class);

        String createTime = DateTimeUtil.getSysTime();
        String customerId = UUIDUtil.getUUID();
        String contactId  = UUIDUtil.getUUID();

        Clue clue  = clueDao.getClueById2("c028aa6317094acd8eed94ce9e97ade5");
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


        Customer customer = new Customer();
        customer.setId(UUIDUtil.getUUID());
        customer.setOwner(owner);
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy("张三");
        customer.setCreateTime(createTime);
        customer.setContactSummary(contactSummary);
        customer.setNextContactTime(nextContactTime);
        customer.setDescription(description);
        customer.setAddress(address);
        //创建客户
        int count5 = customerDao.createCustomer(customer);
        if(count5!=1){
            return;
        }
        session.commit();
        System.out.println(count5);
    }

    @Test
    public void test9(){
        SqlSession session = SqlSessionUtil.getSqlSession();
        ClueRemarkDao clueRemarkDao = session.getMapper(ClueRemarkDao.class);
        CustomerRemarkDao customerRemarkDao = session.getMapper(CustomerRemarkDao.class);

        //获取线索的备注
        List<ClueRemark> clueRemarks = clueRemarkDao.getCluesByClueId("c028aa6317094acd8eed94ce9e97ade5");
        int count6 = 0;
        for(ClueRemark clueRemark:clueRemarks){

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());//id
            customerRemark.setNoteContent(clueRemark.getNoteContent());//备注
            customerRemark.setCreateBy(clueRemark.getCreateBy());//创建者
            customerRemark.setCreateTime(clueRemark.getCreateTime());//创建时间
            customerRemark.setEditFlag(clueRemark.getEditFlag());//修改标志
            customerRemark.setCustomerId("58091511a9d743fdbbe262734e613f82");

            //创建线索
            int count7 =customerRemarkDao.relate(customerRemark);
            if(count7==1){
                count6++;
            }
        }
        if(count6!=clueRemarks.size()){
            return;
        }
        session.commit();
        System.out.println(count6);
    }

    @Test
    public void test10(){
        SqlSession session = SqlSessionUtil.getSqlSession();
        CustomerDao customerDao = session.getMapper(CustomerDao.class);
        Customer customer = customerDao.getCustomerByName("阿里巴巴");
        if(customer==null){

        }
    }

    @Test
    public void test11(){
        SqlSession  session = SqlSessionUtil.getSqlSession();
        DicTypeDao dicTypeDao = (DicTypeDao)session.getMapper(DicTypeDao.class);
        DicValueDao dicValueDao = (DicValueDao)session.getMapper(DicValueDao.class);
        Map<String, List<DicValue>>map = new HashMap<>();
        //获得所有类型的code
        List<DicType> typeList =dicTypeDao.getTypes();
        for(DicType dicType:typeList){
            String type  = dicType.getCode();
            List<DicValue> value = dicValueDao.getValuesByType(type);
            map.put(type,value);
        }
        //
        Set<String> set= map.keySet();
        for(String type:set){
            List<DicValue>dicValues = map.get(type);
            System.out.println("=============="+type+"==============");
            for(DicValue dicValue:dicValues){
                System.out.println("text:"+dicValue.getText());
                System.out.println("value:"+dicValue.getValue());
                System.out.println("++++++++++++++++++++++++++++++++++++");
            }

        }
    }

    @Test
    public void test12(){
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Map pMap = new HashMap();
        Set<String> set1 = bundle.keySet();
        for(String key:set1){
            String value=  bundle.getString(key);
            System.out.println(key+":"+value);
        }
    }

    @Test
    public void test13(){
        SqlSession session = SqlSessionUtil.getSqlSession();
        TranDao tranDao = session.getMapper(TranDao.class);
        int total = tranDao.getTotal();//总数
        List<Map<String,Object>> list = tranDao.getCharts();
        for(Map<String,Object> map:list){
            Set<String>set= map.keySet();
            for(String key:set){
                System.out.println(key+":"+map.get(key));
            }
        }
    }

}
