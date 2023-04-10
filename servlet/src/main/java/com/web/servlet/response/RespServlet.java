package com.web.servlet.response;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

@WebServlet("/servlet/resp/*")
public class RespServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        String route = path.substring(path.indexOf("/resp") + 5);
        if (route.startsWith("/cache")) {
            cache(resp);
        }
        else if (route.startsWith("/refresh")) {
            refresh(resp);
        }
        else if (route.startsWith("/redirect")) {
            redirect(resp);
        }
        else if (route.startsWith("/download")) {
            download(resp);
        }
        else {
            writeByWriter(resp);
        }
    }

    /**
     * 使用字节输出流写入响应正文
     * @param resp：响应
     */
    private void writeByOutputStream(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.write("RespServlet:doGet".getBytes());
        outputStream.write("RespServlet:中文测试".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 使用字符输出流写入响应正文
     * @param resp：响应
     */
    private void writeByWriter(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.write("RespServlet:doGet");
        writer.write("RespServlet:中文测试");
    }

    /**
     * 读取html文件写入响应正文
     * @param resp：响应
     * @param path：文件路径
     */
    private void writeByFile(HttpServletResponse resp, String path) throws IOException {
        try (FileInputStream stream = new FileInputStream(path);
             InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8); //指定字符集避免中文乱码
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line=reader.readLine())!=null) {
                resp.getWriter().write(line);
            }
        }
    }

    /**
     * 测试HTTP缓存
     * 1. 强缓存：
     * 响应头设置Cache-Control: max-age=seconds
     * 注意：浏览器在加载url栏输入地址的根资源时，会默认配置Cache-Control: max-age=0避免对根资源文件使用强缓存
     * 根资源文件：响应"/resp/cache"返回/webapp目录下的cache.html内容，
     * 子请求：cache.html包含的"/resp/cache/sub"图片请求
     * 2. 协商缓存：
     * 响应头设置Last-Modified，第二次请求时会携带If-Modified-Since，通过比对时间判断是否使用缓存（304）
     * @param resp：响应
     */
    private void cache(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        writeByFile(resp, getServletContext().getRealPath("/cache.html"));
    }

    /**
     * 定时刷新，响应头设置Refresh
     * @param resp：响应
     */
    private void refresh(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setHeader("Refresh", "10"); //设置10s后刷新
//        resp.setHeader("Refresh", "10;URL=/servlet/index.jsp"); //设置10s后刷新并指定跳转
        resp.getWriter().write("访问时间："+ LocalTime.now());
    }

    /**
     * 重定向
     * @param resp：响应
     */
    private void redirect(HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/servlet/respCache");
    }

    /**
     * 下载文件
     * @param resp：响应
     */
    private void download(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.setHeader("Content-Disposition", "attachment;filename=cache.html");
        writeByFile(resp, getServletContext().getRealPath("/cache.html"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
