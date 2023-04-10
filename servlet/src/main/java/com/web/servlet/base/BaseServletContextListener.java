package com.web.servlet.base;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class BaseServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(this + " initializes servletContext");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(this + " destroys servletContext");
    }
}
