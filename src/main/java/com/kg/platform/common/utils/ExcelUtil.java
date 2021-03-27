package com.kg.platform.common.utils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ExcelUtil {


    public static void writeBrower(List listExcel, HttpServletResponse response, String fileName, Class clazz) throws IOException {

        //生成EXCEl
        setResponseHeader(response, fileName);
        OutputStream out = null;
        out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 0, clazz);
        writer.write(listExcel, sheet1);
        writer.finish();
        out.flush();
        out.close();
    }

    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        fileName = new String(fileName.getBytes(), "ISO8859-1");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
    }

}
