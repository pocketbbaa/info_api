package com.kg.platform.service.app.impl;


import com.alibaba.fastjson.JSON;
import com.kg.ad.dto.BaseResult;
import com.kg.ad.dto.SiteimageDto;
import com.kg.ad.dubboservice.AdverService;
import com.kg.ad.model.AdverRequest;
import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.model.out.SiteimageOutModel;
import com.kg.platform.model.request.SiteimageRequest;
import com.kg.platform.model.response.AdverListResponse;
import com.kg.platform.model.response.AdverResponse;
import com.kg.platform.model.response.AppStartPageResponse;
import com.kg.platform.model.response.PersonalAdvResponse;
import com.kg.platform.service.app.AppAdverService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Administrator on 2018/5/18.
 */
@Service
public class AppAdverServiceImpl implements AppAdverService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdverService  adverService;


    @Override
    //key规则  显示端_导航位置_显示位置_当前页
    @Cacheable(category = "adver/", key = "#{siteimageAppRequest.disPlayPort}_#{siteimageAppRequest.navigator_pos}_#{siteimageAppRequest.image_pos}_#{siteimageAppRequest.currentPage}_*_*_#{siteimageAppRequest.osVersion}", expire = 1, dateType = DateEnum.DAYS)
    public AppJsonEntity getStartPageAdv(SiteimageRequest siteimageAppRequest) {
        AdverRequest var1=new AdverRequest();
        var1.setNavigatorPos(siteimageAppRequest.getNavigator_pos());
        var1.setImagePos(siteimageAppRequest.getImage_pos());
        var1.setPageSize(siteimageAppRequest.getPageSize());
        var1.setCurrentPage(siteimageAppRequest.getCurrentPage());
        var1.setDisplayPort(siteimageAppRequest.getDisPlayPort());
        var1.setOsVersion(siteimageAppRequest.getOsVersion());
        BaseResult baseResult = adverService.getAdvers(var1);
        if(ExceptionEnum.SUCCESS.getCode().equals(baseResult.getCode())){
            List<SiteimageDto>  siteimageDtos= (List<SiteimageDto> )baseResult.getResponseBody();
            if(CollectionUtils.isNotEmpty(siteimageDtos)){
                SiteimageDto siteimageDto= siteimageDtos.get(0);
                AppStartPageResponse appStartPageResponse=new AppStartPageResponse();
                appStartPageResponse.setAdverOwner(siteimageDto.getAdverOwner());
                appStartPageResponse.setAdverTitle(siteimageDto.getAdverTitle());
                appStartPageResponse.setImageUrl(siteimageDto.getImageAddress());
                appStartPageResponse.setStartPageDisplaySecond(siteimageDto.getStartPageDisplaySecond());
                appStartPageResponse.setSkipType(siteimageDto.getSkipType()+"");
                appStartPageResponse.setSkipTo(siteimageDto.getImageDetail());
                appStartPageResponse.setAdverId(siteimageDto.getImageId()+"");
                appStartPageResponse.setCreateDate(siteimageDto.getCreateDate().getTime());
                appStartPageResponse.setAdverImgType(siteimageDto.getImageType());
                return  AppJsonEntity.makeSuccessJsonEntity(appStartPageResponse);
            }
        }
        return null;
    }

    @Override
    //key规则   显示端_导航位置_显示位置_当前页_文章id_作者id
    @Cacheable(category = "adver/", key = "#{siteimageAppRequest.disPlayPort}_#{siteimageAppRequest.navigator_pos}_#{siteimageAppRequest.image_pos}_#{siteimageAppRequest.currentPage}_#{siteimageAppRequest.userId}_#{siteimageAppRequest.articleId}_#{siteimageAppRequest.osVersion}", expire = 1, dateType = DateEnum.DAYS)
    public List<AdverResponse> getAdvs(SiteimageRequest siteimageAppRequest) {
        List<AdverResponse> advers = new ArrayList<AdverResponse>();
        AdverRequest var1=new AdverRequest();
        var1.setNavigatorPos(siteimageAppRequest.getNavigator_pos());
        var1.setImagePos(siteimageAppRequest.getImage_pos());
        var1.setUserId(siteimageAppRequest.getUserId());
        var1.setArticleId(siteimageAppRequest.getArticleId());
        var1.setCurrentPage(siteimageAppRequest.getCurrentPage());
        var1.setPageSize(siteimageAppRequest.getPageSize());
        var1.setDisplayPort(siteimageAppRequest.getDisPlayPort());
        var1.setOsVersion(siteimageAppRequest.getOsVersion());
        logger.info(">>>>>>>>>>>var1>>>>>>>>>>>>>>>>>"+JsonUtil.writeValueAsString(var1));
        BaseResult baseResult = adverService.getAdvers(var1);
        logger.info(">>>>>>>baseResult>>>>>>>>"+JsonUtil.writeValueAsString(baseResult));
        if(ExceptionEnum.SUCCESS.getCode().equals(baseResult.getCode())){
            List<SiteimageDto>  siteimageDtos= (List<SiteimageDto> )baseResult.getResponseBody();
            for(SiteimageDto siteimageDto:siteimageDtos){
                AdverResponse  siteimageResponse=new AdverResponse();
                siteimageResponse.setAdverId(siteimageDto.getImageId()+"");
                siteimageResponse.setAdverTitle(siteimageDto.getAdverTitle());
                siteimageResponse.setAdverOwner(siteimageDto.getAdverOwner());
                siteimageResponse.setImageUrl(siteimageDto.getImageAddress());
                siteimageResponse.setAdverImgType(siteimageDto.getAdverImgType());
                if(StringUtils.isNotEmpty(siteimageDto.getImageAddress())){
                    siteimageResponse.setImageUrls(siteimageDto.getImageAddress().split(","));
                }
                siteimageResponse.setSkipTo(siteimageDto.getImageDetail());
                siteimageResponse.setSkipType(siteimageDto.getSkipType());
                siteimageResponse.setCreateDate(siteimageDto.getCreateDate().getTime());
                siteimageResponse.setInmageType(siteimageDto.getSkipType()!=null&&siteimageDto.getSkipType()==5?5:siteimageDto.getImageType());
                advers.add(siteimageResponse);
            }
        }
        return advers;
    }

    @Override
    //key规则   显示端_导航位置_显示位置_文章id或者articleId
    @Cacheable(category = "adver/", key = "#{siteimageAppRequest.disPlayPort}_#{siteimageAppRequest.navigator_pos}_#{siteimageAppRequest.image_pos}_#{siteimageAppRequest.currentPage}_*_*_#{siteimageAppRequest.osVersion}", expire = 1, dateType = DateEnum.DAYS)
    public List<AdverListResponse> getRecommendAdvs(SiteimageRequest siteimageAppRequest) {
        List<AdverResponse> s = this.getAdvs(siteimageAppRequest);
        List<AdverListResponse> advers=new ArrayList<AdverListResponse>();
        int i=1;
        for(AdverResponse adverResponse :s){
            String position="";
            AdverListResponse adverListResponse=new AdverListResponse();
            AdverResponse ar1=new AdverResponse();
            ar1.setImageUrl(adverResponse.getImageUrl());
            if(StringUtils.isNotEmpty(adverResponse.getImageUrl())){
                ar1.setImageUrls(adverResponse.getImageUrl().split(","));
            }
            ar1.setAdverImgType(adverResponse.getAdverImgType());
            ar1.setAdverTitle(adverResponse.getAdverTitle());
            ar1.setAdverOwner(adverResponse.getAdverOwner());
            ar1.setSkipType(adverResponse.getSkipType());
            ar1.setSkipTo(adverResponse.getSkipTo());
            ar1.setAdverId(adverResponse.getAdverId());
            ar1.setCreateDate(adverResponse.getCreateDate());
            adverListResponse.setAdInfo(ar1);
            //插入位置
			if(i==1){
				position="5";
			}else if(i==2){
				position="10";
			}else if(i==3){
				position="15";
			}else if(i==4){
				position="20";
			}
          /*  if(siteimageAppRequest.getCurrentPage()==1){
                if(i==1){
                    position="5";
                }else{
                    position="12";
                }
            }else{
                if(i==1){
                    position="2";
                }else{
                    position="12";
                }
            }*/
            adverListResponse.setLocation(position);
            advers.add(adverListResponse);
            i++;
        }
        return  advers;
    }

    @Override
    //key规则   显示端_导航位置_显示位置_文章id或者articleId
    @Cacheable(category = "adver/", key = "#{siteimageAppRequest.disPlayPort}_#{siteimageAppRequest.navigator_pos}_#{siteimageAppRequest.image_pos}_#{siteimageAppRequest.currentPage}_*_*_#{siteimageAppRequest.osVersion}", expire = 1, dateType = DateEnum.DAYS)
    public List<PersonalAdvResponse> getPersonalAdv(SiteimageRequest siteimageAppRequest) {
        //插入个人中心广告
        logger.info("--------------获取广告链接-------------");
        List<AdverResponse> s = this.getAdvs(siteimageAppRequest);
        List<PersonalAdvResponse> prs =  new ArrayList<PersonalAdvResponse>();
        if(s.size()>0){
            for(AdverResponse ar:s){
                PersonalAdvResponse  personalAdvResponse=new PersonalAdvResponse();
                personalAdvResponse.setImageUrl(ar.getImageUrl());
                personalAdvResponse.setSkipTo(ar.getSkipTo());
                personalAdvResponse.setSkipType(ar.getSkipType());
                personalAdvResponse.setAdverId(ar.getAdverId());
                prs.add(personalAdvResponse);
            }
        }
        return  prs;
    }
}
