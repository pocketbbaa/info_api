package com.kg.platform.controller.admin;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.AdminRedisUtilRequest;
import com.kg.platform.service.admin.AdminClearRedisKeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/*
*
 * Created by Administrator on 2018/8/3.*/


@Controller
@RequestMapping("admin/redisUtil")
public class AdminClearRedisKeysController extends AdminBaseController{


    @Autowired
    private AdminClearRedisKeysService adminClearRedisKeysService;

    @RequestMapping(value = "/clearRedisKeys", method = RequestMethod.POST)
    @ResponseBody
    @BaseControllerNote(needCheckParameter = true, needCheckToken = false,beanClazz = AdminRedisUtilRequest.class)
    public JsonEntity clearRedisKeys(@RequestAttribute AdminRedisUtilRequest request){
        Boolean result = adminClearRedisKeysService.clearRedisKeys(request);
        if(result){
            return JsonEntity.makeSuccessJsonEntity(null);

        }else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
    }
}
