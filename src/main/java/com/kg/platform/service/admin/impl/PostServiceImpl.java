package com.kg.platform.service.admin.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.SysPost;
import com.kg.platform.dao.entity.SysPostAuth;
import com.kg.platform.dao.read.admin.AdminPostRMapper;
import com.kg.platform.dao.read.admin.AdminSysUserRMapper;
import com.kg.platform.dao.read.admin.AdminTreeRMapper;
import com.kg.platform.dao.write.SysPostAuthWMapper;
import com.kg.platform.dao.write.SysPostWMapper;
import com.kg.platform.model.request.admin.AdminPostEditRequest;
import com.kg.platform.model.request.admin.SysUserQueryRequest;
import com.kg.platform.model.response.admin.PostQueryResponse;
import com.kg.platform.model.response.admin.SysUserQueryResponse;
import com.kg.platform.model.response.admin.TreeQueryResponse;
import com.kg.platform.service.admin.PostService;
import com.kg.platform.tree.TreeUtils;

/**
 * 岗位管理
 */
@Service("AdminPostService")
public class PostServiceImpl implements PostService {

    @Autowired
    private AdminTreeRMapper adminTreeRMapper;

    @Autowired
    private SysPostWMapper sysPostWMapper;

    @Autowired
    private SysPostAuthWMapper sysPostAuthWMapper;

    @Autowired
    private AdminPostRMapper adminPostRMapper;

    @Autowired
    private AdminSysUserRMapper adminSysUserRMapper;

    @Autowired
    private JedisUtils jedisUtils;

    @Override
    public List<TreeQueryResponse> getAuthTree() {
        List<TreeQueryResponse> data = adminTreeRMapper.selectAuth();
        return TreeUtils.convert2Tree(data, TreeQueryResponse.class, null);
    }

    @Override
    @Transactional
    public boolean addPost(AdminPostEditRequest request) {
        if (null == request.getPostId()) {
            SysPost post = new SysPost();
            post.setCreateDate(new Date());
            post.setPostName(request.getName());
            post.setCreateUser(request.getUserId());
            sysPostWMapper.insertSelective(post);
            // 关联权限
            List<Integer> idList = StringUtils.convertString2IntList(request.getAuthIds(), ",");
            idList.stream().forEach(id -> {
                SysPostAuth auth = new SysPostAuth();
                auth.setAuthId(id);
                auth.setPostId(post.getPostId());
                auth.setCreateDate(new Date());
                auth.setCreateUser(request.getUserId());
                sysPostAuthWMapper.insertSelective(auth);
            });
        } else {
            // 更新
            SysPost post = new SysPost();
            post.setUpdateUser(request.getUserId());
            post.setUpdateDate(new Date());
            post.setPostName(request.getName());
            post.setPostId(request.getPostId());
            sysPostWMapper.updateByPrimaryKeySelective(post);
            // 删除权限
            sysPostAuthWMapper.deleteByPostId(request.getPostId());
            // 关联权限
            List<Integer> idList = StringUtils.convertString2IntList(request.getAuthIds(), ",");
            idList.stream().forEach(id -> {
                SysPostAuth auth = new SysPostAuth();
                auth.setAuthId(id);
                auth.setPostId(request.getPostId());
                auth.setCreateDate(new Date());
                auth.setCreateUser(request.getUserId());
                sysPostAuthWMapper.insertSelective(auth);
            });
            // 编辑岗位清除登录状态
            SysUserQueryRequest req = new SysUserQueryRequest();
            req.setLimit(999);
            req.setStart(0);
            req.setPostId(request.getPostId());
            List<SysUserQueryResponse> list = adminSysUserRMapper.selectByCondition(req);
            if (null != list && list.size() > 0) {
                list.stream().forEach(item -> {
                    jedisUtils.del(JedisKey.userTokenKey(Long.parseLong(item.getId())));
                });
            }
        }
        return true;
    }

    @Override
    public List<PostQueryResponse> getPostList() {
        List<PostQueryResponse> list = adminPostRMapper.selectAll();
        if (null != list && list.size() > 0) {
            list.stream().forEach(item -> {
                if (item.getStatus()) {
                    item.setStatusDisplay("启用");
                } else {
                    item.setStatusDisplay("禁用");
                }
            });
        }
        return list;
    }

    @Override
    public boolean setStatus(AdminPostEditRequest request) {
        SysPost post = new SysPost();
        post.setUpdateDate(new Date());
        post.setUpdateUser(request.getUserId());
        post.setPostId(request.getPostId());
        post.setStatus(request.getStatus());

        // 禁用岗位清除登录状态
        if (null != request.getStatus() && request.getStatus().booleanValue() == false) {
            SysUserQueryRequest req = new SysUserQueryRequest();
            req.setLimit(999);
            req.setStart(0);
            req.setPostId(request.getPostId());
            List<SysUserQueryResponse> list = adminSysUserRMapper.selectByCondition(req);
            if (null != list && list.size() > 0) {
                list.stream().forEach(item -> {
                    jedisUtils.del(JedisKey.userTokenKey(Long.parseLong(item.getId())));
                });
            }
        }

        return sysPostWMapper.updateByPrimaryKeySelective(post) > 0;
    }
}
