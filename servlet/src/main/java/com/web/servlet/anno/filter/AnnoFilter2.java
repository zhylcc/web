package com.web.servlet.anno.filter;

import com.web.servlet.base.BaseFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;


@WebFilter(value = "/servlet/filter", filterName = "annoFilter2", initParams = {
        @WebInitParam(name = "initKey", value = "annoFilter2")
})
public class AnnoFilter2 extends BaseFilter {

}
