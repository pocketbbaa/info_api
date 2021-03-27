package com.kg.platform.service.admin;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.BuryingPointRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public interface BuryingPointService {
    /**
     * 根据埋点类型获取埋点数据
     * @return
     */
    PageModel buryingPointListWithType(BuryingPointRequest request,PageModel page);

    /**
     * 根据类型导出埋点数据EXCEL
     * @param request
     */
    void excelOfBuryingPointList(BuryingPointRequest request, HttpServletResponse response, HttpServletRequest servletRequest);

    /**
     * 生成EXCEL写入浏览器
     * @param listExcel
     * @param response
     * @param fileName
     * @param clazz
     * @throws IOException
     */
    void writeBrower(List listExcel, HttpServletResponse response, String fileName, Class clazz) throws IOException;
}
