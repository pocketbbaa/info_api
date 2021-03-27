SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `kg_muti_bonus_detail`;
CREATE TABLE `kg_muti_bonus_detail` (
  `bonus_detail_id` bigint(20) NOT NULL,
  `extra_bonus_id` bigint(20) NOT NULL COMMENT '额外奖励id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `coin_type` int(2) DEFAULT NULL COMMENT '币种：1rit',
  `amount` decimal(20,8) DEFAULT NULL COMMENT '发放金额',
  `account_flow_id` bigint(20) DEFAULT NULL COMMENT '流水id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`bonus_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='其他币种发放详情';
