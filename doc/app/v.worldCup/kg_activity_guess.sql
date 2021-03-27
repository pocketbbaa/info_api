CREATE TABLE `kg_activity_guess` (
  `guess_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '竞猜ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `guess_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '竞猜时间',
  `competion_id` int(11) NOT NULL COMMENT '赛事ID',
  `support_team_id` int(11) NOT NULL COMMENT '支持的球队ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '预留字段',
  PRIMARY KEY (`guess_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='世界杯活动竞猜表';
ALTER TABLE `kg_activity_guess` ADD INDEX competion_id ( `competion_id` );