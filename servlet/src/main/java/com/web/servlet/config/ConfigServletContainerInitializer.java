package com.web.servlet.config;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.Set;

public class ConfigServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        //注册Listener
        servletContext.addListener("com.web.servlet.config.listener.ConfigServletContextListener");
        servletContext.addListener("com.web.servlet.config.listener.ConfigServletContextListener2");
        //注册Filter
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        FilterRegistration.Dynamic confFilter = servletContext.addFilter("confFilter", "com.web.servlet.config.filter.ConfigFilter");
        confFilter.setInitParameter("initKey", "confFilter");
        confFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/servlet/filter");
        FilterRegistration.Dynamic confFilter2 = servletContext.addFilter("confFilter2", "com.web.servlet.config.filter.ConfigFilter2");
        confFilter2.setInitParameter("initKey", "confFilter2");
        confFilter2.addMappingForUrlPatterns(dispatcherTypes, true, "/servlet/filter");
        //注册Servlet
        ServletRegistration.Dynamic cServlet = servletContext.addServlet("cServlet", "com.web.servlet.config.servlet.CServlet");
        cServlet.setInitParameter("initKey", "cServlet");
        cServlet.setInitParameter("initTime", String.valueOf(System.currentTimeMillis())); //使用注解或web.xml文件都只能配置常量属性值
        cServlet.setLoadOnStartup(1);
        cServlet.addMapping("/servlet/cServlet");
        ServletRegistration.Dynamic cLazyServlet = servletContext.addServlet("cLazyServlet", "com.web.servlet.config.servlet.CLazyServlet");
        cLazyServlet.setInitParameter("initKey", "cLazyServlet");
        cLazyServlet.setInitParameter("initTime", String.valueOf(System.currentTimeMillis()));
        cLazyServlet.addMapping("/servlet/cLazyServlet");
    }
}
