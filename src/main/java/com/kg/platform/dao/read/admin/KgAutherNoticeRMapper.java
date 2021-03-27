package com.kg.platform.dao.read.admin;

import com.kg.platform.dao.entity.KgAutherNotice;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 读权限
 */
public interface KgAutherNoticeRMapper {

    int deleteByPrimaryKey(Long id);

    int insert(KgAutherNotice record);

    int insertSelective(KgAutherNotice record);

    KgAutherNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KgAutherNotice record);

    int updateByPrimaryKey(KgAutherNotice record);

    HashMap<String, Object> execSql_sp(@Param("p_sql") String sql);

    List<Object> execSqlList_sp(@Param("p_sql")String s);

    List<Object> selectBysql(@Param("p_sql")String s);

}

