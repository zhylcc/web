package com.web.servlet.anno.servlet;

import com.web.servlet.base.BaseServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/servlet/aServlet", name = "aServlet", loadOnStartup = 1,
        initParams = {
            @WebInitParam(name = "initKey", value = "aServlet")
        })
public class AServlet extends BaseServlet {

}
