CREATE TABLE `kg_miner_assist` (
  `assist_id` bigint(20) NOT NULL COMMENT '助力ID',
  `rob_id` bigint(20) NOT NULL COMMENT '抢单ID',
  `user_id` bigint(20) NOT NULL COMMENT '助力用户ID',
  `assist_amount` int(20) NOT NULL DEFAULT '0' COMMENT '蓄能值',
  `assist_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '助力时间',
  `assist_status` tinyint(1) DEFAULT '1' COMMENT '助力状态 0:无效 1：正常',
  `miner_id` bigint(20) NOT NULL COMMENT '对应抢单的矿机ID',
  PRIMARY KEY (`assist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抢单的助力榜单';
