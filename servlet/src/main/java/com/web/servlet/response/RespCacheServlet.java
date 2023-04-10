package com.web.servlet.response;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

@WebServlet("/servlet/respCache")
public class RespCacheServlet extends HttpServlet {
    private long lastModified = -1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpg");
        resp.setHeader("Cache-Control", "max-age="+60); //强缓存
        lastModified = System.currentTimeMillis();
        resp.setDateHeader("Last-Modified", lastModified);
        writeValidatedCode(resp);
    }

    /**
     * 生成验证码
     * @param resp：响应
     */
    private void writeValidatedCode(HttpServletResponse resp) throws IOException {
        int width = 200, height = 35;
        //创建内存图像和画笔
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();//画笔
        //绘制边框
        g.setColor(Color.BLUE);
        g.drawRect(0, 0, width, height);
        g.setColor(Color.GRAY);
        g.fillRect(1, 1, width-2, height-2);
        //绘制随机数验证码
        Random r = new Random();
        g.setColor(Color.PINK);
        g.setFont(new Font("宋体", Font.BOLD,30));
        int right=0;
        for(int i=0;i<4;i++){
            g.drawString(String.valueOf(r.nextInt(10)), r.nextInt(35)+right, r.nextInt(10)+20);
            right += 35;
        }
        //绘制干扰线
        g.setColor(Color.WHITE);
        for(int i=0;i<10;i++){
            g.drawLine(r.nextInt(width), r.nextInt(height),r.nextInt(width), r.nextInt(height));
        }
        //将图片字节流写入响应体
        ImageIO.write(image, "jpg", resp.getOutputStream());
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return lastModified;
    }
}
