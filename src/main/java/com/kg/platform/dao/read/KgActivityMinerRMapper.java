package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.out.KgActivityMinerOutModel;

public interface KgActivityMinerRMapper {

    KgActivityMinerOutModel selectByPrimaryKey(Long minerId);

    List<KgActivityMinerOutModel> getMinerList();

}