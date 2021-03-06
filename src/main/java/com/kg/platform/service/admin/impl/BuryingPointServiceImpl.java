package com.kg.platform.service.admin.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kg.platform.common.easyexcel.*;
import com.kg.platform.common.entity.*;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.model.request.BuryingPointRequest;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.admin.AccountService;
import com.kg.platform.service.admin.BuryingPointService;
import com.kg.search.dto.BaseResult;
import com.kg.search.dto.BuryingpointDTO;
import com.kg.search.dubboservice.BuryingpointDubboService;
import com.kg.search.enums.BuryingPointEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/20.
 */
@Service
public class BuryingPointServiceImpl implements BuryingPointService{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TokenManager tokenManager;

    public static Integer EXCELLIMIT = 50000;

    public static Integer REQUEST_SIZE= 80;

    @Autowired
    private BuryingpointDubboService buryingpointDubboService;
    @Autowired
    private AccountService accountService;

    @Override
    public PageModel buryingPointListWithType(BuryingPointRequest request,PageModel page) {

        BuryingPointEnum buryingPointEnum = BuryingPointEnum.getByCode(request.getDataType());

        List<Map<String,Object>> resultList = Lists.newArrayList();
        long count = 0;
        //??????????????????????????????
        Map<String,String> map = DateUtils.getFromAndTo(-1,Calendar.WEEK_OF_MONTH);
        //??????????????????????????????
        BaseResult baseResult = buryingpointDubboService.queryBuryingPointData(buryingPointEnum,map.get("result"),map.get("now"),page.getCurrentPage(),page.getPageSize());
        if(ExceptionEnum.SUCCESS.getCode().equals(baseResult.getCode())){
            if(baseResult.getResponseBody()!=null){
                BuryingpointDTO buryingpointDTO = (BuryingpointDTO) baseResult.getResponseBody();
                count = buryingpointDTO.getTotalHits();
                resultList =  buryingpointDTO.getResultList();
            }
        }
        page.setData(resultList);
        page.setTotalNumber(count);
        return page;
    }

    @Override
    public void excelOfBuryingPointList(BuryingPointRequest request,HttpServletResponse response, HttpServletRequest servletRequest) {

        logger.info("????????????EXCEL?????????????????????????????????????????????");
        long start = System.currentTimeMillis();
        accountService.checkTokenSign(request.getToken(),servletRequest);

        BuryingPointEnum buryingPointEnum = BuryingPointEnum.getByCode(request.getDataType());
        List<Map<String,Object>> resultList = Lists.newArrayList();
        long count;
        int pageSize = REQUEST_SIZE;
        long result1;
        long result2;
        long requestCount;
        int currentPage = 1;
        //??????????????????????????????
        Map<String,String> map = DateUtils.getFromAndTo(-1,Calendar.WEEK_OF_MONTH);
        //??????????????????????????????????????????
        BaseResult baseResult = buryingpointDubboService.queryBuryingPointData(buryingPointEnum,map.get("result"),map.get("now"),currentPage,pageSize);
        if(ExceptionEnum.SUCCESS.getCode().equals(baseResult.getCode())){
            BuryingpointDTO buryingpointDTO = (BuryingpointDTO) baseResult.getResponseBody();
            resultList.addAll(buryingpointDTO.getResultList());
            //?????????????????????????????????
            count = buryingpointDTO.getTotalHits();
            //????????????????????????????????????
            if (count>=EXCELLIMIT){
                result1 = EXCELLIMIT/pageSize;
                result2 = EXCELLIMIT%pageSize>0?1:0;
                requestCount = result1+result2;
                for(int i=2;i<=requestCount;i++){
                    baseResult =  buryingpointDubboService.queryBuryingPointData(buryingPointEnum,map.get("result"),map.get("now"),i,pageSize);
                    resultList = buildExcelList(baseResult,resultList);

                }
            }else {
                //????????????????????????????????????
                result1 = count/pageSize;
                result2 = count%pageSize>0?1:0;
                requestCount = result1+result2;
                for(int i=2;i<=requestCount;i++){
                    int inPageSize = pageSize;
                    if(i==requestCount){
                        inPageSize = (int)count%pageSize;
                    }
                    baseResult =  buryingpointDubboService.queryBuryingPointData(buryingPointEnum,map.get("result"),map.get("now"),i,inPageSize);
                    resultList = buildExcelList(baseResult,resultList);
                }
            }

            //??????EXCEL
            try {
                generateExcel(resultList,request.getDataType(),response);
            } catch (IOException e) {
                String requet = JSON.toJSONString(request);
                logger.error("????????????"+requet+"--------???????????????"+JSON.toJSONString(e.getStackTrace()));
                logger.error("????????????"+requet+"--------???????????????"+e.getMessage());
            }
            logger.info("??????EXCEL?????????????????????:????????????????????????????????????"+(System.currentTimeMillis()-start)+"MS");
        }

    }

