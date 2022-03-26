package com.ccc.dao.user;

import com.ccc.dao.BaseDao;
import com.ccc.pojo.Role;
import com.ccc.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //获取登录用户
    public User getLoginUser(Connection conn, String userCode);
    //修改密码
    public int updatePwd(Connection conn, String newPwd, int userID);
    //根据用户role或用户名获取用户总数
    public int getUserCount(Connection conn, String userName, int userRole,String userCode) throws SQLException;

    //通过用户输入的条件查询用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws  Exception;
    //通过用户输入的条件查询用户列表
    public List<Role> getRoleList(Connection connection, String roleName) throws SQLException;
    //删除用户
    public int delUser(Connection conn,  int userID);
    //获取用户
    public User getUser(Connection conn,  int userID) throws SQLException;
    public int addUser(Connection conn, User user);
    public int updateUser(Connection conn, User user);
}
