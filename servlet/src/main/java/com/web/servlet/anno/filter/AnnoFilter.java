package com.web.servlet.anno.filter;

import com.web.servlet.base.BaseFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;


@WebFilter(value = "/servlet/filter", filterName = "annoFilter", initParams = {
    @WebInitParam(name = "initKey", value = "annoFilter")
})
public class AnnoFilter extends BaseFilter {

}
