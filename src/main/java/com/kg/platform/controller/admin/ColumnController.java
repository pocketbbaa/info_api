package com.kg.platform.controller.admin;

import java.util.List;

import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.model.request.admin.ProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.ColumnEditRequest;
import com.kg.platform.model.response.admin.TreeQueryResponse;
import com.kg.platform.service.admin.ColumnService;

@RestController("AdminColumnController")
@RequestMapping("admin/column")
public class ColumnController {

    @Autowired
    private ColumnService columnService;

    @RequestMapping(value = "getColumnList", method = RequestMethod.POST)
    @BaseControllerNote
    public JsonEntity getColumnList() {
        List<TreeQueryResponse> list = columnService.getColumnList();
        
        return JsonEntity.makeSuccessJsonEntity(list);
    }

    @RequestMapping(value = "addColumn", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ColumnEditRequest.class)
    public JsonEntity addColumn(@RequestAttribute ColumnEditRequest request,@RequestAttribute SysUser sysUser) {
        boolean success = columnService.addColumn(request,sysUser);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(), "类目名已存在！");
        }
    }

    @RequestMapping(value = "deleteColumn", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ColumnEditRequest.class)
    public JsonEntity deleteColumn(@RequestAttribute ColumnEditRequest request) {
        return columnService.deleteColumn(request);
    }

    @RequestMapping(value = "getColumnById", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = ColumnEditRequest.class)
    public JsonEntity getColumnById(@RequestAttribute ColumnEditRequest request) {
        return JsonEntity.makeSuccessJsonEntity(columnService.getColumnById(request));
    }

    @RequestMapping("setProfileAttr")
    @ResponseBody
    @BaseControllerNote(beanClazz = ProfileVO.class)
    public JsonEntity setProfileAttr(@RequestAttribute ProfileVO profileVO){
        return columnService.setProfileAttr(profileVO);
    }

    @RequestMapping("getProfileAttr")
    @ResponseBody
    @BaseControllerNote(needCheckToken = true)
    public JsonEntity getProfileAttr(){
        return columnService.getProfileAttr();
    }
}
