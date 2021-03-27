package com.kg.platform.model.response;

import java.io.Serializable;
import java.util.List;

public class UploadResponse implements Serializable {

    private static final long serialVersionUID = -2515809961659211040L;

    private List<String> filePath;

    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

}
