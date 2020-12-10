package com.bjpowernode.crm.workbench.service.Impl;

import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ContactsDao;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ContactsServiceImpl implements ContactsService {
    private SqlSession session = SqlSessionUtil.getSqlSession();
    private ContactsDao contactsDao = session.getMapper(ContactsDao.class);
    @Override
    public List<Contacts> getAllContacts() {
         List<Contacts> contacts= contactsDao.getAllContacts();
        return contacts;
    }

    @Override
    public List<Contacts> getContactsByName(String cname) {
        List<Contacts> contacts =contactsDao.getContactsByName(cname);
        return contacts;
    }
}
