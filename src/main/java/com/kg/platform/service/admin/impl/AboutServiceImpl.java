package com.kg.platform.service.admin.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.dao.entity.BaseInfo;
import com.kg.platform.dao.read.BaseInfoRMapper;
import com.kg.platform.dao.read.admin.AdminBaseinfoRMapper;
import com.kg.platform.dao.write.BaseInfoWMapper;
import com.kg.platform.model.in.BaseInfoInModel;
import com.kg.platform.model.out.BaseInfoOutModel;
import com.kg.platform.model.response.admin.BaseinfoQueryResponse;
import com.kg.platform.service.admin.AboutService;

/**
 * 关于我们
 */
@Service("AdminAboutService")
public class AboutServiceImpl implements AboutService {

    @Autowired
    private AdminBaseinfoRMapper adminBaseinfoRMapper;

    @Autowired
    private BaseInfoRMapper baseInfoRMapper;

    @Autowired
    private BaseInfoWMapper baseInfoWMapper;

    @Override
    public List<BaseinfoQueryResponse> getBaseinfoList() {
        List<BaseinfoQueryResponse> list = adminBaseinfoRMapper.selectAll();
        if (null != list && list.size() > 0) {
            list.stream().forEach(item -> item
                    .setInfoStatusDisplay(null != item.getInfoStatus() && item.getInfoStatus() ? "显示" : "隐藏"));
        }
        return list;
    }

    @Override
    public boolean setInfoStatus(Integer id, Integer updateUser) {
        BaseInfoOutModel baseInfo = baseInfoRMapper.selectByPrimaryKey(id);
        if (null != baseInfo) {
            boolean infoStatus = baseInfo.getInfoStatus();
            BaseInfo baseInfo1 = new BaseInfo();
            baseInfo1.setInfoId(id);
            baseInfo1.setInfoStatus(!infoStatus);
            baseInfo1.setUpdateDate(new Date());
            baseInfo1.setUpdateUser(updateUser);
            return baseInfoWMapper.updateByPrimaryKeySelective(baseInfo1) > 0;
        }
        return false;
    }

    @Override
    public boolean deleteBaseinfo(Integer id) {
        return baseInfoWMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean addBaseinfo(BaseInfo baseInfo) {
        if (null != baseInfo.getInfoId()) {
            baseInfo.setUpdateDate(new Date());
            baseInfoWMapper.updateByPrimaryKeySelective(baseInfo);
            return true;
        } else {
            BaseInfoInModel model = new BaseInfoInModel();
            model.setInfoName(baseInfo.getInfoName());
            BaseInfoOutModel baseInfoOutModel = baseInfoRMapper.getbaseinfoName(model);
            if (null != baseInfoOutModel) {
                return false;
            }
            baseInfo.setCreateDate(new Date());
            baseInfoWMapper.insertSelective(baseInfo);
            return true;
        }
    }

    @Override
    public boolean setInfoOrder(Integer id, Integer infoOrder, Integer updateUser) {
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setInfoId(id);
        baseInfo.setInfoOrder(infoOrder);
        baseInfo.setUpdateDate(new Date());
        baseInfo.setUpdateUser(updateUser);
        return baseInfoWMapper.updateByPrimaryKeySelective(baseInfo) > 0;
    }
}
