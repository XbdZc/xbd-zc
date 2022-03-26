package com.ccc.servlet.user;

import com.ccc.util.Constant;
import com.sun.org.apache.bcel.internal.Const;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //登出, 1 清除session , 2重定向到登录
        if (req.getSession().getAttribute(Constant.USER_SESSION)!=null){
            req.getSession().setAttribute(Constant.USER_SESSION,null);
            resp.sendRedirect(req.getContextPath()+"/login.jsp");
        }
//        req.getRequestDispatcher("login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
