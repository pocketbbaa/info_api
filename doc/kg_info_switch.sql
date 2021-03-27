/*
Navicat MySQL Data Transfer

Source Server         : kg_new_develop
Source Server Version : 50721
Source Host           : 47.104.165.109:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-24 14:01:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_info_switch
-- ----------------------------
DROP TABLE IF EXISTS `kg_info_switch`;
CREATE TABLE `kg_info_switch` (
  `user_id` bigint(20) NOT NULL,
  `system_info_switch` tinyint(4) NOT NULL DEFAULT '1' COMMENT '系统消息推送开关',
  `comment_reply_switch` tinyint(4) NOT NULL DEFAULT '1' COMMENT '评论回复推送开关',
  `hot_news_switch` tinyint(4) NOT NULL DEFAULT '1' COMMENT '热点资讯推送开关',
  `newsflash_switch` tinyint(4) NOT NULL DEFAULT '1' COMMENT '快讯更新推送开关',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE kg_info_switch COMMENT='APP用户推送开关';