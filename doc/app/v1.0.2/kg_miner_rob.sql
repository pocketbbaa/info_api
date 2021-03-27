CREATE TABLE `kg_miner_rob` (
  `rob_id` bigint(20) NOT NULL COMMENT '抢单ID',
  `miner_id` bigint(20) NOT NULL COMMENT '矿机ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `assist_code` varchar(50) NOT NULL COMMENT '助力码',
  `rob_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '抢单时间',
  `rob_status` tinyint(1) DEFAULT '2' COMMENT '抢单状态 0无效 1.已抢到 2.未抢到',
  `deal_date` timestamp NULL DEFAULT NULL COMMENT '成功抢到的时间',
  PRIMARY KEY (`rob_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='矿机抢单记录';
