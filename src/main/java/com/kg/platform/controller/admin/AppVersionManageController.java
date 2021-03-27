package com.kg.platform.controller.admin;

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
import com.kg.platform.model.request.admin.AppVersionManageRequest;
import com.kg.platform.model.response.admin.AppVersionManageResponse;
import com.kg.platform.service.admin.AppVersionManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * APP 版本管理
 * <p>
 * by wangyang 2018/03/29
 */
@RestController("AppVersionManageController")
@RequestMapping("admin/appvm")
public class AppVersionManageController extends ApiBaseController {

    @Autowired
    private AppVersionManageService appVersionManageService;

    @Autowired
    private ImgUploadService imageService;

    /**
     * 获取分页列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AppVersionManageRequest.class)
    public JsonEntity list(@RequestAttribute AppVersionManageRequest request) {
        PageModel<AppVersionManageResponse> page = new PageModel<>();
        page.setCurrentPage(request.getCurrentPage());
        page.setPageSize(request.getPageSize());
        page = appVersionManageService.getList(request, page);
        return JsonEntity.makeSuccessJsonEntity(page);
    }

    /**
     * 新建版本
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AppVersionManageRequest.class)
    public JsonEntity create(@RequestAttribute AppVersionManageRequest request) {
        if (request == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (StringUtils.isEmpty(request.getVersionNum())) {
            return JsonEntity.makeExceptionJsonEntity("", "版本号不能为空");
        }
        if (request.getSystemType() != 1 && request.getSystemType() != 2) {
            return JsonEntity.makeExceptionJsonEntity("", "操作系统选择有误:" + request.getSystemType());
        }
        if(StringUtils.isEmpty(request.getDownloadUrl())){
            return JsonEntity.makeExceptionJsonEntity("", "下载地址不能为空");
        }
        if(StringUtils.isEmpty(request.getDownloadUrl())){
            if(request.getSystemType() == 1){
                if(StringUtils.isEmpty(request.getDownloadUrlApk())){
                    return JsonEntity.makeExceptionJsonEntity("", "版本地址不能为空");
                }
            }
        }
        AppVersionManageResponse version = appVersionManageService.getByVersionAndSysType(request);
        if (version != null) {
            return JsonEntity.makeExceptionJsonEntity("", "该版本信息已存在,创建失败");
        }
        if (appVersionManageService.create(request)) {
            return JsonEntity.makeSuccessJsonEntity("创建成功");
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
    }

    /**
     * 查看历史版本
     *
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AppVersionManageRequest.class)
    public JsonEntity getDetail(@RequestAttribute AppVersionManageRequest request) {
        if (request == null || request.getId() == null || request.getId() < 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        AppVersionManageResponse response = appVersionManageService.getDetailById(request);
        if (response == null) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
        }
        return JsonEntity.makeSuccessJsonEntity(response);
    }

    /**
     * 删除版本
     *
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BaseControllerNote(beanClazz = AppVersionManageRequest.class)
    public JsonEntity delete(@RequestAttribute AppVersionManageRequest request) {
        if (request == null || request.getId() == null || request.getId() < 0) {
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if (appVersionManageService.deleteById(request)) {
            return JsonEntity.makeSuccessJsonEntity("删除成功");
        }
        return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.DATAERROR);
    }

    /**
     * 文件上传
     *
     * @param files
     * @param imageSource
     * @param userId
     * @return
     */
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @BaseControllerNote(needCheckToken = false)
    public Result<ArrayList<ImgUploadOutModel>> uploadFile(@RequestParam("file") MultipartFile[] files,
                                                           @RequestParam(required = false, defaultValue = "kg.com") String imageSource,
                                                           @RequestParam(required = false, defaultValue = "123456789") String userId) {
        long start = System.currentTimeMillis();
        if (Check.NuNArray(files)) {
            return new Result<>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
        }
        for (MultipartFile file : files) {
            long fileSize = file.getSize();
            logger.info("uploadFile -> fileSize:" + fileSize);
            if (fileSize <= 0) {
                return new Result<>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "上传文件为空");
            } else if (fileSize > (100 * 1024 * 1024)) {
                return new Result<>(ImgExceptionCode.IMG_PARAM_EXCEPTION, "文件大小不能超过100MB");
            }
        }
        ImgUploadInModel model = new ImgUploadInModel(Long.parseLong(userId), imageSource, files);
        Result<ArrayList<ImgUploadOutModel>> result = imageService.uploadFile(model);
        logger.info("文件上传效率：{}", System.currentTimeMillis() - start);
        return result;
    }

}
