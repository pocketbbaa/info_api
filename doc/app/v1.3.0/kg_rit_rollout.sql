/*
Navicat MySQL Data Transfer

Source Server         : mysql_开发
Source Server Version : 50721
Source Host           : 47.104.165.109:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-09-28 11:33:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_rit_rollout
-- ----------------------------
DROP TABLE IF EXISTS `kg_rit_rollout`;
CREATE TABLE `kg_rit_rollout` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_type` int(1) NOT NULL COMMENT '用户类型 0：普通 1：专栏',
  `day_limit` bigint(100) DEFAULT NULL COMMENT '每日兑换限制 额度',
  `month_limit` bigint(100) DEFAULT NULL COMMENT '每月可兑换额度',
  `rate` float(10,5) DEFAULT NULL COMMENT '汇率',
  `min_amount` bigint(100) DEFAULT NULL COMMENT '最低转出',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of kg_rit_rollout
-- ----------------------------
INSERT INTO `kg_rit_rollout` VALUES ('1', '1', '300', '3000', '0.2', '20');
INSERT INTO `kg_rit_rollout` VALUES ('2', '0', '200', '2000', '0.2', '20');
