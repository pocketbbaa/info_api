package com.kg.platform.service.admin.impl;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.kg.platform.model.in.FriendlyLinkInModel;
import com.kg.platform.model.request.FriendlyLinkRequest;
import com.kg.platform.model.response.FriendlyLinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.read.FriendlyLinkRMapper;
import com.kg.platform.dao.write.FriendlyLinkWMapper;
import com.kg.platform.model.out.FriendlyLinkOutModel;
import com.kg.platform.model.request.admin.FriendlyLinkEditRequest;
import com.kg.platform.service.admin.FriendlyLinkService;

@Service("AdminFriendlyLinkService")
public class FriendlyLinkServiceImpl implements FriendlyLinkService {

    @Autowired
    private FriendlyLinkWMapper friendlyLinkWMapper;

    @Autowired
    private FriendlyLinkRMapper friendlyLinkRMapper;

    @Override
    public PageModel<FriendlyLinkResponse> getLinkList(FriendlyLinkRequest request) {
		FriendlyLinkInModel inModel = new FriendlyLinkInModel();
		inModel.setStart((request.getCurrentPage() - 1) * request.getPageSize());
		inModel.setLimit(request.getPageSize());
		inModel.setType(request.getType());
        List<FriendlyLinkOutModel> data = friendlyLinkRMapper.selectByCondition(inModel);
        List<FriendlyLinkResponse> responses = Lists.newArrayList();

        if (null != data && data.size() > 0) {
            data.forEach(item -> {
            	FriendlyLinkResponse response = new FriendlyLinkResponse();
            	response.setLinkAddress(item.getLinkAddress());
            	response.setLinkIcon(item.getLinkIcon());
            	response.setLinkId(item.getLinkId());
            	response.setLinkName(item.getLinkName());
            	response.setLinkOrder(item.getLinkOrder());
            	response.setLinkStatus(item.getLinkStatus());
            	response.setUpdateUser(item.getCreateUserName());
            	response.setCreateDate(item.getCreateDate().getTime()+"");
            	responses.add(response);
            });
        }
        long count = friendlyLinkRMapper.selectCountByCondition();
        PageModel<FriendlyLinkResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setTotalNumber(count);
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(responses);
        return pageModel;
    }

    @Override
    public boolean deleteLink(FriendlyLinkRequest request) {
        return friendlyLinkWMapper.deleteByPrimaryKey(request.getLinkId()) > 0;
    }

    @Override
    public boolean setOrder(FriendlyLinkRequest request) {
        FriendlyLinkInModel link = new FriendlyLinkInModel();
        link.setLinkId(request.getLinkId());
        link.setLinkOrder(request.getLinkOrder());
        return friendlyLinkWMapper.updateByPrimaryKeySelective(link) > 0;
    }

    @Override
    public boolean batchSetStatus(FriendlyLinkRequest request) {
		request.getLinkIdList().forEach(id -> {
            FriendlyLinkInModel inModel= new FriendlyLinkInModel();
			inModel.setLinkId(id);
			inModel.setLinkStatus(request.getLinkStatus());
            friendlyLinkWMapper.updateByPrimaryKeySelective(inModel);
        });
        return true;
    }

    @Override
    public boolean setChannel(FriendlyLinkEditRequest request) {
        List<Integer> idList = StringUtils.convertString2IntList(request.getIds(), ",");
        idList.stream().forEach(id -> {
			FriendlyLinkInModel data = new FriendlyLinkInModel();
            data.setLinkId(id);
            data.setSecondChannel(request.getChannel());
            friendlyLinkWMapper.updateByPrimaryKeySelective(data);
        });
        return true;
    }

    @Override
    public boolean addOrUpdateLink(FriendlyLinkRequest request) {
        if (null == request.getLinkId()) {
            FriendlyLinkInModel keywords = new FriendlyLinkInModel();
            keywords.setLinkName(request.getLinkName());
            keywords.setLinkAddress(request.getLinkAddress());
            keywords.setSecondChannel(request.getSecondChannel());
            keywords.setLinkStatus(request.getLinkStatus());
            keywords.setLinkOrder(request.getLinkOrder());
            keywords.setCreateDate(new Date());
            keywords.setCreateUser(request.getCreateUser());
            keywords.setLinkIcon(request.getLinkIcon());
            keywords.setType(request.getType());
            return friendlyLinkWMapper.insertSelective(keywords) > 0;
        } else {
			FriendlyLinkInModel keywords = new FriendlyLinkInModel();
            keywords.setLinkId(request.getLinkId());
            keywords.setLinkName(request.getLinkName());
            keywords.setLinkAddress(request.getLinkAddress());
            keywords.setSecondChannel(request.getSecondChannel());
            keywords.setLinkStatus(request.getLinkStatus());
            keywords.setLinkOrder(request.getLinkOrder());
            keywords.setCreateDate(new Date());
            keywords.setCreateUser(request.getCreateUser());
            keywords.setLinkIcon(request.getLinkIcon());
            return friendlyLinkWMapper.updateByPrimaryKeySelective(keywords) > 0;
        }
    }
}
