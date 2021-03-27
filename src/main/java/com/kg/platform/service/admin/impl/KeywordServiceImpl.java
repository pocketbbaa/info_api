package com.kg.platform.service.admin.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.Keywords;
import com.kg.platform.dao.read.KeywordsRMapper;
import com.kg.platform.dao.read.admin.AdminKeywordRMapper;
import com.kg.platform.dao.write.KeywordsWMapper;
import com.kg.platform.model.out.KeywordsOutModel;
import com.kg.platform.model.request.admin.KeywordEditRequest;
import com.kg.platform.model.request.admin.KeywordQueryRequest;
import com.kg.platform.model.response.admin.KeywordQueryResponse;
import com.kg.platform.service.admin.KeywordService;

@Service("AdminKeywordService")
public class KeywordServiceImpl implements KeywordService {

    @Autowired
    private AdminKeywordRMapper adminKeywordRMapper;

    @Autowired
    private KeywordsWMapper keywordsWMapper;

    @Autowired
    private KeywordsRMapper keywordsRMapper;

    @Override
    public PageModel<KeywordQueryResponse> getKeywordList(KeywordQueryRequest request) {
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        request.setLimit(request.getPageSize());
        List<KeywordQueryResponse> data = adminKeywordRMapper.selectByCondition(request);
        if (null != data && data.size() > 0) {
            data.stream().forEach(item -> {
                if (item.getChannel() == 0) {
                    item.setChannelName("首页");
                }
                if (item.getStatus()) {
                    item.setStatusDisplay("启用");
                } else {
                    item.setStatusDisplay("禁用");
                }
            });
        }
        long count = adminKeywordRMapper.selectCountByCondition(request);
        PageModel<KeywordQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setTotalNumber(count);
        pageModel.setPageSize(request.getPageSize());
        pageModel.setData(data);
        return pageModel;
    }

    @Override
    public boolean deleteKeyword(KeywordEditRequest request) {
        return keywordsWMapper.deleteByPrimaryKey(request.getId()) > 0;
    }

    @Override
    public boolean setOrder(KeywordEditRequest request) {
        Keywords keywords = new Keywords();
        keywords.setKeywordId(request.getId());
        keywords.setKeywordOrder(request.getOrder());
        return keywordsWMapper.updateByPrimaryKeySelective(keywords) > 0;
    }

    @Override
    public boolean setStatus(KeywordEditRequest request) {
        List<Integer> idList = StringUtils.convertString2IntList(request.getIds(), ",");
        idList.stream().forEach(id -> {
            KeywordsOutModel keywords = keywordsRMapper.selectByPrimaryKey(id);
            Keywords data = new Keywords();
            data.setKeywordId(id);
            data.setKeywordStatus(!keywords.getKeywordStatus());
            keywordsWMapper.updateByPrimaryKeySelective(data);
        });
        return true;
    }

    @Override
    public boolean setChannel(KeywordEditRequest request) {
        List<Integer> idList = StringUtils.convertString2IntList(request.getIds(), ",");
        idList.stream().forEach(id -> {
            Keywords data = new Keywords();
            data.setKeywordId(id);
            data.setSecondChannel(request.getChannel());
            keywordsWMapper.updateByPrimaryKeySelective(data);
        });
        return true;
    }

    @Override
    public boolean addKeyword(KeywordEditRequest request) {
        if (null == request.getId()) {
            Keywords keywords = new Keywords();
            keywords.setKeywordDesc(request.getName());
            keywords.setKeywordLink(request.getLink());
            keywords.setSecondChannel(request.getChannel());
            keywords.setKeywordStatus(request.getStatus());
            keywords.setKeywordOrder(request.getOrder());
            keywords.setCreateDate(new Date());
            keywords.setCreateUser(request.getCreateUser());
            return keywordsWMapper.insertSelective(keywords) > 0;
        } else {
            Keywords keywords = new Keywords();
            keywords.setKeywordId(request.getId());
            keywords.setKeywordDesc(request.getName());
            keywords.setKeywordLink(request.getLink());
            keywords.setSecondChannel(request.getChannel());
            keywords.setKeywordStatus(request.getStatus());
            keywords.setKeywordOrder(request.getOrder());
            keywords.setUpdateDate(new Date());
            keywords.setUpdateUser(request.getCreateUser());
            return keywordsWMapper.updateByPrimaryKeySelective(keywords) > 0;
        }
    }
}
