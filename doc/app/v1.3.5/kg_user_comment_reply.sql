
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for kg_user_comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `kg_user_comment_reply`;
CREATE TABLE `kg_user_comment_reply` (
  `comment_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '评论用户',
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `parent_comment_id` bigint(20) NOT NULL COMMENT '主评论ID',
  `to_user_id` bigint(20) DEFAULT NULL COMMENT '被评论用户ID',
  `comment_desc` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评论内容',
  `comment_date` timestamp NULL DEFAULT NULL COMMENT '评论时间',
  `comment_status` tinyint(2) DEFAULT NULL COMMENT '0 待审核 1 已审核（无需审核） 2 审核拒绝',
  `refuse_reason` varchar(500) DEFAULT NULL COMMENT '审核拒绝原因',
  `display_status` tinyint(2) DEFAULT NULL COMMENT '0 隐藏 1 显示 2 删除',
  PRIMARY KEY (`comment_id`),
  KEY `INDEX_USER_ID` (`user_id`),
  KEY `INDEX_ARTICLE_ID` (`article_id`),
  KEY `INDEX_P_ID` (`parent_comment_id`),
  KEY `INDEX_TU_ID` (`to_user_id`),
  KEY `INDEX_STATE` (`display_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='子回复';

SET FOREIGN_KEY_CHECKS = 1;
