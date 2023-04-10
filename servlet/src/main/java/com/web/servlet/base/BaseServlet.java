package com.web.servlet.base;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    private ServletConfig config;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        System.out.println(config.getServletName()+" initKey: "+config.getInitParameter("initKey"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(config.getServletName()+": "+req.getRequestURI());
    }

    @Override
    public void destroy() {
        System.out.println(config.getServletName()+" destroy.");
    }
}
