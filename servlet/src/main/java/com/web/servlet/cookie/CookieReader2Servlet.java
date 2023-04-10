package com.web.servlet.cookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

@WebServlet("/servlet2/cookie/read")
public class CookieReader2Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        //请求"/servlet2/cookie/read"时，客户端（浏览器）不会携带"/servlet/cookie"响应中的cookie，
        //因为该请求的Path（/servlet2）和cookie中的Path（/servlet）不匹配
        for (Cookie cookie : cookies) {
            if ("cookieName".equals(cookie.getName())) {
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().write("获取Cookie："+cookie.getValue()+", "+ LocalTime.now());
                break;
            }
        }
    }
}
