package com.web.servlet.config.servlet;

import com.web.servlet.base.BaseServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.text.SimpleDateFormat;

public class CServlet extends BaseServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        long now = System.currentTimeMillis();
        String initTimeStr = config.getInitParameter("initTime");
        long initTime = (initTimeStr==null) ? now : Long.parseLong(initTimeStr);
        System.out.println(config.getServletName() + " loads at: " + new SimpleDateFormat("HH:mm:ss").format(initTime));
        System.out.println(config.getServletName() + " inits at: " + new SimpleDateFormat("HH:mm:ss").format(now));
    }
}
