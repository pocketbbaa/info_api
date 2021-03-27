package com.kg.platform.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.model.request.admin.AdminPostEditRequest;
import com.kg.platform.model.response.admin.PostQueryResponse;
import com.kg.platform.model.response.admin.TreeQueryResponse;
import com.kg.platform.service.admin.PostService;

@RestController("AdminPostController")
@RequestMapping("admin/post")
public class PostController extends AdminBaseController {

    @Autowired
    private PostService postService;

    @RequestMapping(value = "getAuthTree", method = RequestMethod.POST)
    @BaseControllerNote
    public JsonEntity getAuthTree() {
        List<TreeQueryResponse> data = postService.getAuthTree();
        return JsonEntity.makeSuccessJsonEntity(data);
    }

    @RequestMapping(value = "addPost", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminPostEditRequest.class)
    public JsonEntity addPost(@RequestAttribute AdminPostEditRequest request) {
        boolean success = postService.addPost(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "getPostList", method = RequestMethod.POST)
    @BaseControllerNote
    public JsonEntity getPostList() {
        List<PostQueryResponse> list = postService.getPostList();
        return JsonEntity.makeSuccessJsonEntity(list);
    }

    @RequestMapping(value = "setStatus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminPostEditRequest.class)
    public JsonEntity setStatus(@RequestAttribute AdminPostEditRequest request) {
        boolean success = postService.setStatus(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }
}
