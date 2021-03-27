package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.SensitiveWords;

public interface SensitiveWordsRMapper {

    SensitiveWords selectByPrimaryKey(Integer wordId);

    List<String> selectAllWords();

}
