CREATE TABLE `kg_column_count` (
  `column_key` varchar(50) NOT NULL COMMENT '根据栏目生成的KEY（对应Redis的key）',
  `article_num` int(11) NOT NULL DEFAULT '0' COMMENT '新闻总量',
  PRIMARY KEY (`column_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='根据栏目ID统计其下的新闻总量';
