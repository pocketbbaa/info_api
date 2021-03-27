package com.kg.platform.dao.cloud;

import com.kg.platform.dao.entity.CloudPackage;
import com.kg.platform.dao.entity.CloudPackageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CloudPackageMapper {
    long countByExample(CloudPackageExample example);

    int deleteByExample(CloudPackageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CloudPackage record);

    int insertSelective(CloudPackage record);

    List<CloudPackage> selectByExample(CloudPackageExample example);

    CloudPackage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CloudPackage record, @Param("example") CloudPackageExample example);

    int updateByExample(@Param("record") CloudPackage record, @Param("example") CloudPackageExample example);

    int updateByPrimaryKeySelective(CloudPackage record);

    int updateByPrimaryKey(CloudPackage record);

    List<CloudPackage> getPackageList();
}