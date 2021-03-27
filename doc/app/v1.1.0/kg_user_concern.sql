SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `kg_user_concern`;
CREATE TABLE `kg_user_concern` (
  `concern_id` bigint(20) NOT NULL COMMENT '关注ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `concern_user_id` bigint(20) NOT NULL COMMENT '被关注人ID',
  `concern_status` int(1) NOT NULL COMMENT '关注状态  1关注 2互相关注',
  `notify_status` int(1) NOT NULL DEFAULT '0' COMMENT '是否已通知  0未通知  1已通知',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '关注时间',
  PRIMARY KEY (`concern_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户关注表';
