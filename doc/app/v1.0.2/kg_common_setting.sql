CREATE TABLE `kg_common_setting` (
  `setting_id` int(10) NOT NULL COMMENT '配置ID',
  `setting_key` varchar(30) DEFAULT NULL COMMENT '配置的key，通过key取相关配置信息',
  `setting_value` json DEFAULT NULL COMMENT '配置信息（配置值）',
  `setting_desc` varchar(100) DEFAULT NULL COMMENT '配置的描述',
  `setting_status` tinyint(1) DEFAULT '1' COMMENT '配置开关状态 0：关闭 1：开启',
  PRIMARY KEY (`setting_id`),
  UNIQUE KEY `setting_key` (`setting_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局配置表';

INSERT INTO `kg_common_setting` VALUES ('1', 'push_article_limit', '{"pushArticleLimit": 8}', '每天文章推送上限', '1');
INSERT INTO `kg_common_setting` VALUES ('2', 'miner_activity_date', '{"endTime": "2018-06-25 18:00:00", "startTime": "2018-06-05 10:00:00"}', '矿机活动开始时间和结束时间', '1');
INSERT INTO `kg_common_setting` VALUES ('3', 'activity_pop_info', '{"activityUrl": "https://h5.kg.com/views/millActivityIndex"}', 'APP活动弹窗信息', '1');