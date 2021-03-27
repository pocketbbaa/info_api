/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50719
Source Host           : 172.16.0.17:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-05-19 11:21:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_extra_bonus_detail
-- ----------------------------
DROP TABLE IF EXISTS `kg_extra_bonus_detail`;
CREATE TABLE `kg_extra_bonus_detail` (
  `bonus_detail_id` bigint(20) NOT NULL COMMENT '发放奖励详细ID',
  `extra_bonus_id` bigint(20) NOT NULL COMMENT '新增奖励发放ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `tv_amount` decimal(20,8) DEFAULT NULL COMMENT '钛值奖励',
  `kg_amount` decimal(20,8) DEFAULT NULL COMMENT '氪金奖励',
  `tv_account_flow_id` bigint(20) DEFAULT NULL COMMENT '钛值流水ID',
  `kg_account_flow_id` bigint(20) DEFAULT NULL COMMENT '氪金流水ID',
  PRIMARY KEY (`bonus_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新增奖励详细表';
