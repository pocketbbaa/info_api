package com.kg.platform.service.app.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.idgen.IDGen;
import com.kg.platform.common.mongo.MongoTables;
import com.kg.platform.common.mongo.MongoUtils;
import com.kg.platform.common.utils.Bean2MapUtil;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.common.utils.UserTagsUtil;
import com.kg.platform.dao.read.UserConcernRMapper;
import com.kg.platform.dao.write.UserConcernWMapper;
import com.kg.platform.model.in.UserConcernInModel;
import com.kg.platform.model.mongoTable.FansOperate;
import com.kg.platform.model.out.HotAuthorsOutModel;
import com.kg.platform.model.out.UserConcernListOutModel;
import com.kg.platform.model.out.UserConcernOutModel;
import com.kg.platform.model.request.UserConcernRequest;
import com.kg.platform.model.response.HotAuthorsResponse;
import com.kg.platform.model.response.UserConcernListResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.app.UserConcernService;


/**
 * @author gavine
 * @Date 2018-5-30
 */
@Service
public class UserConcernServiceImpl implements UserConcernService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserConcernRMapper userConcernRMapper;

    @Autowired
    UserConcernWMapper userConcernWMapper;

    @Autowired
    IDGen idGen;

    @Autowired
    JedisUtils jedisUtils;

    @Override
    public AppJsonEntity concernReminder(UserkgResponse kguser) {
        logger.info(">>>>kguser>>>>>>." + kguser);
        if (null == kguser || StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        UserConcernInModel userInModel = new UserConcernInModel();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("-------------????????????????????????---------------");
        String lastTime = jedisUtils.get(JedisKey.getFansTime(kguser.getUserId()));
        if (StringUtils.isEmpty(lastTime)) {
            lastTime = "1900-01-01 00:00:00";
        }
        logger.info("-------------????????????--------------" + lastTime);
        logger.info("-------------???????????????????????????--------------" + lastTime);
        userInModel.setUserId(Long.valueOf(kguser.getUserId()));
        userInModel.setLastTime(lastTime);
        int count = userConcernRMapper.concernReminder(userInModel);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("focusCount", count);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity fansList(UserkgResponse kguser, PageModel<UserConcernListResponse> page) {
        logger.info(">>>>kguser>>>>>>." + kguser);
        if (null == kguser || StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        logger.info("-------------????????????????????????---------------");
        UserConcernInModel inModel = new UserConcernInModel();
        inModel.setUserId(Long.valueOf(kguser.getUserId()));
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        List<UserConcernListOutModel> lis = userConcernRMapper.getFansList(inModel);
        logger.info("???????????????????????????" + JsonUtil.writeValueAsString(lis));
        page.setData(this.getUserConcernListResponse(lis));
        long count = userConcernRMapper.getFansCount(inModel);
        page.setTotalNumber(count);
        page.setCurrentPage(page.getCurrentPage());
        logger.info("-------------??????????????????????????????---------------");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jedisUtils.set(JedisKey.getFansTime(kguser.getUserId()), sdf.format(new Date()));
        logger.info("-------------??????????????????---------------" + sdf.format(new Date()));
        return AppJsonEntity.makeSuccessJsonEntity(page);
    }

    private List<UserConcernListResponse> getUserConcernListResponse(List<UserConcernListOutModel> l) {
        List<UserConcernListResponse> lis = new ArrayList<UserConcernListResponse>();
        for (UserConcernListOutModel outModel : l) {
            UserConcernListResponse response = new UserConcernListResponse();
            response.setAvatar(outModel.getAvatar());
            response.setUserName(outModel.getUserName());
            response.setVipTag(outModel.getVipTag());
            response.setArticleNum(outModel.getArticleNum());
            response.setUserName(outModel.getUserName());
            response.setColumnName(outModel.getColumnName());
            response.setRealAuthedTag(outModel.getRealnameAuthed());
            response.setUserId(outModel.getUserId());
            response.setPublishKind(3);
            logger.info("-------------??????????????????---------------");
            if (outModel.getColumnAuthed() == 0) {
                String identityTag = UserTagsUtil.buildIdentityTagWithOutColumnAuthed(outModel.getUserRole());
                response.setColumnName(identityTag);
            }
            if (outModel.getUserRole() != 1) {
                logger.info("-------------????????????????????? ?????????????????????0-------------------");
                response.setRealAuthedTag(0);
            } else {
                response.setColumnName("");
            }
            response.setConcernedStatus(outModel.getConcernedStatus());
            response.setConcernUserStatus(outModel.getConcernUserStatus());
            response.setUserId(outModel.getUserId());
            response.setUserRole(outModel.getUserRole());
            response.setResume(outModel.getResume());
            lis.add(response);
        }
        return lis;
    }

    @Override
    public AppJsonEntity concernList(UserkgResponse kguser, PageModel<UserConcernListResponse> page) {
        logger.info(">>>>kguser>>>>>>." + kguser);
        if (null == kguser || StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        logger.info("-------------????????????????????????---------------");
        UserConcernInModel inModel = new UserConcernInModel();
        inModel.setUserId(Long.valueOf(kguser.getUserId()));
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        List<UserConcernListOutModel> lis = userConcernRMapper.getConcernList(inModel);
        logger.info("???????????????????????????" + JsonUtil.writeValueAsString(lis));
        page.setData(this.getUserConcernListResponse(lis));
        long count = userConcernRMapper.getConcernCount(inModel);
        page.setTotalNumber(count);
        page.setCurrentPage(page.getCurrentPage());
        return AppJsonEntity.makeSuccessJsonEntity(page);
    }

    @Override
    public AppJsonEntity concernUser(UserkgResponse kguser, UserConcernRequest request) {
        logger.info(">>>>kguser>>>>>>." + kguser + ">>>>>>request>>>>>>" + JsonUtil.writeValueAsString(request));
        if (null == kguser || StringUtils.isEmpty(kguser.getUserId())
                || StringUtils.isEmpty(request.getConcernUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        String concernUserId = request.getConcernUserId();
        logger.info(">>>>>>>>>>>>>>>>>>" + concernUserId);
        // ???????????????????????????
        String concernUserIds[] = concernUserId.split(",");
        logger.info(">>>>>>>>>>>>>>>>>>" + concernUserIds.length);
        for (String s : concernUserIds) {
            // mongo????????????
            FansOperate fansOperate = new FansOperate();
            logger.info("-------------??????????????????---------------");
            UserConcernInModel inModel = new UserConcernInModel();
            inModel.setUserId(Long.valueOf(kguser.getUserId()));
            inModel.setConcernUserId(Long.valueOf(s));
            UserConcernOutModel userConcern = userConcernRMapper.getConcernInfo(inModel);
            if (userConcern == null) {
                logger.info("--------------------????????????-------------------");
                inModel.setConcernId(idGen.nextId());
                inModel.setCreateDate(new Date());
                logger.info("-----------------???????????????????????????-----------------------");
                UserConcernInModel inModels = new UserConcernInModel();
                inModels.setUserId(Long.valueOf(s));
                inModels.setConcernUserId(Long.valueOf(kguser.getUserId()));
                userConcern = userConcernRMapper.getConcernInfo(inModels);
                inModel.setConcernStatus(1);
                if (userConcern != null) {
                    inModel.setConcernStatus(2);
                }
                userConcernWMapper.insertSelective(inModel);
                logger.info("-----------------?????????????????????????????????-----------------------");
                if (inModel.getConcernStatus() == 2) {
                    inModel = new UserConcernInModel();
                    inModel.setConcernStatus(2);
                    inModel.setConcernId(userConcern.getConcernId());
                    userConcernWMapper.updateByPrimaryKeySelective(inModel);
                }
                fansOperate.setOperation(1);
            } else {
                logger.info("??????????????????");
                inModel.setConcernUserId(Long.valueOf(s));
                userConcernWMapper.deleteUserConcern(inModel);
                logger.info("-----------------??????????????????????????????????????????????????????-----------------------");
                inModel = new UserConcernInModel();
                inModel.setConcernUserId(Long.valueOf(kguser.getUserId()));
                inModel.setUserId(Long.valueOf(s));
                userConcern = userConcernRMapper.getConcernInfo(inModel);
                if (userConcern != null && userConcern.getConcernStatus() == 2) {
                    inModel = new UserConcernInModel();
                    inModel.setConcernStatus(1);
                    inModel.setConcernId(userConcern.getConcernId());
                    logger.info("-----------------???????????????????????????????????????-----------------------"
                            + JsonUtil.writeValueAsString(inModel));
                    userConcernWMapper.updateByPrimaryKeySelective(inModel);
                }
                fansOperate.setOperation(0);
            }
            // ??????mongo??????
            fansOperate.setUserId(kguser.getUserId());
            fansOperate.setConcenUserId(s + "");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            fansOperate.setCreateTime(sdf.format(new Date()));
            fansOperate.set_id(idGen.nextId());
            MongoUtils.insertOne(MongoTables.KG_FANS_LOG_TABLE, new Document(Bean2MapUtil.bean2map(fansOperate)));
            // ????????????????????????
            if (concernUserIds.length == 1) {
                inModel = new UserConcernInModel();
                inModel.setUserId(Long.valueOf(s));
                inModel.setConcernUserId(Long.valueOf(kguser.getUserId()));
                userConcern = userConcernRMapper.getConcernInfo(inModel);
                map.put("concernedStatus", 0);
                if (userConcern != null) {
                    map.put("concernedStatus", 1);
                }
                map.put("operation", fansOperate.getOperation());
                map.put("concernUserStatus", fansOperate.getOperation());
            }
        }
        // ????????????????????????
        if (concernUserIds.length > 1) {
            return AppJsonEntity.makeSuccessJsonEntity("????????????");
        }
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public AppJsonEntity recommendHotAuthors(UserkgResponse kguser, PageModel<HotAuthorsResponse> page) {
        logger.info(">>>>kguser>>>>>>." + kguser);
        if (null == kguser || StringUtils.isEmpty(kguser.getUserId())) {
            return AppJsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
        logger.info("???????????????????????????????????????");
        UserConcernInModel inModel = new UserConcernInModel();
        inModel.setUserId(Long.valueOf(kguser.getUserId()));
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        logger.info("-------?????????????????????27??????????????????????????????????????????-------");
        if (page.getCurrentPage() > 1) {
            inModel.setLimit(27 - page.getPageSize());
        }
        logger.info("--------------");
        // ??????????????????????????????ID
        UserConcernInModel userConcernInModel = new UserConcernInModel();
        userConcernInModel.setUserId(Long.valueOf(kguser.getUserId()));
        List<Long> authIds = userConcernRMapper.getConcernedUserIds(userConcernInModel);
        inModel.setAuthIds(authIds);
        List<HotAuthorsOutModel> recommendHotAuthors = userConcernRMapper.getRecommendHotAuthors(inModel);
        page.setData(this.getrecommendHotAuthors(recommendHotAuthors));
        long count = userConcernRMapper.getRecommendHotAuthorsCount(inModel);
        page.setTotalNumber(count);
        logger.info("-------?????????????????????27??????????????????????????????????????????-------");
        if (count > 27) {
            page.setTotalNumber(27);
        }
        logger.info("--------------");
        page.setCurrentPage(page.getCurrentPage());
        return AppJsonEntity.makeSuccessJsonEntity(page);
    }

    private List<HotAuthorsResponse> getrecommendHotAuthors(List<HotAuthorsOutModel> l) {
        List<HotAuthorsResponse> lis = new ArrayList<HotAuthorsResponse>();
        for (HotAuthorsOutModel outModel : l) {
            HotAuthorsResponse response = new HotAuthorsResponse();
            response.setAvatar(outModel.getAvatar());
            response.setColumnName(outModel.getColumnName());
            response.setResume(outModel.getResume());
            response.setUserId(outModel.getUserId());
            lis.add(response);
        }
        return lis;
    }

    @Override
    public AppJsonEntity hotAuthorList(UserConcernRequest request, PageModel<UserConcernListResponse> page) {
        UserConcernInModel inModel = new UserConcernInModel();
        inModel.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        inModel.setLimit(request.getPageSize());
        logger.info("-------?????????????????????27??????????????????????????????????????????-------");
        if (request.getCurrentPage() > 1) {
            inModel.setLimit(27 - request.getPageSize());
        }
        logger.info("--------------");
        // ??????????????????????????????ID
        inModel.setUserId(request.getUserId());
        logger.info(">>>>>>>>>>>>inModel>>>>>>>>>>>" + JsonUtil.writeValueAsString(inModel));
        List<UserConcernListOutModel> hotAuthors = userConcernRMapper.getHotAuthors(inModel);
        logger.info(">>>>>>>>>>hotAuthors>>>>>>>>>" + JsonUtil.writeValueAsString(hotAuthors));
        page.setData(this.getUserConcernListResponse(hotAuthors));
        logger.info(">>>>>>>>>>hotAuthorsResp>>>>>>>>>" + JsonUtil.writeValueAsString(page.getData()));
        long count = userConcernRMapper.getHotAuthorsCount(inModel);
        page.setTotalNumber(count);
        logger.info("-------?????????????????????27??????????????????????????????????????????-------");
        if (count > 27) {
            page.setTotalNumber(27);
        }
        logger.info("--------------");
        page.setCurrentPage(request.getCurrentPage());
        logger.info(">>>>>>>>>>>>??????page>>>>>>>>>>>" + JsonUtil.writeValueAsString(page));
        return AppJsonEntity.makeSuccessJsonEntity(page);
    }

    @Override
    public PageModel<UserConcernListResponse> searchAuthorList(UserConcernRequest request, PageModel<UserConcernListResponse> page) {
        UserConcernInModel inModel = new UserConcernInModel();
        inModel.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        inModel.setLimit(request.getPageSize());
        // ??????????????????????????????ID
        inModel.setUserId(request.getUserId());
        inModel.setSearchStr(request.getSearchStr());
        List<UserConcernListOutModel> hotAuthors = userConcernRMapper.searchAuthors(inModel);
        page.setData(this.getUserConcernListResponse(hotAuthors));
        long count = userConcernRMapper.searchAuthorsCount(inModel);
        page.setTotalNumber(count);
        page.setCurrentPage(request.getCurrentPage());
        return page;
    }

    @Override
    public boolean isConcerned(Long userId, Long relUserId) {
        UserConcernInModel model = new UserConcernInModel();
        model.setUserId(relUserId);
        model.setConcernUserId(userId);
        UserConcernOutModel userConcernOutModel = userConcernRMapper.getConcernInfo(model);
        return userConcernOutModel != null;
    }

    @Override
    public UserConcernOutModel getConcernInfo(Long userId, Long relUserId) {
        UserConcernInModel model = new UserConcernInModel();
        model.setUserId(relUserId);
        model.setConcernUserId(userId);
        return userConcernRMapper.getConcernInfo(model);
    }

}
