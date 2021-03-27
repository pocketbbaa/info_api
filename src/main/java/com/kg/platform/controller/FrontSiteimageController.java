package com.kg.platform.controller;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kg.core.imgsrv.exception.ImgExceptionCode;
import com.kg.core.imgsrv.model.ImgUploadInModel;
import com.kg.core.imgsrv.model.ImgUploadOutModel;
import com.kg.core.imgsrv.service.ImgUploadService;
import com.kg.framework.context.Result;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.aop.BaseControllerNote;

@Controller
@RequestMapping("siteimage")
public class FrontSiteimageController extends ApiBaseController {

    @Inject
    private ImgUploadService imageService;

    @ResponseBody
    @RequestMapping("upload")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = true)
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

}
