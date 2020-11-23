package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private SqlSession session = SqlSessionUtil.getSqlSession();
    private DicTypeDao dicTypeDao = session.getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = session.getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> result = new HashMap<>();

        //首先取得所有数据类型封装到dicTypes集合
        List<DicType> dicTypes = dicTypeDao.getTypes();

        //遍历dicTypes集合
        for(DicType type:dicTypes){

            //取得每一种类型的字典类型编码
            String code = type.getCode();

            //根据没一个字典类型来取得字典值列表
            List<DicValue> values = dicValueDao.getValuesByType(code);

            //封装进map
            result.put(code,values);
        }
        return result;
    }
}
