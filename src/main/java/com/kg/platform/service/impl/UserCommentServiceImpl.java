package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.common.utils.UserTagsUtil;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.read.UserCommentRMapper;
import com.kg.platform.dao.read.UserProfileRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.dao.write.UserCommentWMapper;
import com.kg.platform.enumeration.CommentAuditStatusEnum;
import com.kg.platform.enumeration.CommentStatusEnum;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.in.CommentReplyInModel;
import com.kg.platform.model.in.UserCommentInModel;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.CommentReplyOutModel;
import com.kg.platform.model.out.UserCommentOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserCommentRequest;
import com.kg.platform.model.response.*;
import com.kg.platform.service.PushService;
import com.kg.platform.service.SiteinfoService;
import com.kg.platform.service.UserCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wltea.analyzer.filter.Filter;

import java.io.IOException;
import java.util.*;

@Service
public class UserCommentServiceImpl implements UserCommentService {
    private static final Logger logger = LoggerFactory.getLogger(UserCommentServiceImpl.class);

    private static final int UN_AUTHED = 0; // 未认证

    @Autowired
    private UserCommentRMapper userCommentRMapper;

    @Autowired
    private UserCommentWMapper userCommentWMapper;

    @Autowired
    private IDGen idGenerater;

    @Autowired
    private UserProfileRMapper userProfileRMapper;

    @Autowired
    private ArticleRMapper articleRMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private UserTagsUtil userTagsUtil;

    @Autowired
    private SiteinfoService siteinfoService;

    @Autowired
    private PushService pushService;

