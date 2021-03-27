package com.kg.platform.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("image")
public class ImageUploadController extends ApiBaseController {

    @Autowired
    private ImgUploadService imageService;

    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @BaseControllerNote(needCheckToken = false, isLogin = false)
    public Result<ArrayList<ImgUploadOutModel>> upload(@RequestParam("file") MultipartFile[] files,
            @RequestParam(required = false, defaultValue = "kg.com") String imageSource,
            @RequestParam(required = false, defaultValue = "123456789") String userId) {
        long start = System.currentTimeMillis();

        if (Check.NuNArray(files)) {
            return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
        }

        try {
            for (MultipartFile file : files) {
                BufferedImage bi = ImageIO.read(file.getInputStream());
                if (bi == null) {
                    return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "此文件不为图片文件");
                }
                long fileSize = file.getSize();
                if (fileSize <= 0) {
                    return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
                } else if (fileSize > (2 * 1024 * 1024)) {
                    return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION,
                            "文件大小不能超过2MB");
                }
            }
        } catch (IOException ioe) {
            return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "图片参数错误，请联系管理员");
        }

        ImgUploadInModel model = null;
        try {
            model = new ImgUploadInModel(Long.parseLong(userId), imageSource, files);
        } catch (Exception e) {
            logger.error("【创建图片model异常 e:{}】"+e.getMessage());
            e.printStackTrace();
            return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "图片参数错误，请联系管理员");
        }
        Result<ArrayList<ImgUploadOutModel>> result = imageService.uploadImages(model);

        logger.info("图片上传效率：{}", System.currentTimeMillis() - start);

        return result;
    }

    @RequestMapping(value = "uploadFile", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @BaseControllerNote(needCheckToken = false, isLogin = false)
    public Result<ArrayList<ImgUploadOutModel>> uploadFile(@RequestParam("file") MultipartFile[] files,
            @RequestParam(required = false, defaultValue = "kg.com") String imageSource,
            @RequestParam(required = false, defaultValue = "123456789") String userId) {
        long start = System.currentTimeMillis();

        if (Check.NuNArray(files)) {
            return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
        }

        for (MultipartFile file : files) {
            long fileSize = file.getSize();
            logger.info("=======" + fileSize);
            if (fileSize <= 0) {
                return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
            } else if (fileSize > (100 * 1024 * 1024)) {
                return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "文件大小不能超过100MB");
            }
        }

        ImgUploadInModel model = new ImgUploadInModel(Long.parseLong(userId), imageSource, files);
        Result<ArrayList<ImgUploadOutModel>> result = imageService.uploadImages(model);

        logger.info("文件上传效率：{}", System.currentTimeMillis() - start);

        return result;
    }

    @RequestMapping(value = "uploadVideo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @BaseControllerNote(needCheckToken = false, isLogin = false)
    public Result<ArrayList<ImgUploadOutModel>> uploadVideo(@RequestParam("file") MultipartFile[] files,
            @RequestParam(required = false, defaultValue = "kg.com") String imageSource,
            @RequestParam(required = false, defaultValue = "123456789") String userId) {
        long start = System.currentTimeMillis();

        if (Check.NuNArray(files)) {
            return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
        }

        // try {
        for (MultipartFile file : files) {
            // BufferedImage bi = ImageIO.read(file.getInputStream());
            String filetype = propertyLoader.getProperty("common", "video.suffixs");
            Pattern p = Pattern.compile(filetype);
            if (!p.matcher(file.getOriginalFilename()).find()) {
                return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "此文件不为视频文件");
            }
            long fileSize = file.getSize();
            if (fileSize <= 0) {
                return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
            } else if (fileSize > (200 * 1024 * 1024)) {
                return new Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "文件大小不能超过200MB");
            }
        }
        // } catch (IOException ioe) {
        // return new
        // Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.IMG_PARAM_EXCEPTION,
        // "视频参数错误，请联系管理员");
        // }

        ImgUploadInModel model = new ImgUploadInModel(Long.parseLong(userId), imageSource, files);
        Result<ArrayList<ImgUploadOutModel>> result = imageService.uploadVideo(model);

        logger.info("视频上传效率：{}", System.currentTimeMillis() - start);

        return result;
    }

    /*
     * @RequestMapping(value = "uploadbase64", method = RequestMethod.POST,
     * produces = "application/json")
     * 
     * @ResponseBody
     * 
     * @BaseControllerNote(needCheckToken = false, isLogin = false) public
     * Result<ArrayList<ImgUploadOutModel>> uploadBase64(@RequestParam("imgstr")
     * String base64str,
     * 
     * @RequestParam(required = false, defaultValue = "kg.com") String
     * imageSource,
     * 
     * @RequestParam(required = false, defaultValue = "123456789") String
     * userId) { long start = System.currentTimeMillis();
     * 
     * if (Check.NuNObject(base64str)) { new
     * Result<ArrayList<ImgUploadOutModel>>(ImgExceptionCode.
     * IMG_PARAM_EXCEPTION, "上传文件为空"); }
     * 
     * // ImgUploadInModel model = new ImgUploadInModel(base64str);
     * Result<ArrayList<ImgUploadOutModel>> result =
     * imageService.uploadBase64Image(model);
     * 
     * logger.info("图片上传效率：{}", System.currentTimeMillis() - start);
     * 
     * return result; }
     */
}
