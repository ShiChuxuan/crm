package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {

    public abstract List<Contacts> getAllContacts();

    public abstract List<Contacts> getContactsByName(String cname);
}
