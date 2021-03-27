package com.kg.platform.common.support;

import java.beans.PropertyEditorSupport;

import com.kg.platform.common.utils.NumberUtils;
import com.kg.platform.common.utils.StringUtils;

public class CustomBigDecimalEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            // Treat empty String as null value.
            setValue(0.000);
        } else {

            setValue(NumberUtils.getBigDecimal(text));
        }
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }
}
