CREATE TABLE `kg_article_extend` (
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `if_into_index` tinyint(1) DEFAULT '0' COMMENT '是否展示到首页推荐列表 0：否 1：是',
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;