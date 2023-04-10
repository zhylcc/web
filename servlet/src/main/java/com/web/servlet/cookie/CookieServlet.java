package com.web.servlet.cookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

@WebServlet("/servlet/cookie/*")
public class CookieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        String route = path.substring(path.indexOf("/cookie") + 7);
        if (route.startsWith("/read")) {
            readCookie(req, resp);
        }
        else {
            writeCookie(resp);
        }
    }

    private void readCookie(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if ("cookieName".equals(cookie.getName())) {
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().write("获取Cookie："+cookie.getValue()+", "+ LocalTime.now());
                break;
            }
        }
    }

    private void writeCookie(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        Cookie cookie = new Cookie("cookieName", "value");
        cookie.setMaxAge(20);
        resp.addCookie(cookie);
        resp.getWriter().write("Cookie写入时间："+ LocalTime.now());
    }
}
