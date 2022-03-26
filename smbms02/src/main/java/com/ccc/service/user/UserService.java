package com.ccc.service.user;

import com.ccc.pojo.Role;
import com.ccc.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserService {
    public User userLogin(String userCode, String userPwd);
    public int updatePwd(String newPwd, int userID);
    public int getUserCount(String userName, int userRole,String userCode);
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);
    public List<Role> getRoleList(String roleName);
    public int addUser(User user);
    public int updateUser(User user);
    public User getUser(int userID);
    public int delUser(int userID);

}
