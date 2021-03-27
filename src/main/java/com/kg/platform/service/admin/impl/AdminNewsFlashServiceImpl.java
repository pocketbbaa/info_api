package com.kg.platform.service.admin.impl;

import com.alibaba.fastjson.JSON;
import com.kg.framework.idgen.IDGenerater;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.BusinessException;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.lock.Lock;
import com.kg.platform.common.utils.*;
import com.kg.platform.common.utils.sensitivefilter.SensitiveFilter;
import com.kg.platform.dao.read.KgCommonSettingRMapper;
import com.kg.platform.dao.read.admin.AdminKgNewsFlashRMapper;
import com.kg.platform.dao.write.admin.AdminKgNewsFlashWMapper;
import com.kg.platform.model.in.KgCommonSettingInModel;
import com.kg.platform.model.in.KgNewsFlashInModel;
import com.kg.platform.model.out.KgCommonSettingOutModel;
import com.kg.platform.model.out.KgNewsFlashOutModel;
import com.kg.platform.model.request.KgNewsFlashRequest;
import com.kg.platform.model.response.KgNewsFlashResponse;
import com.kg.platform.model.response.PushNewsFlashResponse;
import com.kg.platform.service.NewsFlashService;
import com.kg.platform.service.PushMessageService;
import com.kg.platform.service.PushService;
import com.kg.platform.service.SensitiveWordsService;
import com.kg.platform.service.admin.AdminNewsFlashService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */
@Service
public class AdminNewsFlashServiceImpl implements AdminNewsFlashService {

    private static final String newsFlash_push_limit_key = "newsFlash_push_number";

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminKgNewsFlashRMapper adminKgNewsFlashRMapper;

    @Autowired
    private AdminKgNewsFlashWMapper adminKgNewsFlashWMapper;

    @Autowired
    private IDGenerater idGenerater;

    @Autowired
    private NewsFlashService newsFlashService;

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private KgCommonSettingRMapper kgCommonSettingRMapper;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private SensitiveWordsService sensitiveWordsService;

    @Autowired
    private PushService pushService;

