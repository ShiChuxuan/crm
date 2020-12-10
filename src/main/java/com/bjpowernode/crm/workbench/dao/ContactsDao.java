package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    public abstract int createContact(Contacts contact);

    public abstract List<Contacts> getAllContacts();

    public abstract List<Contacts> getContactsByName(String cname);
}
