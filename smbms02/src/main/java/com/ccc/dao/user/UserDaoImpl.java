package com.ccc.dao.user;

import com.ccc.dao.BaseDao;
import com.ccc.pojo.Role;
import com.ccc.pojo.User;
import com.ccc.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.xalan.internal.lib.ExsltDatetime;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public User getLoginUser(Connection conn, String userCode) {
        PreparedStatement pst =null;
        ResultSet rs = null;
        User user =null;
        if (conn!=null){
            String sql = "select * from smbms_user where userCode = ?";
            Object[] params = {userCode};
            ResultSet resultSet = BaseDao.excute(conn, pst, rs, sql, params);
            try {
                while (resultSet.next()){
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUserCode(resultSet.getString("userCode"));
                    user.setUserName(resultSet.getString("userName"));
                    user.setUserPassword(resultSet.getString("userPassword"));
                    user.setGender(resultSet.getInt("gender"));
                    user.setBirthday(resultSet.getTimestamp("birthday"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAddress(resultSet.getString("address"));
                    user.setUserRole(resultSet.getInt("userRole"));
                    user.setCreatedBy(resultSet.getInt("createdBy"));
                    user.setCreationDate(resultSet.getTimestamp("creationDate"));
                    user.setModifyBy(resultSet.getInt("modifyBy"));
                    user.setModifyDate(resultSet.getTimestamp("modifyDate"));
                }
            }catch (SQLException throwables) {
                System.out.println("UserDaoImpl-遍历resulset错误");
                throwables.printStackTrace();
            }finally {
                //不用关Conn, 连接可能设计业务, 到事物里面关
                BaseDao.closeAll(null,pst,resultSet);
            }
        }else {
            System.out.println("UserDaoImpl没获取到Connection");
        }
       return user;
    }

    @Override
    public int updatePwd(Connection conn, String newPwd, int userID) {
        PreparedStatement pst =null;
        int row = 0;
        if (conn!=null){
            String sql = "update smbms_user set userpassword = ? where id = ?";
            Object[] params = {newPwd,userID};
            row = BaseDao.excute(conn, pst,  sql, params);
            //不用关Conn, 连接可能设计业务, 到事物里面关
            BaseDao.closeAll(null,pst,null);
        }else {
            System.out.println("没获取到Connection");
        }
        return row;
    }
    @Override
    public int addUser(Connection conn, User user) {
        PreparedStatement pst =null;
        int row = 0;
        if (conn!=null){
            String sql = "INSERT INTO `smbms`.`smbms_user`( `userCode`, `userName`, `userPassword`, `gender`, `birthday`, `phone`, `address`, `userRole`, `createdBy`, `creationDate`, `modifyBy`, `modifyDate`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),user.getUserRole(),user.getCreatedBy(),user.getCreationDate(),user.getModifyBy(),user.getModifyDate()};
            row = BaseDao.excute(conn, pst,  sql, params);
            //不用关Conn, 连接可能设计业务, 到事物里面关
            BaseDao.closeAll(null,pst,null);
        }else {
            System.out.println("没获取到Connection");
        }
        return row;
    }
    @Override
    public int updateUser(Connection conn, User user) {
        PreparedStatement pst =null;
        int row = 0;
        if (conn!=null){
            String sql = "UPDATE `smbms_user` SET  `userName` = ?,  `gender` = ?, `birthday` = ?, `phone` = ?, `address` = ?,userRole = ?, `modifyBy` = ? , modifyDate = ? WHERE `id` = ? ";
            Object[] params = { user.getUserName(),user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),user.getModifyDate(),user.getId()};
            row = BaseDao.excute(conn, pst,  sql, params);
//            System.out.println("row============================"+row);
            //不用关Conn, 连接可能设计业务, 到事物里面关
            BaseDao.closeAll(null,pst,null);
        }else {
            System.out.println("没获取到Connection");
        }
        return row;
    }

    @Override
    public int getUserCount(Connection conn, String userName, int userRole,String userCode) throws SQLException {
        PreparedStatement pst =null;
        ResultSet rs = null;
        ArrayList<Object> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) FROM `smbms_user` as a INNER JOIN smbms_role AS r ON a.userRole = r.id");
        int count = 0;
        if (conn!=null){
            if (!StringUtils.isNullOrEmpty(userName) && userRole>0){
                sql.append(" where username like ? and userRole = ?");
                list.add("%"+userName+"%");
                list.add(userRole);
            }else if (userRole>0){
                sql.append(" where userRole = ?");
                list.add(userRole);
            }else if (!StringUtils.isNullOrEmpty(userName) ){
                sql.append(" where username like ?");
                list.add("%"+userName+"%");
            }else if (!StringUtils.isNullOrEmpty(userCode) ){
                sql.append(" where userCode = ?");
                list.add(userCode);
            }
            rs = BaseDao.excute(conn, pst,  rs,sql.toString(), list.toArray());
            while (rs.next()){
                count = rs.getInt("count(1)");
            }
            //不用关Conn, 连接可能设计业务, 到事物里面关
            BaseDao.closeAll(null,pst,rs);
        }else {
            System.out.println("没获取到Connection");
        }
//        System.out.println(sql);
        return count;
    }


    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        PreparedStatement pst =null;
        ResultSet rs = null;
        ArrayList<User> list = new ArrayList<>();
        ArrayList<Object> parms = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        User user   =null;
        sql.append("SELECT a.*,r.rolename FROM `smbms_user` as a INNER JOIN smbms_role AS r ON a.userRole = r.id ");
        if (!StringUtils.isNullOrEmpty(userName) && userRole>0){
            sql.append(" where username like ?  and userRole = ?  ");
            parms.add("%"+userName+"%");
            parms.add(userRole);
        }else if (!StringUtils.isNullOrEmpty(userName)){
            sql.append(" where username like ? ");
            parms.add("%"+userName+"%");
        }else if (userRole>0){
            sql.append(" where userRole = ? ");
            parms.add(userRole);
        }
        sql.append(" order by a.creationDate DESC LIMIT ?,? ");
        if (currentPageNo<=0){
            currentPageNo =1;
        }
        parms.add(pageSupport.getPageStart());
        parms.add(pageSupport.getPageSize());
