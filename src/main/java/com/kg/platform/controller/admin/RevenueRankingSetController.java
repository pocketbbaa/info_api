package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.request.admin.RevenueRankingSetRequest;
import com.kg.platform.service.admin.RevenueRankingSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收益排行设置
 */
@RestController("AdminRevenueRankingSetController")
@RequestMapping("admin/author/revenue")
public class RevenueRankingSetController extends AdminBaseController {

    private static final String KEY = "revenue_ranking_set";

    @Autowired
    private RevenueRankingSetService revenueRankingSetService;

    /**
     * 获取设置
     *
     * @return
     */
    @RequestMapping(value = "getSet", method = RequestMethod.POST)
    @BaseControllerNote
    public JsonEntity getSet() {
        return revenueRankingSetService.getSet(KEY);
    }

    /**
     * 添加设置
     *
     * @return
     */
    @RequestMapping(value = "set", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = RevenueRankingSetRequest.class)
    public JsonEntity updateSet(@RequestAttribute RevenueRankingSetRequest request, @RequestAttribute SysUser sysUser) {
        return revenueRankingSetService.updateSet(KEY, request,sysUser);
    }

}
