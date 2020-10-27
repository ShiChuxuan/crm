import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.MD5Util;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.TransactionInvocationHandler;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Proxy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Test {

    @org.junit.Test
    public void Test02() throws ParseException {
        String expireTime = "2019-10-10 10:10:10";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date   =sdf.parse(expireTime); //字符串转换成日期
        expireTime = sdf.format(date);      //日期再转换成字符串
        String  nowTime = sdf.format(new Date());

        long expireTime1 = Long.valueOf(expireTime);
        long nowTime1 = Long.valueOf(nowTime);

        System.out.println(expireTime1);
        System.out.println(nowTime1);

        System.out.println(expireTime1-nowTime1);

    }

    @org.junit.Test
    public void Test03(){
        String expireTime = "2008-10-10 10:10:10";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CurrentTime = sdf.format(date);
        System.out.println(expireTime);
        System.out.println(CurrentTime);

        System.out.println(expireTime.compareTo(CurrentTime));

    }

    @org.junit.Test
    public void Test04(){
        String expireTime = "2019-10-10 10:10:10";      //有效时间
        String currentTime = DateTimeUtil.getSysTime(); //系统时间
        if(expireTime.compareTo(currentTime)>=0){       //根据数据字典比较
            System.out.println("账户在有效期内");
        }else{
            System.out.println("账户已过期");
        }



        //判断是否锁定
        String lockState = "0";
        if("0".equals(lockState)){
            System.out.println("账户已经锁定");
        }else if("1".equals(lockState)){
            System.out.println("允许登录");
        }

        //判断ip地址是否合法
        String allowIps = "192.168.1.1,192.168.1.2,127.0.0.1"; //允许访问的ip
        String ip = "192.168.1.1";//浏览器的ip
        if(allowIps.contains(ip)){
            System.out.println("ip合法");
        }else{
            System.out.println("此ip不允许登录");
        }

        //密码加密
        String pwd = "";
        String MD5PWD =  MD5Util.getMD5(pwd);
        System.out.println("MD5:"+MD5PWD);

    }
}
