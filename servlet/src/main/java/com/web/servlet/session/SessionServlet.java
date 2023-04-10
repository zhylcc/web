package com.web.servlet.session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/servlet/session/*")
public class SessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        String route = path.substring(path.indexOf("/session") + 8);
        if (route.startsWith("/read")) {
            readSession(req, resp);
        }
        else {
            writeSession(req);
        }
    }

    private void readSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write("会话信息读取: "+req.getSession().getAttribute("path"));
    }

    private void writeSession(HttpServletRequest req) {
        //HttpSession内部使用ConcurrentHashMap存储属性
        req.getSession().setAttribute("path", req.getRequestURI());
    }
}
