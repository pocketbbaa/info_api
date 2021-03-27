package com.kg.platform.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.in.PlainTextVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.utils.CheckUtils;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.dao.read.ColumnRMapper;
import com.kg.platform.model.in.ColumnInModel;
import com.kg.platform.model.out.ColumnOutModel;
import com.kg.platform.model.request.ColumnRequest;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.model.response.ColumnResponse;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.service.ColumnService;
import com.kg.platform.service.SiteinfoService;
import org.wltea.analyzer.filter.Filter;

@Service
public class ColumnServiceImpl implements ColumnService {
    private static final Logger logger = LoggerFactory.getLogger(ColumnServiceImpl.class);

    @Resource(name = "columnRMapper")
    private ColumnRMapper columnRMapper;

    @Inject
    private SiteinfoService siteinfoService;

    @Autowired
    protected JedisUtils jedisUtils;

    /**
     * 首页导航栏目
     */
    @Override
    public Result<List<ColumnResponse>> getAllColumn(ColumnRequest request) {
        logger.info("首页导航栏目前端入参：{}", JSON.toJSONString(request));
        ColumnInModel columninModel = new ColumnInModel();
        columninModel.setNavigatorDisplay(request.getNavigatorDisplay() + "");
        // if (request.getNavigatorDisplay() == 4) {
        // request.setColumnLevel("2");
        // } else {
        // request.setColumnLevel("1");
        // }
        columninModel.setColumnLevel(request.getColumnLevel());
        columninModel.setPrevColumn(request.getPrevColumn());
        // columninModel.setNavigatorDisplay(request.getNavigatorDisplay()+"");
        List<ColumnOutModel> list = columnRMapper.selectColumnAll(columninModel);
        List<ColumnOutModel> listresp = columnRMapper.getSecondColumn(columninModel);
        List<ColumnResponse> responselist = new ArrayList<ColumnResponse>();

        for (ColumnOutModel outModel : list) {
            ColumnResponse response = convertOutModelToResponse(outModel);

            ColumnInModel inModel = new ColumnInModel();
            inModel.setPrevColumn(response.getColumnId());

            List<ColumnResponse> responses = new ArrayList<ColumnResponse>();

            for (ColumnOutModel columnOutModel : listresp) {
                ColumnResponse respon = new ColumnResponse();
                if (columnOutModel.getPrevColumn().intValue() == outModel.getColumnId().intValue()) {
                    respon.setColumnId(columnOutModel.getColumnId());
                    respon.setPrevColumn(columnOutModel.getPrevColumn());
                    respon.setColumnName(columnOutModel.getColumnName());
                    respon.setDisplayMode(columnOutModel.getDisplayMode());
                    respon.setDisplayStatus(columnOutModel.getDisplayStatus());
                    respon.setNavigatorDisplay(columnOutModel.getNavigatorDisplay());
                    responses.add(respon);
                }
            }

            if (responses.size() == 0) {
                response.setList(null);
            } else {
                response.setList(responses);
            }

            responselist.add(response);

        }

        return new Result<List<ColumnResponse>>(responselist);
    }

    /**
     * 首页二级频道
     */
    @Override
    public Result<List<ColumnResponse>> getSecondaryAll() {
        List<ColumnOutModel> list = columnRMapper.getSecondaryAll();
        List<ColumnResponse> listresponse = new ArrayList<ColumnResponse>();
        for (ColumnOutModel outModel : list) {
            ColumnResponse response = new ColumnResponse();
            response.setColumnId(outModel.getColumnId());
            response.setPrevColumn(outModel.getPrevColumn());
            response.setColumnName(outModel.getColumnName());

            ColumnInModel inModel = new ColumnInModel();
            inModel.setColumnId(outModel.getColumnId());
            List<ColumnOutModel> outcolum = columnRMapper.getColumnarticleId(inModel);
            List<ArticleResponse> responses = new ArrayList<ArticleResponse>();
            for (ColumnOutModel columnOutModel : outcolum) {
                ArticleResponse aResponse = new ArticleResponse();
                aResponse.setArticleTitle(columnOutModel.getArticle_title());
                aResponse.setArticleImage(columnOutModel.getArticle_image());
                aResponse.setArticleId(columnOutModel.getArticle_id());

                responses.add(aResponse);

            }
            response.setArticleResponses(responses);
            listresponse.add(response);

        }
        return new Result<List<ColumnResponse>>(listresponse);
    }

