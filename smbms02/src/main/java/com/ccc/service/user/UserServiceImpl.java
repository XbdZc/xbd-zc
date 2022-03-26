package com.ccc.service.user;

import com.ccc.dao.BaseDao;
import com.ccc.dao.user.UserDao;
import com.ccc.dao.user.UserDaoImpl;
import com.ccc.pojo.Role;
import com.ccc.pojo.User;
import com.ccc.util.PageSupport;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements UserService{
    private static Connection conn = null;
    UserDao dao = null;

    public UserServiceImpl() {
        dao = new UserDaoImpl();
    }

    @Override
    public User userLogin(String userCode, String userPwd) {
        User user = null;
        try {
            conn = BaseDao.getConn();
            user = dao.getLoginUser(conn, userCode);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        //验证密码
        if (user!=null &&userPwd.equals(user.getUserPassword())){
            return user;
        }
        return null;
    }

    @Override
    public int updatePwd(String newPwd, int userID) {
        int row = 0;
        try {
            conn = BaseDao.getConn();
            row = dao.updatePwd(conn,newPwd,userID);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        return row;
    }

    @Override
    public int getUserCount(String userName, int userRole,String userCode) {
        int count = 0;
        try {
            conn = BaseDao.getConn();
            count = dao.getUserCount(conn,userName,userRole,userCode);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        System.out.println("getUserCount--->"+count);
        return count;
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        List<User> list = null;
        try {
            conn = BaseDao.getConn();
            list = dao.getUserList(conn,userName,userRole,currentPageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        return list;
    }

    @Override
    public List<Role> getRoleList(String roleName) {
        List<Role> list = null;
        try {
            conn = BaseDao.getConn();
            list = dao.getRoleList(conn,roleName);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        return list;
    }

    @Override
    public int addUser(User user) {
        int count = 0;
        try {
            conn = BaseDao.getConn();
            count = dao.addUser(conn,user);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        return count;
    }
    @Override
    public int updateUser(User user) {
        int count = 0;
        try {
            conn = BaseDao.getConn();
            count = dao.updateUser(conn,user);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        return count;
    }
    @Override
    public int delUser(int userID) {
        int row = 0;
        try {
            conn = BaseDao.getConn();
            row = dao.delUser(conn,userID);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        return row;
    }

    @Override
    public User getUser(int userID) {
        User user = null;
        try {
            conn = BaseDao.getConn();
            user = dao.getUser(conn,userID);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeAll(conn,null,null);
        }
        return user;
    }

    @Test
    public void test1(){
//        updatePwd("123456",1);
//        System.out.println(getUserCount("张", 0));
//        List<User> userList = getUserList(null, 0, -1, -4);
//        List<Role> userList = getRoleList(null);
//        System.out.println(userList.get(0).getRoleName());
//        PageSupport pageSupport = new PageSupport();
//        pageSupport.setCurrentPageNo(3);
//        pageSupport.setPageSize(4);
//        System.out.println(pageSupport.getCurrentPageNo());
//        System.out.println(pageSupport.getPageSize());
//        System.out.println(pageSupport.getPageStart());
//        User user = new User();
//        user.setUserName("zs");
//        user.setUserPassword("123321");
//        user.setUserRole(1);
//        user.setModifyDate(new Date());
//        user.setModifyBy(1);
//        user.setCreationDate(new Date());
//        user.setCreatedBy(1);
//        user.setAddress("桂桂");
//        user.setPhone("13322332233");
//        user.setBirthday(new Date());
//        user.setGender(1);
//        user.setUserCode("zhangsan");
//        int i = addUser(user);
//        System.out.println(i);
//        System.out.println(getUser(1).getUserName());
    }
}
