package com.kg.platform.enumeration;

import java.math.BigDecimal;

/**
 * 邀请人数所对应的TV奖励
 * 
 * @author 74190
 *
 */
public enum InviteBonusEnum {

    /**
     * 邀请至10人对应1.00TV
     */
    TEN(10L, new BigDecimal(1.00)),

    /**
     * 邀请至30人对应5.00TV
     */
    THRITY(30L, new BigDecimal(5.00)),

    /**
     * 邀请至50人对应10.00TV
     */
    FIFTY(50L, new BigDecimal(10.00)),

    /**
     * 邀请至80人对应20.00TV
     */
    EIGHTY(80L, new BigDecimal(20.00));

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

    InviteBonusEnum(Long _count, BigDecimal bonus) {
        this._count = _count;
        this._bonus = bonus;
    }

}
