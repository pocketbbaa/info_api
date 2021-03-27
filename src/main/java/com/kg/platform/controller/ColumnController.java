package com.kg.platform.controller;

import java.util.List;

import javax.inject.Inject;

import com.kg.platform.model.in.PlainTextVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.ColumnRequest;
import com.kg.platform.model.response.ColumnResponse;
import com.kg.platform.service.ColumnService;

/**
 * 栏目Controller
 * 
 * @author think
 *
 */
@Controller
@RequestMapping("column")
public class ColumnController extends ApiBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ColumnController.class);

    @Inject
    ColumnService columnService;

    /**
     * 传入参数navigator_display： 2：顶部导航 3：首页主导航 4：频道页主导航
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("listcolumn")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ColumnRequest.class)
    public JsonEntity listcolumn(@RequestAttribute ColumnRequest request) {
        logger.info(JSON.toJSONString(request));
        Result<List<ColumnResponse>> Rlist = null;
        if (null == request.getNavigatorDisplay()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(), "参数值不能是null或者空");
        } else {
            Rlist = columnService.getAllColumn(request);
            return JsonEntity.makeSuccessJsonEntity(Rlist.getData());
        }

    }

    /**
     * 首页二级频道
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("secondaryall")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ColumnRequest.class)
    public JsonEntity getSecondaryAll() {
        Result<List<ColumnResponse>> list = columnService.getSecondaryAll();
        if (list.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(list.getData());
        }
        return JsonEntity.makeExceptionJsonEntity("", "数据获取有误");

    }

    /**
     * 根据一级栏目查二级栏目
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("secondColumn")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ColumnRequest.class)
    public JsonEntity getSecondColumn(@RequestAttribute ColumnRequest request) {
        Result<List<ColumnResponse>> list = columnService.getSecondColumn(request);
        if (list.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(list.getData());
        }
        return JsonEntity.makeExceptionJsonEntity("", "数据获取有误");

    }

    /**
     * 查询SEO
     */
    @ResponseBody
    @RequestMapping("selectSeo")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true, beanClazz = ColumnRequest.class)
    public JsonEntity selectSeo(@RequestAttribute ColumnRequest request) {

        Result<ColumnResponse> result = columnService.selectSeo(request);
        if (result.getData() != null) {
            return JsonEntity.makeSuccessJsonEntity(result);
        }
        return JsonEntity.makeExceptionJsonEntity("", "获取SEO失败");

    }

    /**
     * 敏感词检测
     * @param request
     * @return
     */
    @RequestMapping(value = "checkSensitiveWord",method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(needCheckToken = true,beanClazz = PlainTextVO.class)
    public JsonEntity checkSensitiveWord(@RequestAttribute PlainTextVO request){
        return columnService.checkSensitiveWord(request);
    }
}
