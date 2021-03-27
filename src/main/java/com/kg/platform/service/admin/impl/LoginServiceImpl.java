package com.kg.platform.service.admin.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.common.utils.Constants;
import com.kg.platform.common.utils.MD5Util;
import com.kg.platform.dao.entity.SysMenu;
import com.kg.platform.dao.entity.SysPost;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.read.SysPostRMapper;
import com.kg.platform.dao.read.SysUserRMapper;
import com.kg.platform.dao.read.admin.AdminSysMenuRMapper;
import com.kg.platform.model.request.admin.LoginRequest;
import com.kg.platform.model.request.admin.UserQueryRequest;
import com.kg.platform.model.response.admin.LoginResponse;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.admin.LoginService;
import com.kg.platform.tree.TreeUtils;

@Service("AdminLoginService")
public class LoginServiceImpl implements LoginService {

    @Inject
    private TokenManager tokenManager;

    @Autowired
    private SysUserRMapper sysUserRMapper;

    @Autowired
    private AdminSysMenuRMapper adminSysMenuRMapper;

    @Autowired
    private SysPostRMapper sysPostRMapper;

    @Override
    public LoginResponse login(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(MD5Util.md5Hex(password + Constants.SALT));
        List<SysUser> userList = sysUserRMapper.selectByUsernameAndPassword(request);
        if (null != userList && userList.size() > 0) {
            LoginResponse response = new LoginResponse();
            if (null != userList.get(0).getStatus() && userList.get(0).getStatus() == false) {
                response.setRealname("您的账号已经被禁用，请联系管理员！");
                return response;
            }
            // 查询岗位
            SysPost post = sysPostRMapper.selectByPrimaryKey(userList.get(0).getPostId());
            if (null != post && null != post.getStatus() && !post.getStatus()) {
                response.setRealname("您的岗位被禁用，请联系管理员！");
                return response;
            } else {
                response.setToken(tokenManager.createToken(userList.get(0).getSysUserId()));
                UserQueryRequest req = new UserQueryRequest();
                req.setUserId(String.valueOf(userList.get(0).getSysUserId()));
                response.setMenuList(getSysMenu(req));
                response.setRealname(userList.get(0).getUserRealname());
            }
            return response;
        } else {
            return null;
        }
    }

    @Override
    public void logOut(String userId) {
        tokenManager.deleteToken(Long.parseLong(userId));
    }

    @Override
    public List<SysMenu> getSysMenu(UserQueryRequest request) {
        List<SysMenu> menuList;
        if (Long.parseLong(request.getUserId()) == -999L) {
            menuList = adminSysMenuRMapper.selectAll();
        } else {
            menuList = adminSysMenuRMapper.selectByUserId(Long.parseLong(request.getUserId()));
        }
        menuList = TreeUtils.convert2Tree1(menuList, SysMenu.class, null);
        return menuList;
    }
}