    @Override
    public Result<PageModel<KgNewsFlashResponse>> getNewsFlashListByCondition(KgNewsFlashRequest request,
                                                                              PageModel<KgNewsFlashResponse> page) {
        if (request.getCurrentPage() == 0) {
            page.setCurrentPage(1);
        } else {
            page.setCurrentPage(request.getCurrentPage());
        }
        page.setPageSize(request.getPageSize());

        KgNewsFlashInModel inModel = new KgNewsFlashInModel();
        inModel.setStart((page.getCurrentPage() - 1) * page.getPageSize());
        inModel.setLimit(page.getPageSize());
        inModel.setNewsflashText(request.getNewsflashText());
        inModel.setCreateUserName(request.getCreateUserName());
        inModel.setLevel(request.getLevel());
        inModel.setNewsflashType(request.getNewsflashType());
        inModel.setDisplayStatus(request.getDisplayStatus());
        inModel.setIfPush(request.getIfPush());
        inModel.setNewsflashOrigin(request.getNewsflashOrigin());
        inModel.setRemark(request.getRemark());
        List<KgNewsFlashOutModel> kgNewsFlashOutModelList = adminKgNewsFlashRMapper.selectByCondition(inModel);
        List<KgNewsFlashResponse> responses = new ArrayList<>();
        for (KgNewsFlashOutModel outModel : kgNewsFlashOutModelList) {
            KgNewsFlashResponse response = new KgNewsFlashResponse();
            response.setNewsflashId(outModel.getNewsflashId().toString());
            response.setNewsflashTitle(outModel.getNewsflashTitle());
            response.setNewsflashText(outModel.getNewsflashText());
            response.setNewsflashType(outModel.getNewsflashType());
            response.setLevel(outModel.getLevel());
            response.setIfPush(outModel.getIfPush());
            response.setNewsflashOrigin(outModel.getNewsflashOrigin());
            response.setCreateUserName(outModel.getCreateUserName());
            response.setCreateDate(
                    DateUtils.formatDate(outModel.getCreateDate(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
            response.setDisplayStatus(outModel.getDisplayStatus());
            response.setRemark(outModel.getRemark());
            response.setUpdateUserName(outModel.getUpdateUserName());
            response.setUpdateDate(
                    DateUtils.formatDate(outModel.getUpdateDate(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
            responses.add(response);
        }
        long count = adminKgNewsFlashRMapper.selectCountByCondition(inModel);
        page.setTotalNumber(count);
        page.setData(responses);
        return new Result<>(page);
    }

    @Override
    public Result<KgNewsFlashResponse> detailNewsFlash(KgNewsFlashRequest request) {

        KgNewsFlashOutModel outModel = adminKgNewsFlashRMapper.detailNewsFlash(request.getNewsflashId());
        if (outModel == null) {
            return new Result<>();
        }
        KgNewsFlashResponse response = new KgNewsFlashResponse();
        response.setNewsflashId(outModel.getNewsflashId().toString());
        response.setNewsflashTitle(outModel.getNewsflashTitle());
        response.setNewsflashText(outModel.getNewsflashText());
        response.setLevel(outModel.getLevel());
        response.setNewsflashType(outModel.getNewsflashType());
        response.setNewsflashLink(outModel.getNewsflashLink());
        response.setNewsflashBottomImg(outModel.getNewsflashBottomImg());
        response.setIfPush(outModel.getIfPush());
        response.setDisplayStatus(outModel.getDisplayStatus());

        return new Result<>(response);
    }

    @Override
    public JsonEntity addNewsFlash(KgNewsFlashRequest request) {
        if (StringUtils.isEmpty(request.getNewsflashTitle()) || StringUtils.isEmpty(request.getNewsflashText())
                || request.getNewsflashType() == null || request.getIfPush() == null
                || request.getDisplayStatus() == null || request.getCreateUser() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        //快讯正文和标题敏感词检测
        JsonEntity titleFilter = filterResult(request.getNewsflashTitle(), ExceptionEnum.SENSITIVE_TITLE_ERROR);
        if (titleFilter != null) {
            return titleFilter;
        }

        JsonEntity textFilter = filterResult(request.getNewsflashText(), ExceptionEnum.SENSITIVE_CONTENT_ERROR);
        if (textFilter != null) {
            return textFilter;
        }


        KgNewsFlashInModel inModel = new KgNewsFlashInModel();
        inModel.setNewsflashId(idGenerater.nextId());
        inModel.setNewsflashTitle(request.getNewsflashTitle());
        inModel.setNewsflashText(request.getNewsflashText());
        inModel.setLevel(request.getLevel());
        inModel.setNewsflashType(request.getNewsflashType());
        inModel.setNewsflashLink(request.getNewsflashLink());
        inModel.setNewsflashBottomImg(request.getNewsflashBottomImg());
        inModel.setIfPush(request.getIfPush());
        inModel.setDisplayStatus(request.getDisplayStatus());
        inModel.setNewsflashOrigin(1);// 人工添加
        inModel.setCreateUser(request.getCreateUser());
        int count = adminKgNewsFlashWMapper.insertSelective(inModel);
        if (count > 0) {
            if (request.getIfPush() == 1) {
                // 需要推送，做推送操作
                logger.info("-----------推送快讯消息-----------");
                String title = request.getNewsflashTitle();
                if (title.length() > 20) {
                    title = title.substring(0, 19) + "...";
                }
                pushService.addNewsFlash(title, request.getNewsflashText(), inModel.getNewsflashId().toString());
                // 统计快讯推送
                checkPushNewsFlash();
            }
            return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION);
        }

    }

    @Override
    public JsonEntity delNewsFlash(KgNewsFlashRequest request) {
        if (request.getNewsflashId() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        long count = adminKgNewsFlashWMapper.deleteByPrimaryKey(request.getNewsflashId());
        if (count > 0) {
            return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SYS_EXCEPTION);
        }
    }

    public JsonEntity filterResult(String sentence, ExceptionEnum exceptionEnum) {
        //快讯正文敏感词检测
        Result<SensitiveFilter> filterResult = sensitiveWordsService.getSensitiveFilter();
        if (CheckUtils.checkRetInfo(filterResult, true)) {
            SensitiveFilter filter = filterResult.getData();
            String words = filter.filter(sentence);
            if (StringUtils.isNotBlank(words)) {
                return JsonEntity.makeExceptionJsonEntity(exceptionEnum.getCode(), words);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public JsonEntity updateNewsFlash(KgNewsFlashRequest request) {
        if (StringUtils.isEmpty(request.getNewsflashTitle()) || StringUtils.isEmpty(request.getNewsflashText())
                || request.getNewsflashType() == null || request.getIfPush() == null
                || request.getDisplayStatus() == null || request.getNewsflashId() == null
                || request.getUpdateUser() == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        //快讯标题敏感词检测
        JsonEntity titleFilter = filterResult(request.getNewsflashTitle(), ExceptionEnum.SENSITIVE_TITLE_ERROR);
        if (titleFilter != null) {
            return titleFilter;
        }

        //快讯正文敏感词检测
        JsonEntity textFilter = filterResult(request.getNewsflashText(), ExceptionEnum.SENSITIVE_CONTENT_ERROR);
        if (textFilter != null) {
            return textFilter;
        }

        Lock lock = new Lock();
        try {
            lock.lock(PushNewsFlashResponse.SETTING_KEY + request.getNewsflashId().toString());
            KgNewsFlashOutModel kgNewsFlashOutModel = adminKgNewsFlashRMapper.detailNewsFlash(request.getNewsflashId());
            if (kgNewsFlashOutModel == null) {
                return JsonEntity.makeExceptionJsonEntity("", "快讯不存在");
            }

            KgNewsFlashInModel inModel = new KgNewsFlashInModel();
            inModel.setNewsflashId(request.getNewsflashId());
            inModel.setNewsflashTitle(request.getNewsflashTitle());
            inModel.setNewsflashText(request.getNewsflashText());
            inModel.setLevel(request.getLevel());
            inModel.setNewsflashType(request.getNewsflashType());
            inModel.setNewsflashLink(request.getNewsflashLink());
            inModel.setNewsflashBottomImg(request.getNewsflashBottomImg());
            inModel.setIfPush(request.getIfPush());
            inModel.setDisplayStatus(request.getDisplayStatus());
            inModel.setUpdateUser(request.getUpdateUser());
            inModel.setUpdateDate(new Date());
            int count = adminKgNewsFlashWMapper.updateByPrimaryKeySelective(inModel);
            if (count > 0) {
                // 修改成功
                if (kgNewsFlashOutModel.getIfPush() == 0 && request.getIfPush() == 1) {
                    String title = request.getNewsflashTitle();
                    if (title.length() > 20) {
                        title = title.substring(0, 19) + "...";
                    }
                    pushService.addNewsFlash(title, request.getNewsflashText(), inModel.getNewsflashId().toString());

                    // 推送快讯统计
                    checkPushNewsFlash();
                }
            }
        } catch (Exception e) {
            throw new BusinessException("", e.getMessage());
        } finally {
            lock.unLock();
        }
        return JsonEntity.makeSuccessJsonEntity(ExceptionEnum.SUCCESS);
    }

    @Override
    public JsonEntity getNewsFlashTopMenus() {
        AppJsonEntity appJsonEntity = newsFlashService.getNewsFlashTopMenus();
        return JsonEntity.makeSuccessJsonEntity(appJsonEntity.getData());
    }

    @Override
    public JsonEntity getPushAticleInfo() {
        KgCommonSettingInModel inModel = new KgCommonSettingInModel();
        inModel.setSettingKey(PushNewsFlashResponse.SETTING_KEY);
        KgCommonSettingOutModel outModel = kgCommonSettingRMapper.selectBySettingKey(inModel);
        PushNewsFlashResponse pushNewsFlashResponse = JSON.parseObject(outModel.getSettingValue(),
                PushNewsFlashResponse.class);
        // 取出当日已推送的文章数量
        Integer number = jedisUtils.get(JedisKey.getPushNewsFlashLimit(), Integer.class);
        if (number == null) {
            number = 0;
        }
        pushNewsFlashResponse.setPushNewsFlashNumber(number);
        return JsonEntity.makeSuccessJsonEntity(pushNewsFlashResponse);
    }

    public void checkPushNewsFlash() {
        Lock lock = new Lock();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        try {
            lock.lock(newsFlash_push_limit_key);
            Integer number = jedisUtils.get(JedisKey.getPushNewsFlashLimit(), Integer.class);
            int numberOfDay = 0;
            if (number == null) {
                jedisUtils.set(JedisKey.getPushNewsFlashLimit(), String.valueOf(numberOfDay + 1), zero.getTime());

            } else {
                numberOfDay = number;
                jedisUtils.set(JedisKey.getPushNewsFlashLimit(), String.valueOf(numberOfDay + 1), zero.getTime());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            lock.unLock();
        }
    }
}
