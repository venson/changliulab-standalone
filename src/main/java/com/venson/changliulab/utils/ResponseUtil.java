package com.venson.changliulab.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

public abstract class ResponseUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    public  static <T> void out(HttpServletResponse response, Result<T> r) {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers","*");
        try {
            mapper.writeValue(response.getWriter(), r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void outFileByStream(HttpServletResponse response,String fileName, InputStream is) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-disposition", "attachment;filename=\""+fileName+"\"");

        //跨域处理
//        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers","*");
        response.setHeader("Access-Control-Max-Age","false");
        response.setHeader("Access-Control-Allow-Credentials","10");
        ServletOutputStream outputStream=response.getOutputStream();
        //将文件流逐一放入输出流中打印给前端
        byte[] bs1=new byte[1024];
        int length;
        while((length=is.read(bs1))!=-1){
            outputStream.write(bs1,0,length);
        }
        //关流操作
        outputStream.close();
        is.close();
    }
}
