package com.web.servlet.anno.servlet;

import com.web.servlet.base.BaseServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/servlet/aLazyServlet", name = "aLazyServlet",
        initParams = {
            @WebInitParam(name = "initKey", value = "aLazyServlet")
        })
public class ALazyServlet extends BaseServlet {

}
