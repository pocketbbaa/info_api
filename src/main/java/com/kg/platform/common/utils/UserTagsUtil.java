package com.kg.platform.common.utils;

import com.kg.platform.dao.read.UserProfileRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.out.UserkgOutModel;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTagsUtil {

    @Autowired
    private UserRMapper userRMapper;
    @Autowired
    private UserProfileRMapper userProfileRMapper;

    /**
     * 构建未认证专栏身份标签
     *
     * @param userRole
     * @return
     */
    public static String buildIdentityTagWithOutColumnAuthed(Integer userRole) {
        String identityTag;
        switch (userRole) {
            case 2:
                identityTag = "个人专栏";
                break;
            case 3:
                identityTag = "媒体专栏";
                break;
            case 4:
                identityTag = "企业专栏";
                break;
            default:
                identityTag = "其他组织专栏";
                break;
        }
        return identityTag;
    }

    /**
     * 用户身份标签
     *
     * @param userId
     * @param response
     * @return
     */
    public UserTagBuild buildTags(Long userId) {
        UserTagBuild tag = UserTagBuild.initUserTage();
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(userId);
        if (userkgOutModel == null) {
            return tag;
        }
        //普通用户
        Integer userRole = userkgOutModel.getUserRole();
        tag.setRole(userRole);
        if (userRole == 1) {
            return buildTagsForCommonUser(userkgOutModel, tag);
        }
        Integer columnAuthed = userkgOutModel.getColumnAuthed();
        columnAuthed = columnAuthed == null ? 0 : columnAuthed;
        //专栏未认证
        if (columnAuthed == 0) {
            String identityTag = UserTagsUtil.buildIdentityTagWithOutColumnAuthed(userRole);
            tag.setIdentityTag(identityTag);
            return tag;
        }
        //专栏已认证
        tag.setVipTag(columnAuthed);
        tag.setIdentityTag(userProfileRMapper.getIdentityByUserId(userId));
        return tag;
    }

    /**
     * 用户身份标签
     *
     * @param userId
     * @param response
     * @return
     */
    public UserTagBuild buildTags(Long userId, UserkgOutModel userkgOutModel) {
        UserTagBuild tag = UserTagBuild.initUserTage();
        if (userkgOutModel == null) {
            return tag;
        }
        //普通用户
        Integer userRole = userkgOutModel.getUserRole();
        tag.setRole(userRole);
        if (userRole == 1) {
            return buildTagsForCommonUser(userkgOutModel, tag);
        }
        Integer columnAuthed = userkgOutModel.getColumnAuthed();
        columnAuthed = columnAuthed == null ? 0 : columnAuthed;
        //专栏未认证
        if (columnAuthed == 0) {
            String identityTag = UserTagsUtil.buildIdentityTagWithOutColumnAuthed(userRole);
            tag.setIdentityTag(identityTag);
            return tag;
        }
        //专栏已认证
        tag.setVipTag(columnAuthed);
        tag.setIdentityTag(userProfileRMapper.getIdentityByUserId(userId));
        return tag;
    }

    private UserTagBuild buildTagsForCommonUser(UserkgOutModel userkgOutModel, UserTagBuild tag) {
        if (userkgOutModel == null) {
            return tag;
        }
        long realAuthed = userkgOutModel.getRealnameAuthed();
        if (realAuthed == 0) {
            return tag;
        }
        tag.setRealAuthedTag(1);
        return tag;
    }
}
