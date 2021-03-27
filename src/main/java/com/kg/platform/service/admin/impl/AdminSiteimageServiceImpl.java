package com.kg.platform.service.admin.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.Siteimage;
import com.kg.platform.dao.read.admin.AdminSiteimageRMapper;
import com.kg.platform.dao.write.SiteimageWMapper;
import com.kg.platform.model.request.admin.AdminSiteimageQueryRequest;
import com.kg.platform.model.request.admin.SiteimageEditRequest;
import com.kg.platform.service.admin.AdminSiteimageService;

@Service("AdminSiteimageService")
public class AdminSiteimageServiceImpl implements AdminSiteimageService {
    private static final Logger logger = LoggerFactory.getLogger(AdminSiteimageServiceImpl.class);

    @Resource(name = "adminSiteimageRMapper")
    private AdminSiteimageRMapper adminSiteimageRMapper;

    @Autowired
    private SiteimageWMapper siteimageWMapper;

    @Override
    public PageModel<Siteimage> listImage(AdminSiteimageQueryRequest request) {
        PageModel<Siteimage> pageQuery = new PageModel<>(request.getCurrentPage());
        pageQuery.setPageSize(request.getPageSize());

        long count = adminSiteimageRMapper.countImagesByParam(request);
        if (count == 0L) {
            return pageQuery;
        }
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        List<Siteimage> imageList = adminSiteimageRMapper.selectImagesByParam(request);

        pageQuery.setData(imageList);
        pageQuery.setTotalNumber(count);

        return pageQuery;
    }

    @Override
    public Siteimage getAdvertiseById(AdminSiteimageQueryRequest request) {

        logger.info("获取广告详情入参：" + JSON.toJSONString(request));
        Siteimage imageList = adminSiteimageRMapper.selectByPrimaryKey(request.getImageId());

        logger.info("获取广告详情返回：" + JSON.toJSONString(imageList));

        return imageList;
    }

    @Override
    public boolean addAdvertise(SiteimageEditRequest request) {
        Siteimage siteimage = new Siteimage();
        siteimage.setImageAddress(request.getImageAddress());
        siteimage.setImageType(request.getImageType());
        siteimage.setImageDetail(request.getImageDetail());
        siteimage.setNavigatorPos(request.getNavigatorPos());
        siteimage.setImagePos(request.getImagePos());
        siteimage.setImageStatus(request.getImageStatus());
        siteimage.setImageOrder(request.getImageOrder());

        siteimage.setDisplayPort(request.getDisplayPort());
        siteimage.setAdverStyle(request.getAdverStyle());
        siteimage.setAdverImageType(request.getAdverImageType());
        siteimage.setAdverTitle(request.getAdverTitle());
        siteimage.setAdverIntro(request.getAdverIntro());
        siteimage.setAdverLink(request.getAdverLink());
        siteimage.setAdverOwner(request.getAdverOwner());
        siteimage.setAdverTarget(request.getAdverTarget());
        siteimage.setSpreadTime(request.getSpreadTime());
        siteimage.setSpreadTimeS(request.getSpreadTimeS());
        siteimage.setSpreadTimeE(request.getSpreadTimeE());

        if (null == request.getImageId()) {
			siteimage.setCreateUser(request.getCreateUser());
			siteimage.setCreateDate(new Date());
            return siteimageWMapper.insertSelective(siteimage) > 0;
        } else {
            siteimage.setImageId(request.getImageId());
            siteimage.setUpdateUser(request.getCreateUser());
            siteimage.setUpdateDate(new Date());
            return siteimageWMapper.updateByPrimaryKeySelective(siteimage) > 0;
        }
    }

    @Override
    public boolean addImage(SiteimageEditRequest request) {
        Siteimage siteimage = new Siteimage();
        siteimage.setImageAddress(request.getImageAddress());
        siteimage.setImageType(request.getImageType());
        siteimage.setImageDetail(request.getImageDetail());
        siteimage.setNavigatorPos(request.getNavigatorPos());
        siteimage.setImagePos(request.getImagePos());
        siteimage.setImageStatus(request.getImageStatus());
        siteimage.setImageOrder(request.getImageOrder());

        if (null == request.getImageId()) {
			siteimage.setCreateUser(request.getCreateUser());
			siteimage.setCreateDate(new Date());
            return siteimageWMapper.insertSelective(siteimage) > 0;
        } else {
            siteimage.setImageId(request.getImageId());
			siteimage.setUpdateUser(request.getCreateUser());
            siteimage.setUpdateDate(new Date());
            return siteimageWMapper.updateByPrimaryKeySelective(siteimage) > 0;
        }
    }

    @Override
    public boolean deleteImage(SiteimageEditRequest request) {
        return siteimageWMapper.deleteByPrimaryKey(request.getImageId()) > 0;
    }

    @Override
    public boolean setStatus(SiteimageEditRequest request) {
        List<Long> idList = StringUtils.convertString2LongList(request.getIds(), ",");
        if (null != idList && idList.size() > 0) {
            idList.stream().forEach(id -> {
                Siteimage siteimage = new Siteimage();
                siteimage.setImageId(id);
                siteimage.setImageStatus(request.getImageStatus());
                siteimage.setUpdateUser(request.getUpdateUser());
                siteimage.setUpdateDate(new Date());
                siteimageWMapper.updateByPrimaryKeySelective(siteimage);
            });
        }
        return true;
    }
}
