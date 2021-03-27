package com.kg.platform.dao.read;


import com.kg.platform.model.out.KgColumnCountOutModel;

import java.util.List;

public interface KgColumnCountRMapper {

    KgColumnCountOutModel selectByPrimaryKey(String columnKey);

    List<KgColumnCountOutModel> getAll();

}