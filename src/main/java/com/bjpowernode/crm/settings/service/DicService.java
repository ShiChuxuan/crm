package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public interface DicService {

    public abstract  Map<String, List<DicValue>> getAll();
}
