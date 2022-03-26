package com.ccc.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.ccc.pojo.Role;
import com.ccc.pojo.User;
import com.ccc.service.user.UserService;
import com.ccc.service.user.UserServiceImpl;
import com.ccc.util.Constant;
import com.ccc.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        System.out.println(method);
        if (method.equals("pwdmodify") && !StringUtils.isNullOrEmpty(method)) {
            //检查用户旧密码是否正确
            this.pwdModify(req, resp);
        } else if (method.equals("savepwd") && !StringUtils.isNullOrEmpty(method)) {
            //修改用户密码
            this.UpdatePwd(req, resp);
        } else if (method.equals("query") && !StringUtils.isNullOrEmpty(method)) {
            //查询用户userlist
            this.QueryUser(req, resp);
        } else if (method.equals("add") && !StringUtils.isNullOrEmpty(method)) {
            //添加用户
            this.addUser(req, resp);
        } else if (method.equals("getrolelist") && !StringUtils.isNullOrEmpty(method)) {
            //查询权限表字段
            this.getRoleList(req, resp);
        } else if (method.equals("ucexist") && !StringUtils.isNullOrEmpty(method)) {
            //查询用户code是否存在
            this.queryUcode(req, resp);
        }else if (method.equals("deluser") && !StringUtils.isNullOrEmpty(method)) {
            //查询用户code是否存在
            this.delUser(req, resp);
        }else if (method.equals("modify") && !StringUtils.isNullOrEmpty(method)) {
            //查询用户code是否存在
            this.getUser(req, resp,"usermodify.jsp");
        }else if (method.equals("view") && !StringUtils.isNullOrEmpty(method)) {
            //查询用户code是否存在
            this.getUser(req, resp,"userview.jsp");
        }else if (method.equals("modifyexe") && !StringUtils.isNullOrEmpty(method)) {
            //查询用户code是否存在
            this.updateUser(req, resp);
        }
    }
    //修改用户
    private void updateUser(HttpServletRequest req, HttpServletResponse resp) {
        String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        User user = null;
        try {
            if (!StringUtils.isNullOrEmpty(uid)){
                user = new User();
                user.setUserName(userName);
                user.setGender(Integer.valueOf(gender));
                user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
                user.setPhone(phone);
                user.setAddress(address);
                user.setUserRole(Integer.valueOf(userRole));
                user.setId(Integer.valueOf(uid));
                Object o = req.getSession().getAttribute(Constant.USER_SESSION);
                user.setModifyBy(((User)o).getId());
                user.setModifyDate(new Date());
                UserServiceImpl userService = new UserServiceImpl();
                int i = userService.updateUser(user);
                if (i==1){
                    //req.getRequestDispatcher("/jsp/user.do?method=view&uid="+uid).forward(req,resp);
                    resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=view&uid="+uid);
                }else {
                    req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
                }
            }else {
                req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询user
    private void getUser(HttpServletRequest req, HttpServletResponse resp,String url) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(uid)){
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.getUser(Integer.parseInt(uid));
                req.setAttribute("user",user);
                req.getRequestDispatcher(url).forward(req,resp);
        }
    }

    //删除用户一:  删除
    private void delUser(HttpServletRequest req, HttpServletResponse resp) {
        HashMap<String, String> map = new HashMap<>();
        String temuserid = req.getParameter("uid");

        int userid = 0;
        User user = (User) req.getSession().getAttribute(Constant.USER_SESSION);
        if (user.getUserRole()==1){
            if (!StringUtils.isNullOrEmpty(temuserid)){
                userid = Integer.parseInt(temuserid);
                if (userid!= user.getId() && userid>0){
                    UserServiceImpl service = new UserServiceImpl();
                    System.out.println("userid----------------"+userid);
                    int i = service.delUser(userid);
                    System.out.println("IIIII----------------"+i);
                    if (i == 1) {
                        map.put("delResult","true");
                    }else {
                        map.put("delResult","notexist");
                    }
                }else {
                    map.put("delResult","错误");
                }
            }else {
                map.put("delResult","false");
            }
        }else {
            map.put("delResult","norole");
        }
        try {
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            String jsonString = JSONArray.toJSONString(map);
            out.write(jsonString);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询用户code是否存在
    private void queryUcode(HttpServletRequest req, HttpServletResponse resp) {
        UserServiceImpl userService = new UserServiceImpl();
        String userCode = req.getParameter("userCode");
        HashMap<String, String> hashMap = new HashMap<>();
        int userCount = 0;
        if (!StringUtils.isNullOrEmpty(userCode)) {
            userCount = userService.getUserCount(null, 0, userCode);
            if (userCount ==1){
                hashMap.put("userCode", "exist");
            }else {
                hashMap.put("userCode", "ok");
            }
        }else {
            hashMap.put("userCode", "错误");
        }
//        System.out.println("queryUcode-----userCode>>>>"+userCode);
//        System.out.println("queryUcode-----userCount>>>>"+userCount);
        try {
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            String jsonString = JSONArray.toJSONString(hashMap);
            out.write(jsonString);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询权限表字段
    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) {
        UserServiceImpl userService = new UserServiceImpl();
        List<Role> roleList = userService.getRoleList(null);
        System.out.println("getRoleList-----roleList>>>>>" + roleList.size());
        try {
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            String jsonString = JSONArray.toJSONString(roleList);
            out.write(jsonString);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //t添加用户
    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取页面输入字段
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        //把页面输入添加到user对象
        User user = new User();
        if (!StringUtils.isNullOrEmpty(userCode)) {
            user.setUserCode(userCode);
        }
        if (!StringUtils.isNullOrEmpty(userName)) {
            user.setUserName(userName);
        }
        if (!StringUtils.isNullOrEmpty(userPassword)) {
            user.setUserPassword(userPassword);
        }
        if (!StringUtils.isNullOrEmpty(gender)) {
            user.setGender(Integer.parseInt(gender));
        }
        if (!StringUtils.isNullOrEmpty(phone)) {
            user.setPhone(phone);
        }
        if (!StringUtils.isNullOrEmpty(address)) {
            user.setAddress(address);
        }
        if (!StringUtils.isNullOrEmpty(userRole)) {
            user.setUserRole(Integer.parseInt(userRole));
        }
        if (!StringUtils.isNullOrEmpty(birthday)) {
            try {
                user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //创建对象从session获取当前用户ID
        Object o = req.getSession().getAttribute(Constant.USER_SESSION);
        if (o != null) {
            int createBy = ((User) o).getCreatedBy();
            user.setCreatedBy(createBy);
        }
        UserServiceImpl service = new UserServiceImpl();
        if (user != null) {
            //创建日期填当前日期
            user.setCreationDate(new Date());
            int i = service.addUser(user);
            if (i == 1) { // 成功就跳转到查询页面
                resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
            } else {// 失败重新到添加页面
                req.getRequestDispatcher("useradd.jsp").forward(req, resp);
            }
        } else {
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }
    }

    private void QueryUser(HttpServletRequest req, HttpServletResponse resp) {
        UserServiceImpl service = new UserServiceImpl();
        //查询用户名字
        String queryname = req.getParameter("queryname");
        //查询用户ID
        String tempRole = req.getParameter("queryUserRole");
        int queryUserRole = 0;
        if (!StringUtils.isNullOrEmpty(tempRole)) {
            //不为空的话使用页面获取的ID
            queryUserRole = Integer.parseInt(tempRole);
        }
//        System.out.println("queryUserRole---->>>>" + queryUserRole);
        //最大条数
        String totalCount = req.getParameter("totalCount");
        //第几页
        String tempCurrentPageNo = req.getParameter("currentPageNo");
        int currentPageNo = 0;
        //最大页数
//        String totalPageCount = req.getParameter("totalPageCount");
        //当前页数
        String tempPageIndex = req.getParameter("pageIndex");
        int pageIndex = 0;
        //用户总数量
        int userCount = service.getUserCount(queryname, queryUserRole, null);
//        System.out.println("userCount--->>" + userCount);
        //处理分页
        PageSupport pageSupport = new PageSupport();
        //设置每页大小
        pageSupport.setPageSize(Constant.PAGE_SIZE);
        //放入当前页数
        if (!StringUtils.isNullOrEmpty(tempPageIndex)) {
            pageIndex = Integer.parseInt(tempPageIndex);
        }
        pageSupport.setCurrentPageNo(pageIndex);
        //放入当前总数
        pageSupport.setTotalCount(userCount);
        if (!StringUtils.isNullOrEmpty(tempCurrentPageNo)) {
            currentPageNo = Integer.parseInt(tempCurrentPageNo);
        }
        pageSupport.setCurrentPageNo(currentPageNo);
        //从分页工具类中获取新当前页面CurrentPageNo
        int newPageno = pageSupport.getCurrentPageNo();
//        System.out.println("newPageno--->>" + newPageno);
        //从分页工具类中获取新页面大小PageSize
        int newPageSize = pageSupport.getPageSize();
//        System.out.println("newPageSize--->>" + newPageSize);

        //查询user
        List<User> userList = service.getUserList(queryname, queryUserRole, newPageno, newPageSize);
//        System.out.println("userList--->>" + userList.size());

        //查询role
        List<Role> roleList = service.getRoleList(null);
//        System.out.println("roleList--->>" + roleList.size());

        //回显页面输入框
        req.setAttribute("queryUserName", queryname);
        req.setAttribute("queryUserRole", queryUserRole);

        //返回list页面展示
        req.setAttribute("roleList", roleList);
        req.setAttribute("userList", userList);

        //从分页工具类中获取新最大行数  getTotalCount()
        int totalCount1 = pageSupport.getTotalCount();
//        System.out.println("totalCount1--->>" + totalCount1);

        //从分页工具类中获取新最大页数  getTotalPageCount
        int totalPageCount = pageSupport.getTotalPageCount();
//        System.out.println("totalPageCount--->>" + totalPageCount);

        //返回分页参数到页面
        req.setAttribute("totalCount", totalCount1); //总行数
        req.setAttribute("currentPageNo", newPageno); //当前页
        req.setAttribute("totalPageCount", totalPageCount); //总页数

        try {
            //跳转页面显示
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            System.out.println("userlist.jsp");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("userlist.jsp");
            e.printStackTrace();
        }
    }

    //修改密码方法
    private void UpdatePwd(HttpServletRequest req, HttpServletResponse resp) {
        String newpassword = req.getParameter("newpassword");
        String oldpassword = req.getParameter("oldpassword");
        User user = (User) req.getSession().getAttribute(Constant.USER_SESSION);
        if (user != null && !StringUtils.isNullOrEmpty(newpassword) && !StringUtils.isNullOrEmpty(oldpassword)) {
            //session不为空, 新密码和旧密码不为空进
            if (oldpassword.equals(user.getUserPassword())) {
                //输入的旧密码和用户旧密码一致进
                UserService service = new UserServiceImpl();
                int i = service.updatePwd(newpassword, user.getId());
                if (i == 1) {
                    //影响行数为1代表修改成功
                    req.setAttribute(Constant.SYS_MESSAGE, "密码修改成功!  3秒后跳转到登录界面");
                    req.getSession().setAttribute(Constant.USER_SESSION, null);
                    resp.setHeader("refresh", "3;url="+req.getContextPath()+"/login.jsp");
                } else {//修改失败
                    req.setAttribute(Constant.SYS_MESSAGE, "密码修改失败!  请重试试");
                }
            } else {////输入的旧密码和用户旧密码不一致
                req.setAttribute(Constant.SYS_MESSAGE, "旧密码不匹配");
            }
        } else {//session为空或 新密码或旧密码为空
            req.setAttribute(Constant.SYS_MESSAGE, "请重新登录在尝试");
        }
        try {
            //最后要跳转回修改密码界面, 不写回卡在user.do请求
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查看旧密码方法, 返回JSON
    private void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute(Constant.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<>();
        if (user == null) {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {
            resultMap.put("result", "error");
        } else if (!oldpassword.equals((user.getUserPassword()))) {
            resultMap.put("result", "false");
        } else {
            resultMap.put("result", "true");
        }
        try {
            PrintWriter out = resp.getWriter();
            String jsonString = JSONArray.toJSONString(resultMap);
            out.write(jsonString);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