    /**
     * 根据一级栏目查询二级栏目
     */
    @Override
    public Result<List<ColumnResponse>> getSecondColumn(ColumnRequest request) {
        logger.info("根据一级栏目查询二级栏目：{}", JSON.toJSONString(request));
        ColumnInModel columninModel = new ColumnInModel();
        columninModel.setColumnId(request.getColumnId());

        List<ColumnOutModel> list = columnRMapper.getSecondColumnByColumnId(columninModel);

        List<ColumnResponse> responselist = new ArrayList<ColumnResponse>();

        for (ColumnOutModel outModel : list) {
            ColumnResponse response = convertOutModelToResponse(outModel);

            responselist.add(response);

        }

        return new Result<List<ColumnResponse>>(responselist);
    }

    private ColumnResponse convertOutModelToResponse(ColumnOutModel outModel) {
        ColumnResponse response = new ColumnResponse();

        response.setColumnId(outModel.getColumnId());
        response.setPrevColumn(outModel.getPrevColumn());
        response.setColumnName(outModel.getColumnName());
        response.setNavigatorDisplay(outModel.getNavigatorDisplay());
        response.setDisplayStatus(outModel.getDisplayStatus());
        response.setColumnOrder(outModel.getColumnOrder());
        response.setDisplayMode(outModel.getDisplayMode());
        response.setSeoTitle(outModel.getSeoTitle());
        response.setSeoKeyword(outModel.getSeoKeyword());
        response.setSeoDescription(outModel.getSeoDescription());
        response.setCreateDate(outModel.getCreateDate());
        response.setCreateUser(outModel.getCreateUser());
        response.setUpdateDate(outModel.getUpdateDate());
        response.setUpdateUser(outModel.getUpdateUser());
        response.setColumnLevel(outModel.getColumnLevel());

        return response;
    }

    /**
     * 查栏SEO信息
     */
    @Override
    public Result<ColumnResponse> selectSeo(ColumnRequest request) {
        logger.info("查栏目SEO信息：{}", JSON.toJSONString(request));
        ColumnResponse response = new ColumnResponse();
        ColumnOutModel outModel = new ColumnOutModel();
        // request.getColumnId()==null 查询站点基本信息
        if (null == request.getColumnId()) {
            Result<SiteinfoResponse> siteinfoResponse = siteinfoService.selectSiteInfo();
            CheckUtils.checkRetInfo(siteinfoResponse, true);
            response.setSeoTitle(siteinfoResponse.getData().getSiteTitle());
            response.setSeoKeyword(siteinfoResponse.getData().getSiteKeyword());
            response.setSeoDescription(siteinfoResponse.getData().getSiteDesc());

        } else {
            ColumnInModel inModel = new ColumnInModel();
            inModel.setColumnId(request.getColumnId());
            outModel = columnRMapper.selectByname(inModel);
            if (outModel != null) {
                response.setColumnId(outModel.getColumnId());
                response.setSeoTitle(outModel.getSeoTitle());
                response.setSeoKeyword(outModel.getSeoKeyword());
                response.setSeoDescription(outModel.getSeoDescription());
            }
        }

        return new Result<ColumnResponse>(response);
    }

    @Override
    public JsonEntity checkSensitiveWord(PlainTextVO request) {
        org.wltea.analyzer.result.Result<List<String>> wordsResult = null;
        try {
            wordsResult = Filter.doFilter(request.getPlainTxt());
        } catch (IOException e) {
            new JsonEntity(ExceptionEnum.SYS_EXCEPTION);
        }
        if(wordsResult == null || wordsResult.getData().size() == 0){
            return new JsonEntity(ExceptionEnum.SUCCESS.getCode(),"没有敏感词");
        }else{
            return new JsonEntity(wordsResult.getCode(),wordsResult.getMessage(),wordsResult.getData());
        }
    }

}
