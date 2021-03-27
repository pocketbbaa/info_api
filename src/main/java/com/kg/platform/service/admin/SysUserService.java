package com.kg.platform.service.admin;

import java.util.List;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.dao.entity.SysPost;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.request.admin.SysUserEditRequest;
import com.kg.platform.model.request.admin.SysUserQueryRequest;
import com.kg.platform.model.request.admin.SysUserUserRequest;
import com.kg.platform.model.response.admin.SysUserQueryResponse;
import com.kg.platform.model.response.admin.SysUserUserResponse;

public interface SysUserService {

    /**
     * 获得岗位列表
     * 
     * @return
     */
    List<SysPost> getPostList();

    /**
     * 分页获得系统账号列表
     * 
     * @param request
     * @return
     */
    PageModel<SysUserQueryResponse> getSysUserList(SysUserQueryRequest request);

    /**
     * 重置密码
     * 
     * @return
     */
    boolean resetPassword(SysUserQueryRequest request);

    /**
     * 改变状态0变1，1变0
     * 
     * @param request
     * @return
     */
    boolean setStatus(SysUserQueryRequest request);

    /**
     * 关联前台账号
     * 
     * @param request
     * @return
     */
    boolean setKgUser(SysUserQueryRequest request);

    boolean unsetKgUser(SysUserUserRequest request);

    List<SysUserUserResponse> getRelUser(SysUserUserRequest request);

    /**
     * 根据ID获得系统账户详情
     * 
     * @return
     */
    SysUser getSysUserById(SysUserQueryRequest request);

    /**
     * 添加/编辑账号
     * 
     * @param request
     * @return
     */
    boolean addSysUser(SysUserEditRequest request);

    /**
     * 查询后台系统用户列表
     * 
     * @param request
     * @return
     */
    List<SysUserUserResponse> getSysUsers();

}
