package com.kg.platform.service;

import org.springframework.web.multipart.MultipartFile;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.response.UploadResponse;

public interface ImageService {

    Result<UploadResponse> uploadImg(MultipartFile[] files);

}
