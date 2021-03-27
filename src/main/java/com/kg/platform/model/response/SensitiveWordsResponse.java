package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.List;

import com.kg.platform.dao.entity.SensitiveWords;

public class SensitiveWordsResponse implements Serializable {

    private static final long serialVersionUID = -2515809961659211040L;

    private List<SensitiveWords> allWords;

    public List<SensitiveWords> getAllWords() {
        return allWords;
    }

    public void setAllWords(List<SensitiveWords> allWords) {
        this.allWords = allWords;
    }

}
