package com.kg.platform.service.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.model.response.UploadResponse;
import com.kg.platform.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public Result<UploadResponse> uploadImg(MultipartFile[] files) {
        try {

            if (files != null && files.length > 0) {
                // 循环获取file数组中得文件
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    // 保存文件
                    saveFile(file);
                }
            }

            final UploadResponse resp = generateUploadResponse();
            logger.info("上传文件成功, reqModel: " + JSON.toJSONString(files));

            return new Result<UploadResponse>(resp);
        } catch (Exception ex) {
            return new Result<UploadResponse>(12345, ex.getMessage());
        }
    }

    private UploadResponse generateUploadResponse() {
        final UploadResponse resp = new UploadResponse();

        return resp;
    }

    private boolean saveFile(MultipartFile file) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 文件保存路径
                String filePath = "upload/" + file.getOriginalFilename();
                // 转存文件
                file.transferTo(new File(filePath));
                return true;
            } catch (final Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}