package com.ccc.dao;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver = null;
    private static String url = null;
    private static String user = null;
    private static String pwd = null;
    private static Connection conn = null;
    //初始化变量, 加载驱动
    static {
        Properties properties = new Properties();
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            pwd = properties.getProperty("pwd");
            //加载驱动
            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //获取连接connection
    public static Connection getConn(){
        try {
            conn = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    //通用查询方法, 返回结果集
    public static ResultSet excute(Connection connection, PreparedStatement pst1,ResultSet resultSet,String sql,Object[] params){
        try {
            pst1 = connection.prepareStatement(sql);
            if (params!=null){
                for (int i = 0; i < params.length; i++) {
                    pst1.setObject(i+1,params[i]);
                }
            }
            resultSet = pst1.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    //通用修改方法, 返回影响条数
    public static int excute(Connection connection, PreparedStatement pst1,String sql,Object[] params){
        int row = 0;
        try {
            pst1 = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pst1.setObject(i+1,params[i]);
            }
            row = pst1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }
    //关闭连接方法
    public static boolean closeAll(Connection connection, PreparedStatement pst,ResultSet rs){
        boolean flag = true;
        if (rs!=null){
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                flag =false;
                e.printStackTrace();
            }
        }
        if (pst!=null){
            try {
                pst.close();
                pst = null;
            } catch (SQLException e) {
                flag =false;
                e.printStackTrace();
            }
        }
        if (connection!=null){
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                flag =false;
                e.printStackTrace();
            }
        }
        return flag;
    }
}