//        System.out.println(sql.toString());
//        System.out.println(parms.toArray());
        if (connection!=null){
            rs = BaseDao.excute(connection, pst, rs, sql.toString(), parms.toArray());
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("a.id"));
                user.setUserCode(rs.getString("a.userCode"));
                user.setUserName(rs.getString("a.userName"));
                user.setUserPassword(rs.getString("a.userPassword"));
                user.setGender(rs.getInt("a.gender"));
                user.setBirthday(rs.getTimestamp("a.birthday"));
                user.setPhone(rs.getString("a.phone"));
                user.setAddress(rs.getString("a.address"));
                user.setUserRole(rs.getInt("a.userRole"));
                user.setCreatedBy(rs.getInt("a.createdBy"));
                user.setCreationDate(rs.getTimestamp("a.creationDate"));
                user.setModifyBy(rs.getInt("a.modifyBy"));
                user.setModifyDate(rs.getTimestamp("a.modifyDate"));
                user.setUserRoleName(rs.getString("r.rolename"));
                list.add(user);
            }
            BaseDao.closeAll(null,pst,rs);
        }
//        System.out.println(sql);
//        System.out.println(parms);
//        System.out.println(list.size());
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i).getUserName());
//        }
        return list;
    }

    @Override
    public List<Role> getRoleList(Connection connection, String roleName) throws SQLException {
        PreparedStatement pst =null;
        ResultSet rs = null;
        ArrayList<Role> list = new ArrayList<>();
        ArrayList<Object> parms = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        Role role   =null;
        sql.append("SELECT * FROM `smbms_role` ");
        if (!StringUtils.isNullOrEmpty(roleName)){
            sql.append(" where roleName like ? ");
            parms.add("%"+roleName+"%");
        }

//        System.out.println(sql.toString());
//        System.out.println(parms.toArray());
        if (connection!=null){
            rs = BaseDao.excute(connection, pst, rs, sql.toString(), parms.toArray());
            while (rs.next()) {
                role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                role.setCreatedBy(rs.getInt("createdBy"));
                role.setCreationDate(rs.getTimestamp("creationDate"));
                role.setModifyBy(rs.getInt("modifyBy"));
                role.setModifyDate(rs.getTimestamp("modifyDate"));
                list.add(role);
            }
            BaseDao.closeAll(null,pst,rs);
        }
//        System.out.println(sql);
//        System.out.println(parms);
//        System.out.println(list.size());
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i).getRoleName());
//        }
        return list;
    }

    @Override
    public int delUser(Connection conn, int userID) {
        PreparedStatement pst =null;
        int row = 0;
        if (conn!=null){
            String sql = "delete from smbms_user  where id = ?";
            Object[] params = {userID};
            row = BaseDao.excute(conn, pst,  sql, params);
            //不用关Conn, 连接可能设计业务, 到事物里面关
            BaseDao.closeAll(null,pst,null);
        }else {
            System.out.println("没获取到Connection");
        }
        return row;
    }

    @Override
    public User getUser(Connection conn, int userID) throws SQLException {
        User user = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select a.*,r.roleName as rname from smbms_user as a inner join smbms_role as r on a.userrole=r.id  where a.id = ?";
        pst = conn.prepareStatement(sql);
        Object[] params = {userID};
        rs = BaseDao.excute(conn,pst,rs,sql,params);
        while (rs.next()) {
            user = new User();
            user.setId(rs.getInt("a.id"));
            user.setUserCode(rs.getString("a.userCode"));
            user.setUserName(rs.getString("a.userName"));
            user.setUserPassword(rs.getString("a.userPassword"));
            user.setGender(rs.getInt("a.gender"));
            user.setBirthday(rs.getTimestamp("a.birthday"));
            user.setPhone(rs.getString("a.phone"));
            user.setAddress(rs.getString("a.address"));
            user.setUserRole(rs.getInt("a.userRole"));
            user.setCreatedBy(rs.getInt("a.createdBy"));
            user.setCreationDate(rs.getTimestamp("a.creationDate"));
            user.setModifyBy(rs.getInt("a.modifyBy"));
            user.setModifyDate(rs.getTimestamp("a.modifyDate"));
            user.setUserRoleName(rs.getString("rname"));
        }
        BaseDao.closeAll(null,pst,rs);
        return user;
    }

    @Test
    public void queryCount() throws Exception {
//        getUserList(BaseDao.getConn(),null,0,2,4);
//        getUserCount(BaseDao.getConn(),"张",0);
//        String  sql = "SELECT COUNT(1) FROM `smbms_user` as a INNER JOIN smbms_role AS r ON a.userRole = r.id";
//        sql+=" where username = ?";
//        System.out.println(sql);
//        getRoleList(BaseDao.getConn(),"管理");
//        String date = ExsltDatetime.date();
//        System.out.println(date);

    }

}
