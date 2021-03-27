package com.kg.platform.controller;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/6/2.
 */
@Controller
@RequestMapping("video")
public class VideoController {

    @Autowired
    private VideoService videoService;
    @ResponseBody
    @RequestMapping("hotVideoList")
    @BaseControllerNote(needCheckToken = false, needCheckParameter = false)
    public JsonEntity hotVideoList(){
        return videoService.hotVideoList();
    }
}
