package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import com.kg.ad.dto.BaseResult;
import com.kg.ad.dto.SiteimageDto;
import com.kg.ad.dubboservice.AdverService;
import com.kg.ad.model.AdverRequest;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.dao.entity.Siteimage;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.read.SiteimageRMapper;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.in.SiteimageInModel;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.SiteimageOutModel;
import com.kg.platform.model.request.SiteimageRequest;
import com.kg.platform.model.response.SiteimageResponse;
import com.kg.platform.service.SiteimageinService;

@Service
public class SiteimageinServiceImpl implements SiteimageinService {
    private static final Logger logger = LoggerFactory.getLogger(SiteimageinServiceImpl.class);

    @Resource(name = "siteimageRMapper")
    private SiteimageRMapper siteimageRMapper;

    @Inject
    ArticleRMapper articleRMapper;


    @Autowired
    AdverService adverService;


    @Override
    public Result<List<SiteimageResponse>> getAllColumn(SiteimageRequest request) {
//        logger.info("获取图片入参{}", JSON.toJSONString(request));
//        SiteimageInModel model = new SiteimageInModel();
//        model.setNavigator_pos(request.getNavigator_pos());
//        List<SiteimageOutModel> list = siteimageRMapper.selectAll(model);
//        List<SiteimageResponse> responselist = new ArrayList<SiteimageResponse>();
//        for (SiteimageOutModel siteimageOutModel : list) {
//            SiteimageResponse response = new SiteimageResponse();
//            response.setImagePos(siteimageOutModel.getImagePos());
//            response.setImageType(siteimageOutModel.getImageType());
//            response.setNavigatorPos(siteimageOutModel.getNavigatorPos());
//            response.setImageAddress(siteimageOutModel.getImageAddress());
//            response.setImageDetail(siteimageOutModel.getImageDetail());
//            response.setImageOrder(siteimageOutModel.getImageOrder());
//            if (siteimageOutModel.getImageType() == 1) {
//                ArticleInModel inModel = new ArticleInModel();
//                inModel.setArticleId(Long.parseLong(siteimageOutModel.getImageDetail()));
//                ArticleOutModel outModel = articleRMapper.getArticleContent(inModel);
//                if (null != outModel) {
//                    response.setArticleId(outModel.getArticleId());
//                    response.setArticleTitle(outModel.getArticleTitle());
//                    response.setCreateUser(outModel.getCreateUser().toString());
//                }
//
//            }
//            responselist.add(response);
//        }
        logger.info("获取图片入参{}", JSON.toJSONString(request));
        // SiteimageInModel model = new SiteimageInModel();
        // model.setNavigator_pos(request.getNavigator_pos());
        // List<SiteimageOutModel> list = siteimageRMapper.selectAll(model);
        AdverRequest adverRequest=new AdverRequest();
        adverRequest.setNavigatorPos(request.getNavigator_pos());
        adverRequest.setImagePos(request.getImage_pos());
        adverRequest.setDisplayPort(1);
        BaseResult baseResult = adverService.getAdvers(adverRequest);
        logger.info("web端广告------------"+JsonUtil.writeValueAsString(baseResult));
        List<SiteimageResponse> responselist = new ArrayList<SiteimageResponse>();
        if(ExceptionEnum.SUCCESS.getCode().equals(baseResult.getCode())){
            List<SiteimageDto>  siteimageDtos= (List<SiteimageDto> )baseResult.getResponseBody();
            System.out.println(">>>>siteimageDtos>>>>>>"+JsonUtil.writeValueAsString(siteimageDtos));
            for (SiteimageDto siteimageOutModel : siteimageDtos) {
                SiteimageResponse response = new SiteimageResponse();
                response.setImageId(siteimageOutModel.getImageId());
                response.setImagePos(siteimageOutModel.getImagePos());
                response.setImageType(siteimageOutModel.getImageType());
                response.setNavigatorPos(siteimageOutModel.getNavigatorPos());
                response.setImageAddress(siteimageOutModel.getImageAddress());
                response.setImageDetail(siteimageOutModel.getImageDetail());
                response.setImageOrder(siteimageOutModel.getImageOrder());
                if (siteimageOutModel.getImageType() == 1 || siteimageOutModel.getImageType() == 3) {
                    //跳转文章
                    if(siteimageOutModel.getSkipType()==2){
                        ArticleInModel inModel = new ArticleInModel();
                        inModel.setArticleId(Long.parseLong(siteimageOutModel.getImageDetail()));
                        ArticleOutModel outModel = articleRMapper.getArticleContent(inModel);
                        if (null != outModel) {
                            response.setArticleId(outModel.getArticleId());
                            response.setArticleTitle(outModel.getArticleTitle());
                            response.setCreateUser(outModel.getCreateUser().toString());
                            response.setArticleType(outModel.getPublishKind());
                        }
                    }
                }
                responselist.add(response);
            }
        }
        return new Result<List<SiteimageResponse>>(responselist);
    }

    @Override
    public Result<Siteimage> selectByPrimaryKey(Integer columnId) {
        return null;
    }

}
