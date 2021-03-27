package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.out.ColumnOutModel;
import com.kg.platform.model.request.admin.ColumnEditRequest;
import com.kg.platform.model.request.admin.ProfileVO;
import com.kg.platform.model.response.admin.TreeQueryResponse;

public interface ColumnService {

    List<TreeQueryResponse> getColumnList();

    boolean addColumn(ColumnEditRequest request,SysUser sysUser);

    JsonEntity deleteColumn(ColumnEditRequest request);

    ColumnOutModel getColumnById(ColumnEditRequest request);

    JsonEntity setProfileAttr(ProfileVO profileVO);

    JsonEntity getProfileAttr();

}
