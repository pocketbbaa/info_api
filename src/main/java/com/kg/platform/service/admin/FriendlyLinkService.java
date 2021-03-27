package com.kg.platform.service.admin;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.model.request.FriendlyLinkRequest;
import com.kg.platform.model.request.admin.AdminBaseRequest;
import com.kg.platform.model.request.admin.FriendlyLinkEditRequest;
import com.kg.platform.model.response.FriendlyLinkResponse;
import com.kg.platform.model.response.admin.FriendlyLinkQueryResponse;

public interface FriendlyLinkService {

	/**
	 * 列表
	 * @param request
	 * @return
	 */
    PageModel<FriendlyLinkResponse> getLinkList(FriendlyLinkRequest request);

	/**
	 * 删除
	 * @param request
	 * @return
	 */
	boolean deleteLink(FriendlyLinkRequest request);

	/**
	 * 设置排序
	 * @param request
	 * @return
	 */
    boolean setOrder(FriendlyLinkRequest request);

	/**
	 * 批量设置显示状态
	 * @param request
	 * @return
	 */
	boolean batchSetStatus(FriendlyLinkRequest request);

    boolean setChannel(FriendlyLinkEditRequest request);

	/**
	 * 新增或者修改
	 * @param request
	 * @return
	 */
	boolean addOrUpdateLink(FriendlyLinkRequest request);
}
