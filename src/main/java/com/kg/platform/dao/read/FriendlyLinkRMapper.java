package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.in.FriendlyLinkInModel;
import com.kg.platform.model.out.FriendlyLinkOutModel;

public interface FriendlyLinkRMapper {

    FriendlyLinkOutModel selectByPrimaryKey(Integer linkId);

    List<FriendlyLinkOutModel> getFriendlyAll(FriendlyLinkInModel inModel);

    List<FriendlyLinkOutModel> selectByCondition(FriendlyLinkInModel inModel);

    long selectCountByCondition();
}
