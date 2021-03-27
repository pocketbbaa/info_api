package com.kg.platform.dao.cloud;

import com.kg.platform.dao.entity.CloudOrder;
import com.kg.platform.dao.entity.CloudOrderExample;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CloudOrderMapper {
    long countByExample(CloudOrderExample example);

    int deleteByExample(CloudOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CloudOrder record);

    int insertSelective(CloudOrder record);

    List<CloudOrder> selectByExample(CloudOrderExample example);

    List<CloudOrder> selectByCondition(CloudOrder record);

    CloudOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CloudOrder record, @Param("example") CloudOrderExample example);

    int updateByExample(@Param("record") CloudOrder record, @Param("example") CloudOrderExample example);

    int updateByPrimaryKeySelective(CloudOrder record);

    int updateByPrimaryKey(CloudOrder record);

    long selectCountByCondition(CloudOrder record);

    List<CloudOrder> getPaymentList(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize, @Param("time") String time);


    int issueBTCEarnings(@Param("id") Long id, @Param("dailyEarningsBtcToOrder") BigDecimal dailyEarningsBtcToOrder);

    Double getPerformance(@Param("userId") Long userId, @Param("time") String time);

    Double getTotalEarnings(@Param("userId") Long userId, @Param("time") String time);
}