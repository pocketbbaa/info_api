package com.kg.platform.dao.read;

import com.kg.platform.model.in.KgInfoSwitchInModel;
import com.kg.platform.model.out.KgInfoSwitchOutModel;

import java.util.List;

public interface KgInfoSwitchRMapper {


    KgInfoSwitchOutModel selectByPrimaryKey(Long userId);

    List<String> getAllUserIdBySwitchOn(KgInfoSwitchInModel inModel);

}