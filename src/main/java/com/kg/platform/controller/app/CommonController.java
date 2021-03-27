package com.kg.platform.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.kg.core.imgsrv.exception.ImgExceptionCode;
import com.kg.core.imgsrv.model.ImgUploadInModel;
import com.kg.core.imgsrv.model.ImgUploadOutModel;
import com.kg.core.imgsrv.service.ImgUploadService;
import com.kg.framework.context.Result;
import com.kg.framework.toolkit.Check;
import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.AppJsonEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping("/kgApp")
public class CommonController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ImgUploadService imageService;

    /**
     * 文件上传
     *
     * @param files
     * @param imageSource
     * @param userId
     * @return
     */
    @RequestMapping(value = "/image//upload", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @BaseControllerNote(needCheckToken = false, isLogin = false)
    public JSONObject upload(@RequestParam("file") MultipartFile[] files,
                             @RequestParam(required = false, defaultValue = "kg.com") String imageSource,
                             @RequestParam(required = false, defaultValue = "123456789") String userId) {
        long start = System.currentTimeMillis();
        if (Check.NuNArray(files)) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空"));
        }
        try {
            for (MultipartFile file : files) {
                BufferedImage bi = ImageIO.read(file.getInputStream());
                if (bi == null) {
                    return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ImgExceptionCode.IMG_PARAM_EXCEPTION, "此文件不为图片文件"));
                }
                long fileSize = file.getSize();
                if (fileSize <= 0) {
                    return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空"));
                } else if (fileSize > (2 * 1024 * 1024)) {
                    return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ImgExceptionCode.IMG_PARAM_EXCEPTION,
                            "文件大小不能超过2MB"));
                }
            }
        } catch (IOException ioe) {
            return AppJsonEntity.jsonFromObject(AppJsonEntity.makeExceptionJsonEntity(ImgExceptionCode.IMG_PARAM_EXCEPTION, "图片参数错误，请联系管理员"));
        }
        ImgUploadInModel model = new ImgUploadInModel(Long.parseLong(userId), imageSource, files);
        Result<ArrayList<ImgUploadOutModel>> result = imageService.uploadImages(model);
        log.info("图片上传效率：{}", System.currentTimeMillis() - start);
        return AppJsonEntity.jsonFromObject(AppJsonEntity.makeSuccessJsonEntity(result.getData()));
    }


}
