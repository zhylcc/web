package com.web.servlet.request;

import com.web.servlet.entity.User;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@WebServlet("/servlet/req/*")
public class ReqServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String path = req.getRequestURI();
        String route = path.substring(path.indexOf("/req") + 4);
        if (route.startsWith("/pack")) {
            try {
//            System.out.println("反射封装："+packing(req.getParameterMap()));
                System.out.println("BeanUtils封装："+packingWithBeanUtils(req.getParameterMap()));
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else if (route.startsWith("/forward")) {
            forward(req, resp, "/reqNext");
        }
        else if (route.startsWith("/include")) {
            include(req, resp, "/reqNext");
        }
        else {
            getRequestParams(req);
        }
    }

    /**
     * 使用getParameterXxx方法获取请求参数
     * @param req：请求
     */
    private void getRequestParams(HttpServletRequest req) {
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            System.out.println(name + ": " + req.getParameter(name));
        }
    }

    /**
     * 将请求参数封装成实体类
     * @param paramMap：请求参数（Map）
     * @return 封装的实体类对象
     */
    private User packing(Map<String, String[]> paramMap)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        User user = new User();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String prop = entry.getKey();
            String[] value = entry.getValue();
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(prop, User.class);
            Method writeMethod = propertyDescriptor.getWriteMethod();
            Class<?> type = propertyDescriptor.getPropertyType();
            if (value.length > 1) {
                writeMethod.invoke(user, (Object) value);
            }
            else {
                if (type == Integer.TYPE || type == Integer.class) {
                    writeMethod.invoke(user, Integer.parseInt(value[0]));
                }
                else {
                    writeMethod.invoke(user, value[0]);
                }
            }
        }
        return user;
    }

    /**
     * 基于apache的common-beanutils将请求参数封装为实体类
     * @param paramMap：请求参数（Map）
     * @return 封装的实体类对象
     */
    private User packingWithBeanUtils(Map<String, String[]> paramMap)
            throws InvocationTargetException, IllegalAccessException {
        User user = new User();
        BeanUtils.populate(user, paramMap);
        return user;
    }

    /**
     * 请求转发（忽略当前响应正文）
     * @param req：原请求
     * @param resp：响应
     * @param toRouteKey：转发目的路由
     */
    private void forward(HttpServletRequest req, HttpServletResponse resp, String toRouteKey) throws ServletException, IOException {
        req.setAttribute("from", "ReqServlet.doGet:forward");
        RequestDispatcher dispatcher = req.getRequestDispatcher(toRouteKey);
        dispatcher.forward(req, resp);
        resp.getWriter().write("ReqServlet.doGet:forward");
    }

    /**
     * 请求包含（共同写入响应头和响应正文）
     * @param req：原请求
     * @param resp：被包含响应
     * @param toRouteKey：包含目的路由
     */
    private void include(HttpServletRequest req, HttpServletResponse resp, String toRouteKey) throws ServletException, IOException {
        req.setAttribute("from", "ReqServlet.doGet:include");
        RequestDispatcher dispatcher = req.getRequestDispatcher(toRouteKey);
        dispatcher.include(req, resp);
        resp.getWriter().write("ReqServlet.doGet:include");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        readByReader(req);
//        getRequestParams(req);
    }

    /**
     * 使用字节输入流读取请求正文（要求使用POST方式请求，GET方式请求体为空）
     * @param req：请求
     */
    private void readByInputStream(HttpServletRequest req) throws IOException {
        try (ServletInputStream inputStream = req.getInputStream()) {
            byte[] content = new byte[1024];
            int len;
            while ((len=inputStream.read(content)) > 0) {
                System.out.println(new String(content, 0, len));
            }
        }
    }

    /**
     * 使用字符输入流读取请求正文
     * @param req：请求
     */
    private void readByReader(HttpServletRequest req) throws IOException {
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line=reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
