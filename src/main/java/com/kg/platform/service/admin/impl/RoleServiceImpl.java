package com.kg.platform.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kg.platform.dao.entity.Role;
import com.kg.platform.dao.entity.RoleProfile;
import com.kg.platform.dao.read.RoleProfileRMapper;
import com.kg.platform.dao.read.RoleRMapper;
import com.kg.platform.dao.read.admin.AdminRoleRMapper;
import com.kg.platform.dao.write.RoleProfileWMapper;
import com.kg.platform.dao.write.RoleWMapper;
import com.kg.platform.model.request.admin.RoleEditRequest;
import com.kg.platform.model.request.admin.RoleQueryRequest;
import com.kg.platform.model.response.admin.RoleQueryResponse;
import com.kg.platform.service.admin.RoleService;

@Service("AdminRoleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private AdminRoleRMapper adminRoleRMapper;

    @Autowired
    private RoleRMapper roleRMapper;

    @Autowired
    private RoleWMapper roleWMapper;

    @Autowired
    private RoleProfileRMapper roleProfileRMapper;

    @Autowired
    private RoleProfileWMapper roleProfileWMapper;

    @Override
    public List<RoleQueryResponse> getRoleList() {
        List<RoleQueryResponse> data = adminRoleRMapper.selectByCondition();
        if (null != data && data.size() > 0) {
            data.stream().forEach(item -> {
                if (item.getStatus()) {
                    item.setStatusDisplay("启用");
                } else {
                    item.setStatusDisplay("禁用");
                }
            });
        }
        return data;
    }

    @Override
    public boolean setStatus(RoleEditRequest request) {
        Role role = roleRMapper.selectByPrimaryKey(request.getId());
        Role r = new Role();
        r.setRoleId(request.getId());
        r.setRoleStatus(!role.getRoleStatus());
        return roleWMapper.updateByPrimaryKeySelective(r) > 0;
    }

    @Override
    public List<RoleProfile> getRoleProfile(RoleQueryRequest request) {
        return roleProfileRMapper.selectByRoleId(request.getId());
    }

    @Override
    public boolean setRoleProfile(RoleEditRequest request) {
        String json = request.getJson();
        List<RoleProfile> profileList = JSON.parseArray(json, RoleProfile.class);
        if (null != profileList && profileList.size() > 0) {
            profileList.stream().forEach(item -> {
                roleProfileWMapper.updateByPrimaryKeySelective(item);
            });
        }
        return true;
    }
}
