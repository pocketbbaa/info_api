package com.kg.platform.service.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.in.admin.BonusInModel;
import com.kg.platform.model.request.admin.*;
import com.kg.platform.model.response.admin.InviteBonusQueryResponse;
import com.kg.platform.model.response.admin.PublishBonusQueryResponse;
import com.kg.platform.model.response.admin.RealnameQueryResponse;
import com.kg.platform.model.response.admin.ShareQueryResponse;
import com.kg.platform.model.response.admin.UserQueryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BonusService {

    PageModel<InviteBonusQueryResponse> getInviteBonus(BonusQueryRequest request);

    PageModel<RealnameQueryResponse> realnameBonus(BonusQueryRequest request);

    PageModel<RealnameQueryResponse> userColumnBonus(BonusQueryRequest request);

    /**
     * 发文奖励
     *
     * @param request
     * @return
     */
    PageModel<PublishBonusQueryResponse> publishArticleBonus(PublishBonusQueryRequest request);

    /**
     * 氪金奖励
     *
     * @param request
     * @return
     */
    Boolean addedTxbBonus(BonusInModel bonusInModel);

    /**
     * 钛值奖励
     *
     * @param request
     * @return
     */
    Boolean addedTvBonus(BonusInModel bonusInModel);

    /**
     * 分享奖励
     *
     * @param request
     * @return
     */
    PageModel<ShareQueryResponse> shareArticleBonus(ShareBonusQueryRequest request);

    /**
     * 检索用户返回列表
     *
     * @param request
     * @return
     */
    Result<List<UserQueryResponse>> getUserBonusInfos(UserBonusQueryRequest userBonusQueryRequest);

    /**
     * 确认发放奖励
     *
     * @param request
     * @return
     */
    Result<UserQueryResponse> confirmBonus(UserBonusQueryRequest userBonusQueryRequest, HttpServletRequest request);

    /**
     * Excel文件上传
     *
     * @param files
     * @return
     */
    JsonEntity bonusListUpload(MultipartFile[] files);

    /**
     * 确认发放用户奖励ForFile
     *
     * @param request
     * @return
     */
    Result<UserQueryResponse> confirmBonusForFile(UserBonusListRequest request);
}
