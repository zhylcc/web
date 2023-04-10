package com.web.servlet.base;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

public abstract class BaseFilter implements Filter {

    private FilterConfig cfg;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        cfg = filterConfig;
        System.out.println(cfg.getFilterName()+ "初始化，参数如下：");
        Enumeration<String> names = cfg.getInitParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            System.out.println(name + ": " + cfg.getInitParameter(name));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            System.out.println(cfg.getFilterName()+"拦截请求："+((HttpServletRequest) servletRequest).getRequestURI());
        }
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("返回"+cfg.getFilterName());
    }

    @Override
    public void destroy() {
        System.out.println(cfg.getFilterName()+"销毁");
    }
}
