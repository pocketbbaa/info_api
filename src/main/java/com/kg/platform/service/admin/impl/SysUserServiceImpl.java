package com.kg.platform.service.admin.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.MD5Util;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.SysPost;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.entity.SysUserUser;
import com.kg.platform.dao.read.SysPostRMapper;
import com.kg.platform.dao.read.SysUserRMapper;
import com.kg.platform.dao.read.admin.AdminSysUserRMapper;
import com.kg.platform.dao.read.admin.SysUserUserRMapper;
import com.kg.platform.dao.write.SysUserWMapper;
import com.kg.platform.dao.write.admin.SysUserUserWMapper;
import com.kg.platform.model.request.admin.LoginRequest;
import com.kg.platform.model.request.admin.SysUserEditRequest;
import com.kg.platform.model.request.admin.SysUserQueryRequest;
import com.kg.platform.model.request.admin.SysUserUserRequest;
import com.kg.platform.model.response.admin.SysUserQueryResponse;
import com.kg.platform.model.response.admin.SysUserUserResponse;
import com.kg.platform.service.admin.SysUserService;

@Service("AdminSysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysPostRMapper sysPostRMapper;

    @Autowired
    private AdminSysUserRMapper adminSysUserRMapper;

    @Autowired
    private SysUserUserRMapper sysUserUserRMapper;

    @Autowired
    private SysUserUserWMapper sysUserUserWMapper;

    @Autowired
    private SysUserWMapper sysUserWMapper;

    @Autowired
    private SysUserRMapper sysUserRMapper;

    @Override
    public List<SysPost> getPostList() {
        return sysPostRMapper.selectAll();
    }

    @Override
    public PageModel<SysUserQueryResponse> getSysUserList(SysUserQueryRequest request) {
        request.setLimit(request.getPageSize());
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        List<SysUserQueryResponse> list = adminSysUserRMapper.selectByCondition(request);
        if (null != list && list.size() > 0) {
            list.stream().forEach(item -> {
                if (item.getStatus() == 0) {
                    item.setStatusDisplay("禁用");
                } else {
                    item.setStatusDisplay("启用");
                }
            });
        }
        long count = adminSysUserRMapper.selectCountByCondition(request);
        PageModel<SysUserQueryResponse> pageModel = new PageModel<>();
        pageModel.setCurrentPage(request.getCurrentPage());
        pageModel.setPageSize(request.getPageSize());
        pageModel.setTotalNumber(count);
        pageModel.setData(list);
        return pageModel;
    }

    @Override
    public boolean resetPassword(SysUserQueryRequest request) {
        SysUser user = new SysUser();
        user.setSysUserId(request.getUserId());
        user.setSysUserPassword(MD5Util.md5Hex(request.getPassword() + Constants.SALT));
        return sysUserWMapper.updateByPrimaryKeySelective(user) > 0;
    }

    @Override
    public boolean setStatus(SysUserQueryRequest request) {
        SysUser user = new SysUser();
        user.setSysUserId(request.getUserId());
        user.setStatus(request.getStatus());
        return sysUserWMapper.updateByPrimaryKeySelective(user) > 0;
    }

    @Override
    public boolean setKgUser(SysUserQueryRequest request) {

        SysUserUserRequest req = new SysUserUserRequest();
        req.setKgUserId(request.getKgUserId());
        req.setSysUserId(request.getUserId().intValue());
        SysUserUser sysUser = sysUserUserRMapper.selectByUserAndKgUser(req);
        if (null != sysUser) {
            return false;
        }

        SysUserUser user = new SysUserUser();
        user.setSysUserId(request.getUserId());
        user.setKgUserId(Long.parseLong(request.getKgUserId()));
        sysUserUserWMapper.insertSelective(user);
        return true;
    }

    @Override
    public boolean unsetKgUser(SysUserUserRequest request) {
        sysUserUserWMapper.deleteByPrimaryKey(request.getRelId());
        return true;
    }

    @Override
    public List<SysUserUserResponse> getRelUser(SysUserUserRequest request) {
        return sysUserUserRMapper.selectBySysUser(request.getSysUserId());
    }

    @Override
    public SysUser getSysUserById(SysUserQueryRequest request) {
        return sysUserRMapper.selectByPrimaryKey(request.getUserId());
    }

    @Override
    public boolean addSysUser(SysUserEditRequest request) {
        LoginRequest req = new LoginRequest();
        req.setUsername(request.getUsername());
        if (null != request.getUserId()) {
            req.setUserId(String.valueOf(request.getUserId()));
        }
        List<SysUser> userList = sysUserRMapper.selectByUsername(req);
        if (null != userList && userList.size() > 0) {
            return false;
        }

        SysUser user = new SysUser();
        user.setSysUserName(request.getUsername());
        user.setUserMobile(request.getMobile());
        user.setUserRealname(request.getRealname());
        if (!StringUtils.isBlank(request.getPassword())) {
            user.setSysUserPassword(MD5Util.md5Hex(request.getPassword() + Constants.SALT));
        }
        user.setPostId(request.getPostId());
        if (null != request.getUserId()) {
            user.setSysUserId(request.getUserId());
            return sysUserWMapper.updateByPrimaryKeySelective(user) > 0;
        }
        return sysUserWMapper.insertSelective(user) > 0;
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.md5Hex("123" + Constants.SALT));
    }

    @Override
    public List<SysUserUserResponse> getSysUsers() {
        return adminSysUserRMapper.selectSysUsers();
    }

}
