package com.kg.platform.service.admin.impl;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.NumberUtils;
import com.kg.platform.dao.entity.Siteinfo;
import com.kg.platform.dao.entity.UserComment;
import com.kg.platform.dao.read.KgInfoSwitchRMapper;
import com.kg.platform.dao.read.UserCommentRMapper;
import com.kg.platform.dao.read.admin.AdminCommentRMapper;
import com.kg.platform.dao.write.SiteinfoWMapper;
import com.kg.platform.dao.write.UserCommentWMapper;
import com.kg.platform.enumeration.CommentStatusEnum;
import com.kg.platform.model.in.CommentReplyInModel;
import com.kg.platform.model.out.CommentReplyOutModel;
import com.kg.platform.model.out.UserCommentOutModel;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.request.admin.CommentBatchRequest;
import com.kg.platform.model.request.admin.CommentQueryRequest;
import com.kg.platform.model.response.SiteinfoResponse;
import com.kg.platform.model.response.admin.CommentQueryResponse;
import com.kg.platform.service.PushService;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.TokenManager;
import com.kg.platform.service.admin.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("AdminCommentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    protected JedisUtils jedisUtils;
    @Autowired
    private AdminCommentRMapper adminCommentRMapper;
    @Autowired
    private UserCommentWMapper userCommentWMapper;
    @Autowired
    private SiteinfoWMapper siteinfoWMapper;
    @Autowired
    private TokenManager manager;
    @Autowired
    private KgInfoSwitchRMapper kgInfoSwitchRMapper;
    @Autowired
    private SiteinfoService siteinfoService;
    @Autowired
    private PushService pushService;
    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Override
    public PageModel<CommentQueryResponse> getCommentList(CommentQueryRequest request) {
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        request.setLimit(request.getPageSize());
        Date end = request.getEndDate();
        if (null != end) {
            // 设置时分秒
            request.setEndDate(DateUtils.getDateEnd(end));
        }
        List<CommentQueryResponse> list = adminCommentRMapper.selectByCondition(request);
        if (null != list && list.size() > 0) {
            list.forEach(item -> {
                if (item.getDisplayStatus() == 1) {
                    item.setDisplayStatusDisplay("显示");
                } else if (item.getDisplayStatus() == 2) {
                    item.setDisplayStatusDisplay("删除");
                } else {
                    item.setDisplayStatusDisplay("隐藏");
                }
                item.setStatusDisplay(
                        CommentStatusEnum.getCommentStatusEnum(NumberUtils.intValue(item.getStatus())).getDisplay());
            });
        }
        long count = adminCommentRMapper.selectCountByCondition(request);
        PageModel<CommentQueryResponse> model = new PageModel<>();
        model.setData(list);
        model.setTotalNumber(count);
        model.setCurrentPage(request.getCurrentPage());
        model.setPageSize(request.getPageSize());

        Result<SiteinfoResponse> response = siteinfoService.selectSiteInfo();
        model.setTotalPrice(response.getData().getCommentAudit());

        return model;
    }

    @Override
    public boolean setDisplayStatus(CommentQueryRequest request) {
        Long commentId = request.getCommentId();
        int floorState = request.getFloorState();
        if (floorState == 1) {
            userCommentWMapper.updateDisplayStatusFroComment(commentId, request.getDisplayStatus());
            return true;
        }
        userCommentWMapper.updateDisplayStatusFroCommentReply(commentId, request.getDisplayStatus());
        return true;
    }

    @Override
    public boolean auditComment(CommentQueryRequest request) {
        UserComment comment = new UserComment();
        comment.setCommentId(request.getCommentId());
        comment.setCommentStatus(request.getStatus());
        comment.setRefuseReason(request.getRefuseReason());

        Integer status = request.getStatus();

        int floorState = request.getFloorState();
        int updateComment = 0, updateCommentReply = 0;
        if (floorState == 1) {
            updateComment = userCommentWMapper.updateByPrimaryKeySelective(comment);
            if (updateComment > 0) {
                if (status != null && status == 1) {
                    UserCommentOutModel userCommentOutModel = userCommentRMapper.getCommentById(request.getCommentId());
                    UserCommentRequest userCommentRequest = new UserCommentRequest();
                    userCommentRequest.setCommentId(String.valueOf(userCommentOutModel.getCommentId()));
                    userCommentRequest.setUserId(String.valueOf(userCommentOutModel.getUserId()));
                    userCommentRequest.setArticleId(String.valueOf(userCommentOutModel.getArticleId()));
                    userCommentRequest.setCommentDesc(userCommentOutModel.getCommentDesc());
                    pushService.addComment(userCommentRequest);
                }
                return true;
            }
        }
        if (floorState == 2) {
            updateCommentReply = userCommentWMapper.updateCommentStatusForCommentReply(request.getCommentId(), request.getStatus(), request.getRefuseReason());
            if (updateCommentReply > 0) {
                if (status != null && status == 1) {
                    CommentReplyOutModel commentReply = userCommentRMapper.getCommentReplyById(request.getCommentId());
                    CommentReplyInModel commentReplyInModel = new CommentReplyInModel();
                    BeanUtils.copyProperties(commentReply, commentReplyInModel);
                    pushService.commentReplyPush(commentReplyInModel);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteComment(CommentQueryRequest request) {
        return userCommentWMapper.deleteByPrimaryKey(request.getCommentId()) > 0;
    }

    @Override
    public boolean commentSet(CommentQueryRequest request) {
        Siteinfo siteinfo = new Siteinfo();
        siteinfo.setSiteId(1);
        siteinfo.setCommentAudit(request.getCommentSet());

        jedisUtils.del(JedisKey.siteInfoKey());
        return siteinfoWMapper.updateByPrimaryKeySelective(siteinfo) > 0;
    }

    @Transactional
    @Override
    public void deleteCommentBatch(CommentQueryRequest request) throws Exception {
        List<CommentBatchRequest> commentBatchRequestList = request.getList();
        if (CollectionUtils.isEmpty(commentBatchRequestList)) {
            throw new Exception();
        }
        List<Long> commentIds = new ArrayList<>();
        List<Long> commentReplyIds = new ArrayList<>();
        commentBatchRequestList.forEach(batchReques -> {
            int floorState = batchReques.getFloorState();
            if (floorState == 1) {
                commentIds.add(batchReques.getCommentId());
            } else {
                commentReplyIds.add(batchReques.getCommentId());
            }
        });
        if (!CollectionUtils.isEmpty(commentIds)) {
            userCommentWMapper.updateDisplayStatusFroCommentBatch(commentIds);
        }
        if (!CollectionUtils.isEmpty(commentReplyIds)) {
            userCommentWMapper.updateDisplayStatusFroCommentReolyBatch(commentReplyIds);
        }
    }

}
