package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.util.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import java.util.*;

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

        //------------------------------------------------------

        //数据字典处理完毕后，处理Stage2Possibility.properties文件
        /*
        *
        *   处理Stage2Possibility.properties文件步骤：
        *       解析该文件，将该属性文件中的键值对关系处理成为java中键值对关系（map）
        *
        *       Map<String(阶段stage),String(可能性possibility)>pMap = ...
        *       pMap.put("01资质审查",10)；
        *       pMap.put("02需求分析",25)；
        *       ...
        *       pMap.put("07...",...)
        *
        *       pMap保存值之后，就放在服务器缓存中
        *       application.setAttribute("pMap",pMap);
        *
        *
        *
        * */

        //解析properties文件
        //将配置文件放在src的根目录下，在maven中配置文件们都被整理成了一个resource文件夹，所以resource文件夹可以看做在根目录下
        //使用ResourceBundle.getBundle() 参数直接是需要读取的配置文件的名字，后面不需要加.properties。加了报错
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Map pMap = new HashMap();
        Set<String> set1 = bundle.keySet();
        for(String key:set1){
            String value=  bundle.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //System.out.println("全局作用域对象被销毁了");
    }
}
