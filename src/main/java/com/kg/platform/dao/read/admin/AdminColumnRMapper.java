package com.kg.platform.dao.read.admin;

import java.util.List;

import com.kg.platform.dao.entity.Column;
import com.kg.platform.model.in.ColumnInModel;
import com.kg.platform.model.response.ColumnResponse;

public interface AdminColumnRMapper {

    Column selectByPrimaryKey(Integer columnId);

    List<ColumnResponse> selectAll(ColumnInModel model);

}
