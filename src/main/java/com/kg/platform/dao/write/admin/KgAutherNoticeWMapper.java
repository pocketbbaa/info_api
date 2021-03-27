package com.kg.platform.dao.write.admin;

import com.kg.platform.dao.entity.KgAutherNotice;
import com.kg.platform.model.in.admin.NoticeListModel;

/**
 * 写权限
 */
public interface KgAutherNoticeWMapper {
    int deleteByPrimaryKey(Long id);

    int insert(KgAutherNotice record);

    int insertSelective(KgAutherNotice record);

    KgAutherNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgAutherNotice record);

    int updateByPrimaryKey(KgAutherNotice record);

    KgAutherNotice selectByFilter(NoticeListModel noticeListModel);

}

