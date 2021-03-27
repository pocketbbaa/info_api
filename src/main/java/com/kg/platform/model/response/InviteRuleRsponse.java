package com.kg.platform.model.response;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/5/3.
 */
public class InviteRuleRsponse {
    private Long _count;
    private BigDecimal _bonus;

    public Long get_count() {
        return _count;
    }

    public void set_count(Long _count) {
        this._count = _count;
    }

    public BigDecimal get_bonus() {
        return _bonus;
    }

    public void set_bonus(BigDecimal _bonus) {
        this._bonus = _bonus;
    }
}
