
DROP TABLE IF EXISTS `kg_balance_deducted`;
CREATE TABLE `kg_balance_deducted` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `type` tinyint(2) NOT NULL COMMENT '账户类型1：TV，2：KG，3：RIT',
  `state` tinyint(2) NOT NULL COMMENT '状态：1：已处理，0：未处理',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `amount` decimal(20,8) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='平台账户应扣余额';

