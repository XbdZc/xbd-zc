package com.ccc.servlet.user;

import com.ccc.pojo.User;
import com.ccc.service.user.UserService;
import com.ccc.service.user.UserServiceImpl;
import com.ccc.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        UserService service = new UserServiceImpl();
        User user = service.userLogin(userCode, userPassword);
        if (user!=null){
            req.getSession().setAttribute(Constant.USER_SESSION,user);
            resp.sendRedirect(req.getContextPath()+"/jsp/frame.jsp");
        }else{
            req.setAttribute("error","用户名或密码错误, 重登!");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
