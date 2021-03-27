package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.in.BaseInfoInModel;
import com.kg.platform.model.out.BaseInfoOutModel;

public interface BaseInfoRMapper {

    BaseInfoOutModel selectByPrimaryKey(Integer infoId);

    List<BaseInfoOutModel> getbaseinfoAll();

    BaseInfoOutModel getbaseinfoType(BaseInfoInModel infoInModel);

    BaseInfoOutModel getbaseinfoName(BaseInfoInModel infoInModel);

}
