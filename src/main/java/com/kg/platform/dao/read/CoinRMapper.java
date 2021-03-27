package com.kg.platform.dao.read;

import com.kg.platform.model.out.CoinOutModel;
import org.apache.ibatis.annotations.Param;

public interface CoinRMapper {

    /**
     * 根据类型查询币种信息
     *
     * @param code
     * @return
     */
    CoinOutModel getByType(@Param("type") int type);
}
