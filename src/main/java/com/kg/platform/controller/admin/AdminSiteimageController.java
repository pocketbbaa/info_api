package com.kg.platform.controller.admin;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kg.core.imgsrv.exception.ImgExceptionCode;
import com.kg.core.imgsrv.model.ImgUploadInModel;
import com.kg.core.imgsrv.model.ImgUploadOutModel;
import com.kg.core.imgsrv.service.ImgUploadService;
import com.kg.framework.context.Result;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.controller.ApiBaseController;
import com.kg.platform.dao.entity.Siteimage;
import com.kg.platform.model.request.admin.AdminSiteimageQueryRequest;
import com.kg.platform.model.request.admin.SiteimageEditRequest;
import com.kg.platform.service.admin.AdminSiteimageService;

@RestController("AdminSiteimageController")
@RequestMapping("admin/siteimage")
public class AdminSiteimageController extends ApiBaseController {

    @Inject
    AdminSiteimageService adminSiteimageService;

    @Autowired
    private ImgUploadService imageService;

    @RequestMapping("/list")
    @BaseControllerNote(beanClazz = AdminSiteimageQueryRequest.class)
    public JsonEntity listSiteimage(@RequestAttribute AdminSiteimageQueryRequest request) {

        PageModel<Siteimage> page = adminSiteimageService.listImage(request);

        return JsonEntity.makeSuccessJsonEntity(page);
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = "application/json")
    @BaseControllerNote(needCheckToken = false, isLogin = false)
    public Result<ArrayList<ImgUploadOutModel>> upload(@RequestParam("file") MultipartFile[] files,
            @RequestParam(required = false, defaultValue = "kg.com") String imageSource,
            @RequestParam(required = false, defaultValue = "123456789") String userId) {
        long start = System.currentTimeMillis();

        if (Check.NuNArray(files)) {
            return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
        }

        ImgUploadInModel model = new ImgUploadInModel(Long.parseLong(userId), imageSource, files);
        Result<ArrayList<ImgUploadOutModel>> result = imageService.uploadImages(model);

        logger.info("图片上传效率：{}", System.currentTimeMillis() - start);

        return result;
    }

    @RequestMapping(value = "addAdvertise", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SiteimageEditRequest.class)
    public JsonEntity addAdvertise(@RequestAttribute SiteimageEditRequest request) {
        boolean success = adminSiteimageService.addAdvertise(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "getAdvertise", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AdminSiteimageQueryRequest.class)
    public JsonEntity getAdvertise(@RequestAttribute AdminSiteimageQueryRequest request) {
        if (null == request.getImageId()) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
        Siteimage siteimage = adminSiteimageService.getAdvertiseById(request);

        return JsonEntity.makeSuccessJsonEntity(siteimage);
    }

    @RequestMapping(value = "addImage", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SiteimageEditRequest.class)
    public JsonEntity addImage(@RequestAttribute SiteimageEditRequest request) {
        boolean success = adminSiteimageService.addImage(request);
        if (success) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                    ExceptionEnum.SUCCESS.getMessage());
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                    ExceptionEnum.DATAERROR.getMessage());
        }
    }

    @RequestMapping(value = "deleteImage", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SiteimageEditRequest.class)
    public JsonEntity deleteImage(@RequestAttribute SiteimageEditRequest request) {
        if (null != request.getImageId()) {
            boolean success = adminSiteimageService.deleteImage(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }
    }

    @RequestMapping(value = "setStatus", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = SiteimageEditRequest.class)
    public JsonEntity setStatus(@RequestAttribute SiteimageEditRequest request) {
        if (!StringUtils.isBlank(request.getIds()) && null != request.getUpdateUser()) {
            boolean success = adminSiteimageService.setStatus(request);
            if (success) {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
                        ExceptionEnum.SUCCESS.getMessage());
            } else {
                return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR.getCode(),
                        ExceptionEnum.DATAERROR.getMessage());
            }
        } else {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
                    ExceptionEnum.PARAMEMPTYERROR.getMessage());
        }

    }

}
