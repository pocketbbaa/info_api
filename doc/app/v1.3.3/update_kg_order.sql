alter table kg_order modify column origin tinyint(2) NOT NULL COMMENT '三方来源 1：兑吧扣积分 11：兑吧加积分';
ALTER TABLE kg_order ADD INDEX create_date ( `create_date` );