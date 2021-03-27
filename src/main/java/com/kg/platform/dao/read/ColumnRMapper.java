package com.kg.platform.dao.read;

import java.util.HashMap;
import java.util.List;

import com.kg.platform.model.in.ColumnInModel;
import com.kg.platform.model.out.ColumnOutModel;
import org.apache.ibatis.annotations.Param;

public interface ColumnRMapper {

    ColumnOutModel selectByColumnKey(Integer columnId);

    List<ColumnOutModel> selectColumnAll(ColumnInModel inModel);

    List<ColumnOutModel> getSecondaryAll();

    List<ColumnOutModel> getColumnarticleId(ColumnInModel inModel);

    List<ColumnOutModel> getSecondColumn(ColumnInModel inModel);

    ColumnOutModel getsecondColumnName(ColumnInModel inModel);

    List<ColumnOutModel> getSecondColumnByColumnId(ColumnInModel inModel);

    List<ColumnOutModel> selectColumnByName(ColumnInModel model);

    // 根据ID查name
    ColumnOutModel selectByname(ColumnInModel inModel);

    List<ColumnOutModel> appSelectFistColumn();

    List<ColumnOutModel> appSelectFistColumnForSitemap();

    List<ColumnOutModel> appSelectSecondColumn();

    List<ColumnOutModel> appSelectSecondColumnForSitemap();

    void updateGlobalProfile(@Param("rule") Integer rule, @Param("name") String name);

    HashMap getProfile(@Param("name") String name);
}