    /**
     * 查询我的所有评论
     */
    @Override
    public PageModel<UserCommentResponse> getCommentAll(UserCommentRequest request,
                                                        PageModel<UserCommentResponse> page) {
        logger.info("查询所有评论前端入参：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));

        UserCommentInModel inModel = new UserCommentInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setUserId(Long.parseLong(request.getUserId()));

        List<UserCommentOutModel> list = userCommentRMapper.getUserCommentAll(inModel);
        List<UserCommentResponse> responses = new ArrayList<>();
        for (UserCommentOutModel userCommentOutModel : list) {
            UserCommentResponse response = new UserCommentResponse();
            response.setCommentId(userCommentOutModel.getCommentId().toString());
            response.setArticleId(String.valueOf(userCommentOutModel.getArticleId()));
            response.setArticleImage(userCommentOutModel.getArticleImage());
            response.setArticleTitle(userCommentOutModel.getArticleTitle());
            response.setCommentDate(DateUtils.calculateTime(userCommentOutModel.getCommentDate()));
            response.setCommentDesc(userCommentOutModel.getCommentDesc());
            response.setDisplayStatus(userCommentOutModel.getDisplayStatus());
            response.setCommentStatus(userCommentOutModel.getCommentStatus());
            response.setPublishKind(userCommentOutModel.getPublishKind());
            responses.add(response);

        }
        long count = userCommentRMapper.getUserCommentCount(inModel);
        page.setData(responses);
        page.setTotalNumber(count);
        return page;

    }

    /**
     * 文章评论列表
     */
    @Override
    public PageModel<UserCommentResponse> getCommentArtAll(UserCommentRequest request,
                                                           PageModel<UserCommentResponse> page) {
        logger.info("文章评论列表前端入参：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));

        UserCommentInModel inModel = new UserCommentInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        if (StringUtils.isEmpty(request.getArticleId())) {
            return new PageModel<>();
        }
        try {
            Long articleId = Long.parseLong(request.getArticleId());
            inModel.setArticleId(articleId);
            List<UserCommentOutModel> list = userCommentRMapper.getCommentArtAll(inModel);
            List<UserCommentResponse> responses = new ArrayList<>();
            for (UserCommentOutModel userCommentOutModel : list) {
                UserCommentResponse response = new UserCommentResponse();
                response.setCommentDate(DateUtils.calculateTime(userCommentOutModel.getCommentDate()));
                response.setCommentDateTimestamp(String.valueOf(userCommentOutModel.getCommentDate().getTime()));
                response.setCommentId(userCommentOutModel.getCommentId().toString());
                response.setCommentDesc(userCommentOutModel.getCommentDesc());
                response.setAvatar(userCommentOutModel.getAvaTar());
                response.setUserName(userCommentOutModel.getUserName());
                response.setUserId(userCommentOutModel.getUserId().toString());
                response.setUserRole(userCommentOutModel.getUserRole());
                response = buildTags(response);
                responses.add(response);
            }
            long count = userCommentRMapper.getCommentArtCount(inModel);

            page.setData(responses);
            page.setTotalNumber(count);
            return page;

        } catch (Exception e) {
            return new PageModel<>();
        }
    }

    /**
     * 构建身份标签
     *
     * @param response
     * @return
     */
    private UserCommentResponse buildTags(UserCommentResponse response) {
        Long userId = Long.valueOf(response.getUserId());
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
        if (userkgOutModel == null) {
            return response;
        }
        // 普通用户
        Integer userRole = userkgOutModel.getUserRole();
        if (userRole == 1) {
            long realAuthed = userkgOutModel.getRealnameAuthed();
            if (realAuthed == UN_AUTHED) {
                return response;
            }
            response.setRealAuthedTag(1);
            return response;
        }
        int columnAuthed = userkgOutModel.getColumnAuthed();
        // 专栏未认证
        if (columnAuthed == UN_AUTHED) {
            String identityTag = UserTagsUtil.buildIdentityTagWithOutColumnAuthed(userRole);
            response.setIdentityTag(identityTag);
            return response;
        }
        // 专栏已认证
        response.setVipTag(columnAuthed);
        response.setIdentityTag(userProfileRMapper.getIdentityByUserId(userId));
        return response;
    }

    /**
     * 保存评论
     */
    @Override
    public boolean addComment(UserCommentRequest request) {
        logger.info("保存评论前端入参：{}", JSON.toJSONString(request));

        UserCommentInModel inModel = new UserCommentInModel();
        inModel.setCommentId(idGenerater.nextId());
        inModel.setArticleId(Long.parseLong(request.getArticleId()));
        inModel.setCommentDate(new Date());
        inModel.setCommentDesc(request.getCommentDesc());
        inModel.setUserId(Long.parseLong(request.getUserId()));
        inModel.setCommentStatus(request.getCommentStatus());
        inModel.setDisplayStatus(1);
        if (userCommentWMapper.insertSelective(inModel) > 0) {
            // 推送消息到APP 入库
            if (request.getCommentStatus() != null && request.getCommentStatus() == 1) {
                request.setCommentId(String.valueOf(inModel.getCommentId()));
                pushService.addComment(request);
                return true;
            }
        }
        return false;
    }

    /**
     * 删除评论
     *
     * @return
     */
    public boolean deleteComment(UserCommentRequest request) {
        logger.info("删除评论前端入参：{}", JSON.toJSONString(request));

        UserCommentInModel inModel = new UserCommentInModel();
        inModel.setCommentId(Long.parseLong(request.getCommentId()));
        return userCommentWMapper.deleteByKey(inModel) > 0;

    }

    /**
     * 他人主页评论列表
     */
    @Override
    public PageModel<UserCommentResponse> getOthersComment(UserCommentRequest request,
                                                           PageModel<UserCommentResponse> page) {
        logger.info("他人主页评论列表前端入参：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));

        UserCommentInModel inModel = new UserCommentInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setUserId(Long.parseLong(request.getUserId()));

        List<UserCommentOutModel> list = userCommentRMapper.getOthersComment(inModel);
        List<UserCommentResponse> listresponses = new ArrayList<>();
        for (UserCommentOutModel userCommentOutModel : list) {
            UserCommentResponse response = new UserCommentResponse();
            response.setArticleId(String.valueOf(userCommentOutModel.getArticleId()));
            response.setCommentDesc(userCommentOutModel.getCommentDesc());
            response.setArticleTitle(userCommentOutModel.getArticleTitle());
            response.setCommentDate(DateUtils.calculateTime(userCommentOutModel.getCommentDate()));
            response.setCommentDateTimestamp(String.valueOf(userCommentOutModel.getCommentDate().getTime()));
            response.setPublishKind(userCommentOutModel.getPublishKind());
            response.setArticleImage(userCommentOutModel.getArticleImage());
            response.setArticleImageSize(userCommentOutModel.getArticleImageSize());
            listresponses.add(response);

        }
        long count = userCommentRMapper.getCommentArtCount(inModel);

        page.setData(listresponses);
        page.setTotalNumber(count);
        return page;
    }


    @Override
    public PageModel<CommentResponse> getCommentList(UserCommentRequest request, PageModel<CommentResponse> page) {

        logger.info("getCommentList 入参 ：" + JSONObject.toJSONString(request));
        Long articleId = Long.valueOf(request.getArticleId());
        Long authorId = getAuthorId(articleId);
        int start = (page.getCurrentPage() - 1) * page.getPageSize();
        int limit = page.getPageSize();
        List<UserCommentOutModel> commentOutModelMap = userCommentRMapper.getCommentList(articleId, start, limit);
        List<CommentResponse> commentResponseList = setCommentListDetails(commentOutModelMap, authorId);
        UserCommentInModel inModel = new UserCommentInModel();
        inModel.setArticleId(articleId);
        long count = userCommentRMapper.getCommentArtCount(inModel);
        page.setData(commentResponseList);
        page.setTotalNumber(count);
        return page;
    }

    /**
     * 获取作者ID
     *
     * @param articleId
     * @return
     */
    public Long getAuthorId(Long articleId) {
        ArticleInModel articleInModel = new ArticleInModel();
        articleInModel.setArticleId(articleId);
        ArticleOutModel articleOutModel = articleRMapper.getArticleCreateuser(articleInModel);
        if (articleOutModel == null) {
            return 0L;
        }
        Long authorId = articleOutModel.getCreateUser();
        return authorId == null ? 0L : authorId;
    }

    @Override
    public PageModel<CommentReplyResponse> getCommentReplyDetail(UserCommentRequest request, PageModel<CommentReplyResponse> page) {

        logger.info("getCommentReplyDetail 入参 ：" + JSONObject.toJSONString(request));
        Long commentId = Long.valueOf(request.getCommentId());
        Long articleId = Long.valueOf(request.getArticleId());
        Long authorId = getAuthorId(articleId);
        int start = (page.getCurrentPage() - 1) * page.getPageSize();
        int limit = page.getPageSize();
        page.setData(new ArrayList<>());
        List<CommentReplyOutModel> commentReplyOutModelList = userCommentRMapper.getCommentReplyByTopCommentId(commentId, start, limit);
        if (CollectionUtils.isEmpty(commentReplyOutModelList)) {
            return page;
        }
        Set<Long> userIdsSet = new HashSet<>();
        for (CommentReplyOutModel outModel : commentReplyOutModelList) {
            userIdsSet.add(outModel.getUserId());
            userIdsSet.add(outModel.getToUserId());
        }
        List<Long> userIds = new ArrayList<>(userIdsSet);
        Map<Long, CommentUser> commentUserMap = userRMapper.commentUserList(userIds, authorId);
        List<CommentReplyResponse> commentReplyResponseList = new ArrayList<>();
        for (CommentReplyOutModel outModel : commentReplyOutModelList) {
            commentReplyResponseList.add(buildCommentReplyResponse(outModel, commentUserMap));
        }
        if (CollectionUtils.isEmpty(commentReplyResponseList)) {
            return page;
        }
        long count = userCommentRMapper.getCommentReplyCountByTopCommentId(commentId);
        page.setData(commentReplyResponseList);
        page.setTotalNumber(count);
        return page;
    }


    @Override
    public AppJsonEntity commentReply(UserCommentRequest request, String userId) {
        logger.info("commentReply 入参 ：" + JSONObject.toJSONString(request) + "  userId:" + userId);
        AppJsonEntity checkResult = checkParams(request, userId);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }

        CommentReplyInModel inModel = buildCommentReplyInModel(request, userId);
        Integer success = userCommentWMapper.addCommentReply(inModel);
        if (success != null && success > 0) {
            Integer commentStatus = inModel.getCommentStatus();
            if (commentStatus == CommentStatusEnum.AUDIT.getStatus()) {
                //评论推送
                pushService.commentReplyPush(inModel);
            }

            Map<String, Object> map = buildAddCommentResponseMap(inModel, request, userId);

            if (inModel.getCommentStatus() == CommentStatusEnum.WAIT.getStatus()) {
                return AppJsonEntity.makeExceptionJsonEntity("10030", "评论成功，需要审核，请耐心等待", map);
            }
            return AppJsonEntity.makeSuccessJsonEntity(map);
        }
        return AppJsonEntity.makeExceptionJsonEntity("", "评论失败");
    }

    /**
     * 构建评论回复返回值
     *
     * @param inModel
     * @param request
     * @param userId
     * @return
     */
    private Map<String, Object> buildAddCommentResponseMap(CommentReplyInModel inModel, UserCommentRequest request, String userId) {
        Long authorId = getAuthorId(Long.valueOf(request.getArticleId()));
        Map<String, Object> map = new HashMap<>();
        map.put("commentId", inModel.getCommentId());
        if (userId.equals(String.valueOf(authorId))) {
            map.put("isAuthor", 1);
        } else {
            map.put("isAuthor", 0);
        }
        UserTagBuild userTagBuild = userTagsUtil.buildTags(Long.valueOf(userId));
        map.put("userTagBuild", userTagBuild);
        return map;
    }

    @Override
    public AppJsonEntity delete(UserCommentRequest request, String userId) {

        logger.info("delete 入参 ：" + JSONObject.toJSONString(request) + "  userId:" + userId);
        if (StringUtils.isEmpty(userId)) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if (request == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getCommentId()) || request.getFloorState() <= 0) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Long connentId = Long.valueOf(request.getCommentId());
        int floorState = request.getFloorState();
        Long commentUserId;
        if (floorState == 1) {
            commentUserId = userCommentRMapper.getUserIdByCommentId(connentId);
        } else {
            commentUserId = userCommentRMapper.getUserIdByCommentReplyId(connentId);
        }
        if (commentUserId == null || !String.valueOf(commentUserId).equals(userId)) {
            return AppJsonEntity.makeExceptionJsonEntity("", "删除失败，不能删除不是自己的评论");
        }
        if (floorState == 1) {
            userCommentRMapper.updateByCommentId(connentId);
        } else {
            userCommentRMapper.updateByCommentReplyId(connentId);
        }
        return AppJsonEntity.makeSuccessJsonEntity("删除成功");
    }


    @Override
    public AppJsonEntity myCommentList(UserCommentRequest request, String userId, PageModel<CommentListResponse> page) {
        logger.info("myCommentList 入参 ：" + JSONObject.toJSONString(request) + "  userId:" + userId);
        int start = (page.getCurrentPage() - 1) * page.getPageSize();
        int limit = page.getPageSize();
        long count = userCommentRMapper.getMyCommentListByUserIdCount(Long.valueOf(userId));
        if (count <= 0) {
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        List<CommentReplyOutModel> commentReplyOutModelList = userCommentRMapper.getMyCommentListByUserId(Long.valueOf(userId), start, limit);
        return getUserCommentList(commentReplyOutModelList, count, page);
    }

    @Override
    public AppJsonEntity userCommentList(String userId, PageModel<CommentListResponse> page) {
        logger.info("userCommentList 入参 ：" + JSONObject.toJSONString(page) + "  userId:" + userId);
        int start = (page.getCurrentPage() - 1) * page.getPageSize();
        int limit = page.getPageSize();
        long count = userCommentRMapper.getCommentListByUserIdCount(Long.valueOf(userId));
        if (count <= 0) {
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        List<CommentReplyOutModel> commentReplyOutModelList = userCommentRMapper.getCommentListByUserId(Long.valueOf(userId), start, limit);
        return getUserCommentList(commentReplyOutModelList, count, page);
    }

    @Override
    public AppJsonEntity getCommentDetail(UserCommentRequest request) {
        logger.info("getCommentDetail 入参 ：" + JSONObject.toJSONString(request));
        if (StringUtils.isEmpty(request.getCommentId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Long commentId = Long.valueOf(request.getCommentId());
        UserCommentOutModel model = userCommentRMapper.getCommentById(commentId);
        if (model == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        Long userId = model.getUserId();
        CommentResponse commentResponse = CommentResponse.initBaseInfo(String.valueOf(model.getCommentId()), model.getCommentDesc(), String.valueOf(model.getCommentDate().getTime()), model.getDisplayStatus(), userId);

        Long author = getAuthorId(model.getArticleId());
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(userId);
        Map<Long, CommentUser> commentUserMap = userRMapper.commentUserList(userIdList, author);
        commentResponse.setCommentUser(buildUser(commentUserMap.get(userId)));
        return AppJsonEntity.makeSuccessJsonEntity(commentResponse);
    }

    /**
     * 根据用户ID获取用户评论列表
     *
     * @param userId
     * @param start
     * @param limit
     * @param page
     * @return
     */
    private AppJsonEntity getUserCommentList(List<CommentReplyOutModel> commentReplyOutModelList, long count, PageModel<CommentListResponse> page) {

        if (CollectionUtils.isEmpty(commentReplyOutModelList)) {
            return AppJsonEntity.makeSuccessJsonEntity(page);
        }
        Set<Long> userIds = new HashSet<>();
        Set<Long> articleIds = new HashSet<>();
        Set<Long> parentCommentIds = new HashSet<>();  // 待组装父级评论

        commentReplyOutModelList.forEach(commentReplyOutModel -> {
            Long commentUserId = commentReplyOutModel.getUserId();
            Long replyUserId = commentReplyOutModel.getToUserId();
            Long articleId = commentReplyOutModel.getArticleId();
            if (commentUserId != null) {
                userIds.add(commentUserId);
            }
            if (replyUserId != null) {
                userIds.add(replyUserId);
            }
            if (articleId != null) {
                articleIds.add(articleId);
            }
            if (commentReplyOutModel.getFloorState() == 2) {
                parentCommentIds.add(commentReplyOutModel.getParentCommentId());
            }
        });

        // 组装文章数据
        Map<Long, CommentArticle> commentArticleMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(articleIds)) {
            commentArticleMap = articleRMapper.commentArticleList(articleIds);
        }

        // 组装父级评论数据
        Map<Long, CommentReplyOutModel> parentCommentMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(parentCommentIds)) {
            parentCommentMap = userCommentRMapper.getCommentListByIds(parentCommentIds);
        }

        if (!CollectionUtils.isEmpty(parentCommentMap)) {
            for (Long key : parentCommentMap.keySet()) {
                CommentReplyOutModel parentReplyOutModel = parentCommentMap.get(key);
                if (parentReplyOutModel == null) {
                    continue;
                }
                userIds.add(parentReplyOutModel.getUserId());
            }
        }

        // 组装用户数据
        Map<Long, CommentUser> commentUserMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userIds)) {
            List<Long> userIdList = new ArrayList<>(userIds);
            commentUserMap = userRMapper.commentUserList(userIdList, -1L);
        }


        List<CommentListResponse> commentListResponseList = new ArrayList<>();

        for (CommentReplyOutModel outModel : commentReplyOutModelList) {
            CommentListResponse response = CommentListResponse.initBaseInfo(outModel);
            response.setCommentUser(buildUser(commentUserMap.get(outModel.getUserId())));
            response.setReplyUser(buildUser(commentUserMap.get(outModel.getToUserId())));
            response.setCommentArticle(commentArticleMap.get(outModel.getArticleId()));

            CommentReplyOutModel parentCommentOutModel = parentCommentMap.get(outModel.getParentCommentId());
            response.setParentComment(buildParentComment(parentCommentOutModel, commentUserMap));
            commentListResponseList.add(response);
        }
        page.setData(commentListResponseList);
        page.setTotalNumber(count);
        return AppJsonEntity.makeSuccessJsonEntity(page);
    }

    /**
     * 构建父评论数据
     *
     * @param commentReplyOutModel
     * @param commentUserMap
     * @return
     */
    private ParentComment buildParentComment(CommentReplyOutModel commentReplyOutModel, Map<Long, CommentUser> commentUserMap) {
        if (commentReplyOutModel == null) {
            return null;
        }
        ParentComment comment = new ParentComment();
        comment.setCommentId(String.valueOf(commentReplyOutModel.getCommentId()));
        comment.setCommentDesc(commentReplyOutModel.getCommentDesc());
        comment.setCommentDate(String.valueOf(commentReplyOutModel.getCommentDate().getTime()));
        comment.setDisplayStatus(commentReplyOutModel.getDisplayStatus());
        Long articleId = commentReplyOutModel.getArticleId();
        comment.setArticleId(String.valueOf(articleId));
        Long userId = commentReplyOutModel.getUserId();
        if (userId != null) {
            comment.setCommentUser(buildUser(commentUserMap.get(userId)));
        }
        return comment;
    }


    /**
     * 构建子回复入库数据
     *
     * @param request
     * @param userId
     * @return
     */
    private CommentReplyInModel buildCommentReplyInModel(UserCommentRequest request, String userId) {
        CommentReplyInModel inModel = new CommentReplyInModel();
        Result<SiteinfoResponse> response = siteinfoService.selectSiteInfo();
        if (response.getData().getCommentAudit() == CommentAuditStatusEnum.NEED.getStatus()) {
            inModel.setCommentStatus(CommentStatusEnum.WAIT.getStatus());
        } else {
            inModel.setCommentStatus(CommentStatusEnum.AUDIT.getStatus());
        }
        inModel.setArticleId(Long.valueOf(request.getArticleId()));
        inModel.setCommentDesc(request.getCommentDesc());
        inModel.setUserId(Long.valueOf(userId));
        inModel.setCommentDate(new Date());
        inModel.setParentCommentId(Long.valueOf(request.getParentCommentId()));
        inModel.setCommentId(idGenerater.nextId());
        inModel.setDisplayStatus(1);
        if (StringUtils.isNotEmpty(request.getToUserId())) {
            inModel.setToUserId(Long.valueOf(request.getToUserId()));
        }
        return inModel;
    }

    /**
     * 参数校验
     *
     * @param request
     * @param userId
     * @return
     */
    private AppJsonEntity checkParams(UserCommentRequest request, String userId) {
        if (StringUtils.isEmpty(userId)) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.TOKENRERROR);
        }
        if (request == null) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getArticleId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getParentCommentId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getCommentDesc())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "评论不能为空");
        }
        if (request.getCommentDesc().length() > 500) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "评论内容不能超过500个字");
        }
        try {
            org.wltea.analyzer.result.Result<List<String>> filterResult = Filter.doFilter(request.getCommentDesc());
            if (filterResult.getCode().equals("100")) {
                List<String> words = filterResult.getData();
                return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.SENSITIVE_COMMENT_ERROR.getCode(), words.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return AppJsonEntity.makeSuccessJsonEntity(null);
    }

    /**
     * 回复数据构造
     *
     * @param commentOutModelMap
     * @return
     */
    private List<CommentResponse> setCommentListDetails(List<UserCommentOutModel> commentOutModelMap, Long authorId) {
        if (CollectionUtils.isEmpty(commentOutModelMap)) {
            return new ArrayList<>();
        }
        Set<Long> commentIds = new HashSet<>();
        Set<Long> userIdsSet = new HashSet<>();
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for (UserCommentOutModel outModel : commentOutModelMap) {
            if (outModel == null) {
                continue;
            }
            commentIds.add(outModel.getCommentId());
            Long userId = outModel.getUserId();
            userIdsSet.add(userId);
            String commentDate = String.valueOf(outModel.getCommentDate().getTime());
            commentResponseList.add(CommentResponse.initBaseInfo(String.valueOf(outModel.getCommentId()), outModel.getCommentDesc(), commentDate, outModel.getDisplayStatus(), userId));
        }
        if (CollectionUtils.isEmpty(commentResponseList)) {
            return new ArrayList<>();
        }

        //获取子回复信息
        List<CommentReplyOutModel> commentReplyResponseList = userCommentRMapper.getCommentReplyList(commentIds, 3);

        Map<Long, CommentReplyCount> commentReplyCountMap = userCommentRMapper.getCommentReplyCountMap(commentIds);

        if (!CollectionUtils.isEmpty(commentReplyResponseList)) {
            for (CommentReplyOutModel model : commentReplyResponseList) {
                userIdsSet.add(model.getUserId());
                userIdsSet.add(model.getToUserId());
            }
        }
        List<Long> userIds = new ArrayList<>(userIdsSet);

        //获取用户信息
        Map<Long, CommentUser> commentUserMap = userRMapper.commentUserList(userIds, authorId);

        for (CommentResponse response : commentResponseList) {
            //组装用户
            response.setCommentUser(buildUser(commentUserMap.get(response.getUserId())));

            //组装子回复总数
            int replyCount = 0;
            CommentReplyCount commentReplyCount = commentReplyCountMap.get(Long.valueOf(response.getCommentId()));
            if (commentReplyCount != null) {
                replyCount = commentReplyCount.getReplyCount();
            }
            response.setCommentReplyCount(replyCount);

            Long commentId = Long.valueOf(response.getCommentId());
            List<CommentReplyResponse> commentReplyList = new ArrayList<>();

            if (CollectionUtils.isEmpty(commentReplyResponseList)) {
                continue;
            }
            //组装子回复
            for (CommentReplyOutModel outModel : commentReplyResponseList) {
                Long parentCommentId = outModel.getParentCommentId();
                if (commentId.equals(parentCommentId)) {
                    commentReplyList.add(CommentReplyResponse.build(outModel, commentUserMap));
                }
            }
            response.setCommentReply(commentReplyList);
        }
        return commentResponseList;
    }

    /**
     * 构建用户身份标签
     *
     * @param commentUser
     * @return
     */
    private CommentUser buildUser(CommentUser commentUser) {
        if (commentUser == null) {
            return null;
        }
        UserTagBuild userTagBuild = userTagsUtil.buildTags(commentUser.getUserId());
        commentUser.setIdentityTag(userTagBuild.getIdentityTag());
        commentUser.setRealAuthedTag(userTagBuild.getRealAuthedTag());
        commentUser.setVipTag(userTagBuild.getVipTag());
        return commentUser;
    }

    private CommentReplyResponse buildCommentReplyResponse(CommentReplyOutModel outModel, Map<Long, CommentUser> commentUserMap) {
        CommentReplyResponse replyResponse = new CommentReplyResponse();
        replyResponse.setCommentId(String.valueOf(outModel.getCommentId()));
        replyResponse.setCommentDesc(outModel.getCommentDesc());
        replyResponse.setCommentDate(String.valueOf(outModel.getCommentDate().getTime()));
        replyResponse.setDisplayStatus(outModel.getDisplayStatus());
        replyResponse.setCommentUser(buildUser(commentUserMap.get(outModel.getUserId())));
        Long toUserId = outModel.getToUserId();
        if (toUserId != null && toUserId > 0) {
            replyResponse.setReplyUser(buildUser(commentUserMap.get(outModel.getToUserId())));
        }
        return replyResponse;
    }


}
