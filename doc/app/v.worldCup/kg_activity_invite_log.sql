/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50719
Source Host           : 172.16.0.17:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-06-12 17:38:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_activity_invite_log
-- ----------------------------
DROP TABLE IF EXISTS `kg_activity_invite_log`;
CREATE TABLE `kg_activity_invite_log` (
  `invite_id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `rel_user_id` bigint(20) NOT NULL COMMENT '被邀请人ID',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态  0未使用 1已使用',
  `activity_id` tinyint(5) NOT NULL COMMENT '活动ID  4世界杯活动',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`invite_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动邀新记录关系';
