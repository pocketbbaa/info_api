package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.Hotsearch;
import com.kg.platform.model.out.HotsearchOutModel;

public interface HotsearchRMapper {

    Hotsearch selectByPrimaryKey(Integer searchwordId);

    List<HotsearchOutModel> selectHotAll();
}