    /**
     * ??????EXCELLIST??????
     * @param baseResult
     * @param resultList
     * @return
     */
    private List<Map<String,Object>> buildExcelList(BaseResult baseResult, List<Map<String, Object>> resultList){
        if(ExceptionEnum.SUCCESS.getCode().equals(baseResult.getCode())) {
            if(baseResult.getResponseBody()!=null){
                BuryingpointDTO buryingpointDTO = (BuryingpointDTO) baseResult.getResponseBody();
                if(buryingpointDTO.getResultList()!=null&&buryingpointDTO.getResultList().size()>0){
                    resultList.addAll(buryingpointDTO.getResultList());
                }
            }
        }
        return resultList;
    }

    /**
     *
     * @param resultList
     * @param dataType ???????????????1.???????????? 2.???????????? 3.???????????? 4.IOS?????? 5.???????????? 6.????????????
      */
    private void generateExcel(List<Map<String,Object>> resultList, int dataType, HttpServletResponse response) throws IOException {

        String fileName = BuryingPointEnum.getByCode(dataType).getMessage()+System.currentTimeMillis()+".csv";
        logger.info("?????????EXCEL????????????????????????????????????????????????"+fileName);
        switch (dataType){
            case (1):
                List<StartDataExcel> startDataExcels = parseResultList(resultList,StartDataExcel.class);
                startDataExcels = StartDataExcel.MeaningOfConversion(startDataExcels);
                writeBrower(startDataExcels,response,fileName,StartDataExcel.class);
                break;
            case (2):
                List<VideoDataExcel> videoDataExcels = parseResultList(resultList,VideoDataExcel.class);
                videoDataExcels = VideoDataExcel.MeaningOfConversion(videoDataExcels);
                writeBrower(videoDataExcels,response,fileName,VideoDataExcel.class);
                break;
            case (3):
                List<ArticleDataExcel> articleDataExcels = parseResultList(resultList,ArticleDataExcel.class);
                articleDataExcels = ArticleDataExcel.MeaningOfConversion(articleDataExcels);
                writeBrower(articleDataExcels,response,fileName,ArticleDataExcel.class);
                break;
            case (4):
                List<IosNoticeExcel> iosNoticeExcels = parseResultList(resultList,IosNoticeExcel.class);
                iosNoticeExcels = IosNoticeExcel.MeaningOfConversion(iosNoticeExcels);
                writeBrower(iosNoticeExcels,response,fileName,IosNoticeExcel.class);
                break;
            case (5):
                List<AndroidNoticeExcel> androidNoticeExcels = parseResultList(resultList,AndroidNoticeExcel.class);
                AndroidNoticeExcel.androidMeaningOfConversion(androidNoticeExcels);
                writeBrower(androidNoticeExcels,response,fileName,AndroidNoticeExcel.class);
                break;
            case (6):
                List<AdvDataExcel> advDataExcels = parseResultList(resultList,AdvDataExcel.class);
                AdvDataExcel.MeaningOfConversion(advDataExcels);
                writeBrower(advDataExcels,response,fileName,AdvDataExcel.class);
                break;

        }

    }

    public void writeBrower(List listExcel,HttpServletResponse response,String fileName, Class clazz) throws IOException {

        //??????EXCEl
        setResponseHeader(response,fileName);
        OutputStream out = null;
        out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
        //????????????sheet, sheet1  ????????????List<String> ?????????????????????
        Sheet sheet1 = new Sheet(1, 0,clazz);
        writer.write(listExcel, sheet1);
        writer.finish();
        out.flush();
        out.close();
    }
    /**
     * ???map???list?????????????????????????????????JAVA??????
     * @param resultList
     * @param javaType
     * @param <V>
     * @return
     */
    public <V> List<V> parseResultList(List<Map<String,Object>> resultList,Class<V> javaType){
        List<V> paseResultList = Lists.newArrayList();
        for (Map<String,Object> map:resultList) {
            paseResultList.add(JSON.parseObject(JSON.toJSONString(map), javaType));
        }
        return paseResultList;

    }

    //?????????????????????
    public void setResponseHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
            fileName = new String(fileName.getBytes(),"ISO8859-1");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
    }
}
