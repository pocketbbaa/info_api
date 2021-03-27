ALTER TABLE kg_article ADD  `if_push` tinyint(1) DEFAULT '1' COMMENT '是否向APP推送 0：否 1：是',
  ADD `if_platform_publish_award` tinyint(1) DEFAULT '1' COMMENT '是否发放平台发文奖励 0：否 1：是';