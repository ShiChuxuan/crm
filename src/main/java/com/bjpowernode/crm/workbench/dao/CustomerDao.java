package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    public abstract int createCustomer(Customer customer);

    public abstract Customer getCustomerByName(String company);

    public abstract List<String> getCustomerName(String name);

    public abstract String getCustomerIdByName(String customerName);
}
