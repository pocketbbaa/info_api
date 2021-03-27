package com.kg.platform.service.admin.impl;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.dao.entity.AppVersionManage;
import com.kg.platform.dao.read.admin.AppVersionManageRMapper;
import com.kg.platform.dao.write.admin.AppVersionManageWMapper;
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.admin.AppVersionManageResponse;
import com.kg.platform.service.admin.AppVersionManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppVersionManageServiceImpl implements AppVersionManageService {

    @Autowired
    private AppVersionManageWMapper appVersionManageWMapper;

    @Autowired
    private AppVersionManageRMapper appVersionManageRMapper;

    @Override
    public boolean create(AppVersionManageRequest request) {
        Date now = new Date();
        request.setCreateTime(now);
        request.setUpdateTime(now);
        AppVersionManage appVersionManage = new AppVersionManage();
        BeanUtils.copyProperties(request, appVersionManage);
        Integer result = appVersionManageWMapper.create(appVersionManage);
        return result != null && result == 1;
    }

    @Override
    public boolean deleteById(AppVersionManageRequest request) {
        Long id = request.getId();
        if (id == null || id < 0) {
            return false;
        }
        Integer result = appVersionManageWMapper.deleteById(id);
        return result != null && result == 1;
    }

    @Override
    public AppVersionManageResponse getDetailById(AppVersionManageRequest request) {
        Long id = request.getId();
        if (id == null || id < 0) {
            return null;
        }
        AppVersionManage appVersionManage = appVersionManageRMapper.getById(id);
        AppVersionManageResponse response = new AppVersionManageResponse();
        BeanUtils.copyProperties(appVersionManage, response);
        return response;
    }

    @Override
    public PageModel<AppVersionManageResponse> getList(AppVersionManageRequest request, PageModel<AppVersionManageResponse> page) {
        int currentPage = page.getCurrentPage();
        int pageSize = page.getPageSize();
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 20 : pageSize;
        request.setStart((currentPage - 1) * pageSize);
        request.setLimit(pageSize);
        List<AppVersionManageResponse> appVersionManageRMapperList = appVersionManageRMapper.getList(request);
        Long total = appVersionManageRMapper.getTotalCount();
        page.setData(appVersionManageRMapperList);
        page.setTotalNumber(total);
        return page;
    }

    @Override
    public AppVersionManageResponse getByVersionAndSysType(AppVersionManageRequest request) {
        AppVersionManage appVersionManage = appVersionManageRMapper.getByVersionAndSysType(request.getVersionNum(), request.getSystemType(),request.getChannel());
        if (appVersionManage == null) {
            return null;
        }
        AppVersionManageResponse response = new AppVersionManageResponse();
        BeanUtils.copyProperties(appVersionManage, response);
        return response;
    }
}
