/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50719
Source Host           : 172.16.0.17:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-05-19 11:21:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_extra_bonus
-- ----------------------------
DROP TABLE IF EXISTS `kg_extra_bonus`;
CREATE TABLE `kg_extra_bonus` (
  `extra_bonus_id` bigint(20) NOT NULL,
  `total_tv_amount` decimal(20,8) DEFAULT NULL COMMENT '钛值奖励总额',
  `total_kg_amount` decimal(20,8) DEFAULT NULL COMMENT '氪金奖励总额',
  `bonus_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '奖励原因',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `sys_user_id` int(11) NOT NULL COMMENT '管理员ID',
  `total_num` int(4) NOT NULL COMMENT '奖励总人数',
  PRIMARY KEY (`extra_bonus_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新增奖励表';
