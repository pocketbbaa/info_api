ALTER TABLE kg_news_flash
  ADD `newsflash_origin` tinyint(1) DEFAULT '0' COMMENT '0：系统抓取 1：人工添加',
  ADD `create_user` bigint(20) DEFAULT NULL COMMENT '发布人',
  ADD `ifPush` tinyint(1) DEFAULT '0' COMMENT '是否已推送 0：未推送 1：已推送',
  ADD `level` tinyint(1) DEFAULT '0' COMMENT '重要级别 0：不重要 1：重要',
  ADD `newsflash_bottom_img` varchar(100) DEFAULT NULL COMMENT '快讯分享底图',
  ADD `display_status` tinyint(1) DEFAULT '1' COMMENT '显示状态 0：隐藏 1：显示',
  ADD `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  ADD `update_user` bigint(20) DEFAULT NULL COMMENT '更新人';
UPDATE kg_news_flash SET update_date=create_date;
alter table kg_news_flash modify column create_date timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';