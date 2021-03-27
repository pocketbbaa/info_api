package com.kg.platform.service.admin;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.entity.KgAutherNotice;
import com.kg.platform.model.in.admin.NoticeListModel;
import com.kg.platform.model.response.UserkgResponse;

public interface NoticeService {

    /**
     * 获取最新公告
     */
    JsonEntity getNoticeInfoById(KgAutherNotice request);

    /**
     * 添加公告
     */
    JsonEntity addNoticeInfo(KgAutherNotice request, UserkgResponse kguser);

    /**
     * 删除公告
     */
    JsonEntity removeNotice(KgAutherNotice request);

    /**
     * 更新公告
     */
    JsonEntity updateNotice(KgAutherNotice request);

    /**
     * 公告管理查询
     */
    JsonEntity getNoticeInfo(NoticeListModel noticeListModel);
}
