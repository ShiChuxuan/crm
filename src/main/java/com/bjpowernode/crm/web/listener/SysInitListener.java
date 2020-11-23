package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.util.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {
    /*
    *
    *   该方法是用来监听全局作用域对象的方法，当服务器启动，全局作用域对象就开始创建
    *   对象创建完毕后，马上执行该方法
    *
    *   sce:该参数能够取得监听的对象
    *       监听的是什么对象，就可以通过该参数取得什么对象
    *       例如我们现在监听的是全局作用域对象
    *
    * */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        //System.out.println("全局作用域对象创建了");
        ServletContext application = event.getServletContext();
        DicService service = (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
        *   可以打包成为一个map
        *   业务层应该是这样来保存数据的：
        *       map.put("appellationList",dvList1);
        *       map.put("clueStateList",dvList2);
        *       map.put("stageList",dvList3);
        *       ....
        *       ...
        *
        *
        *
        * */
        System.out.println("开始处理数据字典...");
        Map<String, List<DicValue>> map = service.getAll();
        //将map解析为全局作用域对象中保存的键值对
        Set<String>set = map.keySet();
        for(String key:set){
            List<DicValue>value = map.get(key);
            application.setAttribute(key,value);
        }
        System.out.println("处理数据字典结束！");

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //System.out.println("全局作用域对象被销毁了");
    }
}
