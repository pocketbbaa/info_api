package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.model.in.KeywordsInModel;
import com.kg.platform.model.out.KeywordsOutModel;

public interface KeywordsRMapper {

    KeywordsOutModel selectByPrimaryKey(Integer keywordId);

    List<KeywordsOutModel> getKeywordsAll(KeywordsInModel inModel);

}
