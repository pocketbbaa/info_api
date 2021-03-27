package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kg.platform.common.context.Result;
import com.kg.platform.dao.read.HotsearchRMapper;
import com.kg.platform.model.out.HotsearchOutModel;
import com.kg.platform.model.response.HotsearchResponse;
import com.kg.platform.service.HotsearchService;

@Service
public class HotsearchServiceImpl implements HotsearchService {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(HotsearchServiceImpl.class);

    @Inject
    HotsearchRMapper hotsearchRMapper;

    @Override
    public Result<List<HotsearchResponse>> selectHotAll() {
        List<HotsearchOutModel> outModel = hotsearchRMapper.selectHotAll();
        List<HotsearchResponse> list = new ArrayList<HotsearchResponse>();
        for (HotsearchOutModel hotsearchOutModel : outModel) {
            HotsearchResponse response = new HotsearchResponse();
            response.setSearchwordId(hotsearchOutModel.getSearchwordId());
            response.setSearchwordDesc(hotsearchOutModel.getSearchwordDesc());
            list.add(response);
        }

        return new Result<List<HotsearchResponse>>(list);
    }

}
