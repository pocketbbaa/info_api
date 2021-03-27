package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.entity.KgAutherNotice;
import com.kg.platform.model.in.admin.NoticeListModel;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.admin.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin/notice")
public class NoticeController extends AdminBaseController {

    @Autowired
    NoticeService noticeService;

    /**
     * 添加公告
     */
    @ResponseBody
    @RequestMapping("addNoticeInfo")
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true, beanClazz = KgAutherNotice.class)
    public JsonEntity addNoticeInfo(@RequestAttribute KgAutherNotice request, @RequestAttribute(required = false) UserkgResponse kguser){
        return noticeService.addNoticeInfo(request,kguser);
    }

    /**
     * 删除公告
     */
    @RequestMapping("deleteNotice")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true,isLogin = true, beanClazz = KgAutherNotice.class)
    public JsonEntity deleteNotice(@RequestAttribute KgAutherNotice request){
        return noticeService.removeNotice(request);
    }

    /**
     * 编辑公告
     */
    @RequestMapping("updateNotice")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true,beanClazz = KgAutherNotice.class)
    public JsonEntity updateNotice(@RequestAttribute KgAutherNotice request){
        return noticeService.updateNotice(request);
    }

    /**
     * 根据ID查询公告详情
     */
    @RequestMapping("getNoticeById")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true, isLogin = true,beanClazz = KgAutherNotice.class)
    public JsonEntity getNoticeById(@RequestAttribute KgAutherNotice request){
        return noticeService.getNoticeInfoById(request);
    }

    /**
     * 获取公告列表
     */
    @RequestMapping("getBkgNoticeInfo")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true, needCheckParameter = true,isLogin = true, beanClazz = NoticeListModel.class)
    public JsonEntity getBkgNoticeInfo(@RequestAttribute NoticeListModel request){
        return noticeService.getNoticeInfo(request);
    }
}
