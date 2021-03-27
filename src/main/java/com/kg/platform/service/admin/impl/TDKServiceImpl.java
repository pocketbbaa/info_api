package com.kg.platform.service.admin.impl;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.read.KgSeoTdkRMapper;
import com.kg.platform.dao.write.KgSeoTdkWMapper;
import com.kg.platform.model.in.KgSeoTdkInModel;
import com.kg.platform.model.out.KgSeoTdkOutModel;
import com.kg.platform.model.request.KgSeoTdkRequest;
import com.kg.platform.model.response.KgSeoTdkResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.dao.entity.Siteinfo;
import com.kg.platform.dao.write.SiteinfoWMapper;
import com.kg.platform.model.request.admin.TDKEditRequest;
import com.kg.platform.service.admin.TDKService;

import java.util.Date;

@Service("AdminTDKService")
public class TDKServiceImpl implements TDKService {

    @Autowired
    private SiteinfoWMapper siteinfoWMapper;

    @Autowired
    private KgSeoTdkRMapper kgSeoTdkRMapper;

    @Autowired
    private KgSeoTdkWMapper kgSeoTdkWMapper;

    /**
     * 编辑首页TDK
     * 
     * @param request
     * @return
     */
    @Override
    public boolean editHomeTDK(TDKEditRequest request) {
        Siteinfo siteinfo = new Siteinfo();
        siteinfo.setSiteId(1);
        siteinfo.setSiteTitle(request.getTitle());
        siteinfo.setSiteDesc(request.getDescription());
        siteinfo.setSiteKeyword(request.getKeywords());
        return siteinfoWMapper.updateByPrimaryKeySelective(siteinfo) > 0;
    }

	@Override
	public JsonEntity detailTdk(KgSeoTdkRequest request) {
		KgSeoTdkInModel inModel = new KgSeoTdkInModel();
		inModel.setTargetType(request.getTargetType());
		inModel.setTargetId(request.getTargetId());
    	KgSeoTdkOutModel outModel = kgSeoTdkRMapper.detailTdk(inModel);
		KgSeoTdkResponse response = new KgSeoTdkResponse();
		BeanUtils.copyProperties(outModel, response);
		return JsonEntity.makeSuccessJsonEntity(response);
	}

	@Override
	public JsonEntity editTdk(KgSeoTdkRequest request) {
    	KgSeoTdkInModel inModel = new KgSeoTdkInModel();
    	inModel.setTargetType(request.getTargetType());
    	inModel.setTargetId(request.getTargetId());
		KgSeoTdkOutModel outModel = kgSeoTdkRMapper.detailTdk(inModel);
    	inModel.setSeoTitle(request.getSeoTitle());
    	inModel.setSeoDesc(request.getSeoDesc());
    	inModel.setSeoKeywords(request.getSeoKeywords());
    	inModel.setUpdateDate(new Date());

		if(outModel==null){
			kgSeoTdkWMapper.insertSelective(inModel);
		}else {
			kgSeoTdkWMapper.editTdk(inModel);
		}
		return JsonEntity.makeSuccessJsonEntity();
	}
}
