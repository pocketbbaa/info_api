package com.kg.platform.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CloudPackageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public CloudPackageExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(Double value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(Double value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(Double value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(Double value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(Double value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(Double value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<Double> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<Double> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(Double value1, Double value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(Double value1, Double value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andCastLinesIsNull() {
            addCriterion("\"cast_ lines\" is null");
            return (Criteria) this;
        }

        public Criteria andCastLinesIsNotNull() {
            addCriterion("\"cast_ lines\" is not null");
            return (Criteria) this;
        }

        public Criteria andCastLinesEqualTo(Double value) {
            addCriterion("\"cast_ lines\" =", value, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesNotEqualTo(Double value) {
            addCriterion("\"cast_ lines\" <>", value, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesGreaterThan(Double value) {
            addCriterion("\"cast_ lines\" >", value, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesGreaterThanOrEqualTo(Double value) {
            addCriterion("\"cast_ lines\" >=", value, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesLessThan(Double value) {
            addCriterion("\"cast_ lines\" <", value, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesLessThanOrEqualTo(Double value) {
            addCriterion("\"cast_ lines\" <=", value, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesIn(List<Double> values) {
            addCriterion("\"cast_ lines\" in", values, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesNotIn(List<Double> values) {
            addCriterion("\"cast_ lines\" not in", values, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesBetween(Double value1, Double value2) {
            addCriterion("\"cast_ lines\" between", value1, value2, "castLines");
            return (Criteria) this;
        }

        public Criteria andCastLinesNotBetween(Double value1, Double value2) {
            addCriterion("\"cast_ lines\" not between", value1, value2, "castLines");
            return (Criteria) this;
        }

        public Criteria andPerformanceIsNull() {
            addCriterion("performance is null");
            return (Criteria) this;
        }

        public Criteria andPerformanceIsNotNull() {
            addCriterion("performance is not null");
            return (Criteria) this;
        }

        public Criteria andPerformanceEqualTo(Double value) {
            addCriterion("performance =", value, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceNotEqualTo(Double value) {
            addCriterion("performance <>", value, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceGreaterThan(Double value) {
            addCriterion("performance >", value, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceGreaterThanOrEqualTo(Double value) {
            addCriterion("performance >=", value, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceLessThan(Double value) {
            addCriterion("performance <", value, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceLessThanOrEqualTo(Double value) {
            addCriterion("performance <=", value, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceIn(List<Double> values) {
            addCriterion("performance in", values, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceNotIn(List<Double> values) {
            addCriterion("performance not in", values, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceBetween(Double value1, Double value2) {
            addCriterion("performance between", value1, value2, "performance");
            return (Criteria) this;
        }

        public Criteria andPerformanceNotBetween(Double value1, Double value2) {
            addCriterion("performance not between", value1, value2, "performance");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentIsNull() {
            addCriterion("\"years_earnings_ percent\" is null");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentIsNotNull() {
            addCriterion("\"years_earnings_ percent\" is not null");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentEqualTo(Double value) {
            addCriterion("\"years_earnings_ percent\" =", value, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentNotEqualTo(Double value) {
            addCriterion("\"years_earnings_ percent\" <>", value, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentGreaterThan(Double value) {
            addCriterion("\"years_earnings_ percent\" >", value, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentGreaterThanOrEqualTo(Double value) {
            addCriterion("\"years_earnings_ percent\" >=", value, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentLessThan(Double value) {
            addCriterion("\"years_earnings_ percent\" <", value, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentLessThanOrEqualTo(Double value) {
            addCriterion("\"years_earnings_ percent\" <=", value, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentIn(List<Double> values) {
            addCriterion("\"years_earnings_ percent\" in", values, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentNotIn(List<Double> values) {
            addCriterion("\"years_earnings_ percent\" not in", values, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentBetween(Double value1, Double value2) {
            addCriterion("\"years_earnings_ percent\" between", value1, value2, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andYearsEarningsPercentNotBetween(Double value1, Double value2) {
            addCriterion("\"years_earnings_ percent\" not between", value1, value2, "yearsEarningsPercent");
            return (Criteria) this;
        }

        public Criteria andTimeLimitIsNull() {
            addCriterion("time_limit is null");
            return (Criteria) this;
        }

        public Criteria andTimeLimitIsNotNull() {
            addCriterion("time_limit is not null");
            return (Criteria) this;
        }

        public Criteria andTimeLimitEqualTo(Integer value) {
            addCriterion("time_limit =", value, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitNotEqualTo(Integer value) {
            addCriterion("time_limit <>", value, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitGreaterThan(Integer value) {
            addCriterion("time_limit >", value, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("time_limit >=", value, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitLessThan(Integer value) {
            addCriterion("time_limit <", value, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitLessThanOrEqualTo(Integer value) {
            addCriterion("time_limit <=", value, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitIn(List<Integer> values) {
            addCriterion("time_limit in", values, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitNotIn(List<Integer> values) {
            addCriterion("time_limit not in", values, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitBetween(Integer value1, Integer value2) {
            addCriterion("time_limit between", value1, value2, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andTimeLimitNotBetween(Integer value1, Integer value2) {
            addCriterion("time_limit not between", value1, value2, "timeLimit");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceIsNull() {
            addCriterion("have_electric_price is null");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceIsNotNull() {
            addCriterion("have_electric_price is not null");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceEqualTo(Boolean value) {
            addCriterion("have_electric_price =", value, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceNotEqualTo(Boolean value) {
            addCriterion("have_electric_price <>", value, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceGreaterThan(Boolean value) {
            addCriterion("have_electric_price >", value, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceGreaterThanOrEqualTo(Boolean value) {
            addCriterion("have_electric_price >=", value, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceLessThan(Boolean value) {
            addCriterion("have_electric_price <", value, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceLessThanOrEqualTo(Boolean value) {
            addCriterion("have_electric_price <=", value, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceIn(List<Boolean> values) {
            addCriterion("have_electric_price in", values, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceNotIn(List<Boolean> values) {
            addCriterion("have_electric_price not in", values, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceBetween(Boolean value1, Boolean value2) {
            addCriterion("have_electric_price between", value1, value2, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveElectricPriceNotBetween(Boolean value1, Boolean value2) {
            addCriterion("have_electric_price not between", value1, value2, "haveElectricPrice");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerIsNull() {
            addCriterion("daily_electric_power is null");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerIsNotNull() {
            addCriterion("daily_electric_power is not null");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerEqualTo(Double value) {
            addCriterion("daily_electric_power =", value, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerNotEqualTo(Double value) {
            addCriterion("daily_electric_power <>", value, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerGreaterThan(Double value) {
            addCriterion("daily_electric_power >", value, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerGreaterThanOrEqualTo(Double value) {
            addCriterion("daily_electric_power >=", value, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerLessThan(Double value) {
            addCriterion("daily_electric_power <", value, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerLessThanOrEqualTo(Double value) {
            addCriterion("daily_electric_power <=", value, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerIn(List<Double> values) {
            addCriterion("daily_electric_power in", values, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerNotIn(List<Double> values) {
            addCriterion("daily_electric_power not in", values, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerBetween(Double value1, Double value2) {
            addCriterion("daily_electric_power between", value1, value2, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andDailyElectricPowerNotBetween(Double value1, Double value2) {
            addCriterion("daily_electric_power not between", value1, value2, "dailyElectricPower");
            return (Criteria) this;
        }

        public Criteria andElectricPriceIsNull() {
            addCriterion("electric_price is null");
            return (Criteria) this;
        }

        public Criteria andElectricPriceIsNotNull() {
            addCriterion("electric_price is not null");
            return (Criteria) this;
        }

        public Criteria andElectricPriceEqualTo(Double value) {
            addCriterion("electric_price =", value, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceNotEqualTo(Double value) {
            addCriterion("electric_price <>", value, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceGreaterThan(Double value) {
            addCriterion("electric_price >", value, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceGreaterThanOrEqualTo(Double value) {
            addCriterion("electric_price >=", value, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceLessThan(Double value) {
            addCriterion("electric_price <", value, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceLessThanOrEqualTo(Double value) {
            addCriterion("electric_price <=", value, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceIn(List<Double> values) {
            addCriterion("electric_price in", values, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceNotIn(List<Double> values) {
            addCriterion("electric_price not in", values, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceBetween(Double value1, Double value2) {
            addCriterion("electric_price between", value1, value2, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andElectricPriceNotBetween(Double value1, Double value2) {
            addCriterion("electric_price not between", value1, value2, "electricPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceIsNull() {
            addCriterion("have_other_price is null");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceIsNotNull() {
            addCriterion("have_other_price is not null");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceEqualTo(Boolean value) {
            addCriterion("have_other_price =", value, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceNotEqualTo(Boolean value) {
            addCriterion("have_other_price <>", value, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceGreaterThan(Boolean value) {
            addCriterion("have_other_price >", value, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceGreaterThanOrEqualTo(Boolean value) {
            addCriterion("have_other_price >=", value, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceLessThan(Boolean value) {
            addCriterion("have_other_price <", value, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceLessThanOrEqualTo(Boolean value) {
            addCriterion("have_other_price <=", value, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceIn(List<Boolean> values) {
            addCriterion("have_other_price in", values, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceNotIn(List<Boolean> values) {
            addCriterion("have_other_price not in", values, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceBetween(Boolean value1, Boolean value2) {
            addCriterion("have_other_price between", value1, value2, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andHaveOtherPriceNotBetween(Boolean value1, Boolean value2) {
            addCriterion("have_other_price not between", value1, value2, "haveOtherPrice");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameIsNull() {
            addCriterion("other_price_name is null");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameIsNotNull() {
            addCriterion("other_price_name is not null");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameEqualTo(String value) {
            addCriterion("other_price_name =", value, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameNotEqualTo(String value) {
            addCriterion("other_price_name <>", value, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameGreaterThan(String value) {
            addCriterion("other_price_name >", value, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameGreaterThanOrEqualTo(String value) {
            addCriterion("other_price_name >=", value, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameLessThan(String value) {
            addCriterion("other_price_name <", value, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameLessThanOrEqualTo(String value) {
            addCriterion("other_price_name <=", value, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameLike(String value) {
            addCriterion("other_price_name like", value, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameNotLike(String value) {
            addCriterion("other_price_name not like", value, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameIn(List<String> values) {
            addCriterion("other_price_name in", values, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameNotIn(List<String> values) {
            addCriterion("other_price_name not in", values, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameBetween(String value1, String value2) {
            addCriterion("other_price_name between", value1, value2, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andOtherPriceNameNotBetween(String value1, String value2) {
            addCriterion("other_price_name not between", value1, value2, "otherPriceName");
            return (Criteria) this;
        }

        public Criteria andClassifyIsNull() {
            addCriterion("classify is null");
            return (Criteria) this;
        }

        public Criteria andClassifyIsNotNull() {
            addCriterion("classify is not null");
            return (Criteria) this;
        }

        public Criteria andClassifyEqualTo(Byte value) {
            addCriterion("classify =", value, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyNotEqualTo(Byte value) {
            addCriterion("classify <>", value, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyGreaterThan(Byte value) {
            addCriterion("classify >", value, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyGreaterThanOrEqualTo(Byte value) {
            addCriterion("classify >=", value, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyLessThan(Byte value) {
            addCriterion("classify <", value, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyLessThanOrEqualTo(Byte value) {
            addCriterion("classify <=", value, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyIn(List<Byte> values) {
            addCriterion("classify in", values, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyNotIn(List<Byte> values) {
            addCriterion("classify not in", values, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyBetween(Byte value1, Byte value2) {
            addCriterion("classify between", value1, value2, "classify");
            return (Criteria) this;
        }

        public Criteria andClassifyNotBetween(Byte value1, Byte value2) {
            addCriterion("classify not between", value1, value2, "classify");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcIsNull() {
            addCriterion("daily_earnings_btc is null");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcIsNotNull() {
            addCriterion("daily_earnings_btc is not null");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcEqualTo(Long value) {
            addCriterion("daily_earnings_btc =", value, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcNotEqualTo(Long value) {
            addCriterion("daily_earnings_btc <>", value, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcGreaterThan(Long value) {
            addCriterion("daily_earnings_btc >", value, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcGreaterThanOrEqualTo(Long value) {
            addCriterion("daily_earnings_btc >=", value, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcLessThan(Long value) {
            addCriterion("daily_earnings_btc <", value, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcLessThanOrEqualTo(Long value) {
            addCriterion("daily_earnings_btc <=", value, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcIn(List<Long> values) {
            addCriterion("daily_earnings_btc in", values, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcNotIn(List<Long> values) {
            addCriterion("daily_earnings_btc not in", values, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcBetween(Long value1, Long value2) {
            addCriterion("daily_earnings_btc between", value1, value2, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsBtcNotBetween(Long value1, Long value2) {
            addCriterion("daily_earnings_btc not between", value1, value2, "dailyEarningsBtc");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbIsNull() {
            addCriterion("daily_earnings_rmb is null");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbIsNotNull() {
            addCriterion("daily_earnings_rmb is not null");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbEqualTo(Double value) {
            addCriterion("daily_earnings_rmb =", value, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbNotEqualTo(Double value) {
            addCriterion("daily_earnings_rmb <>", value, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbGreaterThan(Double value) {
            addCriterion("daily_earnings_rmb >", value, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbGreaterThanOrEqualTo(Double value) {
            addCriterion("daily_earnings_rmb >=", value, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbLessThan(Double value) {
            addCriterion("daily_earnings_rmb <", value, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbLessThanOrEqualTo(Double value) {
            addCriterion("daily_earnings_rmb <=", value, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbIn(List<Double> values) {
            addCriterion("daily_earnings_rmb in", values, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbNotIn(List<Double> values) {
            addCriterion("daily_earnings_rmb not in", values, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbBetween(Double value1, Double value2) {
            addCriterion("daily_earnings_rmb between", value1, value2, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andDailyEarningsRmbNotBetween(Double value1, Double value2) {
            addCriterion("daily_earnings_rmb not between", value1, value2, "dailyEarningsRmb");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Boolean value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Boolean value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Boolean value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Boolean value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Boolean value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Boolean value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Boolean> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Boolean> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Boolean value1, Boolean value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Boolean value1, Boolean value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}