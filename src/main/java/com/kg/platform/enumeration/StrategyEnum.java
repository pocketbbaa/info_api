package com.kg.platform.enumeration;

import com.kg.platform.enumeration.enumHelper.EnumFindHelper;
import com.kg.platform.enumeration.enumHelper.EnumKeyGetter;
import com.kg.platform.model.strategyUtil.*;

public enum StrategyEnum {

    /**
     * 策略表
     */
    BASISQUERY(1,"基本查询方式",NormalQueryStrategy.class),
    MASTERQUERY(2,"指定师傅字段查询方式",MasterQueryStrategy.class),
    ORDERARTICLE(3,"文章数排序方式",ArticleQueryStrategy.class),
    ORDERCOMMENT(4,"评论量排序方式",CommentQueryStrategy.class),
    ORDERCOLLECT(5,"收藏量方式排序",CollectQueryStrategy.class),
    ORDERSHARE(6,"分享量方式排序",ShareQueryStrategy.class),
    AUDITDATE(7,"审核时间方式排序",NormalQueryStrategy.class);

    private Integer no;
    private String desc;
    private Class<? extends BaseStrategyCentre> filedClassName;

    StrategyEnum(Integer no,String desc,Class<? extends BaseStrategyCentre> cls){
        this.no = no;
        this.desc = desc;
        this.filedClassName = cls;
    }

    static final EnumFindHelper<StrategyEnum,Integer> Helper = new EnumFindHelper<StrategyEnum,Integer>(
            StrategyEnum.class,new Getter());

    /**
     * 枚举处理器返回结果处理方式
     */
    static class Getter implements EnumKeyGetter<StrategyEnum, Integer> {
        @Override
        public Integer getKey(StrategyEnum enumValue) {
            return enumValue.no;
        }
    }


    /**
     * 通过key查询value
     */
    public static StrategyEnum find(Integer no,StrategyEnum defaultValue){
        return Helper.find(no, defaultValue);
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Class<? extends BaseStrategyCentre> getFiledClassName() {
        return filedClassName;
    }

    public void setFiledClassName(Class<? extends BaseStrategyCentre> filedClassName) {
        this.filedClassName = filedClassName;
    }
}
