package com.bjpowernode.crm.settings.domain;
/*
*  关于字符串中表现的日期及时间
*  我们在市场上有常用的有两种方式
*  日期：年月日
*       yyyy-MM-dd 10位字符串
*
*   日期+时间： 年月日时分秒 19字符串
*       yyyy-MM-dd HH:mm:ss
*
* */

/*
*   关于登录
*          验证账号和密码
*          User user = 执行sql语句 select * from tbl_user where loginAct = ? & loginPwd = ?
*
*           User 对象为null，说明账号密码错误
*
*           如果user对象不为null，说明账号密码正确
*
*           需要继续向下验证其他字段的信息
*
*           从user中get到
*
*           expireTime验证失效时间
*
*           lockState验证锁定状态
*
*           allowIps验证浏览器端的ip地址是否有效
*
*
*
*
* */
public class User {
    private String id ;         //编号 主键
    private String loginAct;    //登录账号
    private String name;        //用户的真实姓名
    private String loginPwd;    //登录密码
    private String email;       //邮箱
    private String expireTime;  //失效时间
    private String lockState;   //锁定状态 0：锁定 1：启用
    private String deptno;      //部门编号
    private String allowIps ;   //允许访问的ip地址
    private String createTime;  //
    private String createBy;    //
    private String editTime;    //
    private String editBy;      //

    public void setId(String id) {
        this.id = id;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public String getId() {
        return id;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public String getName() {
        return name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", loginAct='" + loginAct + '\'' +
                ", name='" + name + '\'' +
                ", loginPwd='" + loginPwd + '\'' +
                ", email='" + email + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", lockState='" + lockState + '\'' +
                ", deptno='" + deptno + '\'' +
                ", allowIps='" + allowIps + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", editTime='" + editTime + '\'' +
                ", editBy='" + editBy + '\'' +
                '}';
    }
}
