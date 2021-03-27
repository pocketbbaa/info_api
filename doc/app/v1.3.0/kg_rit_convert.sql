/*
Navicat MySQL Data Transfer

Source Server         : mysql_开发
Source Server Version : 50721
Source Host           : 47.104.165.109:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-09-28 11:32:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_rit_convert
-- ----------------------------
DROP TABLE IF EXISTS `kg_rit_convert`;
CREATE TABLE `kg_rit_convert` (
  `id` bigint(200) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型 0 普通用户 1 专栏用户',
  `rit_rate` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'rit于氪金的汇率',
  `day_limit` bigint(50) DEFAULT NULL COMMENT '每日兑换限制 额度',
  `next_day_limit` bigint(50) DEFAULT NULL COMMENT '待更新每日兑换额度',
  `next_day_cnt` int(50) DEFAULT NULL COMMENT '待更新每次兑换次数',
  `day_cnt` int(5) DEFAULT NULL COMMENT '每日兑换次数',
  `push_time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '每日刷新额度时间',
  `next_push_time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '次日额度刷新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of kg_rit_convert
-- ----------------------------
INSERT INTO `kg_rit_convert` VALUES ('0', '0', '140-1', '10000', '10000', '1', '1', '2018-09-28 10:00:00', '2018-09-28 10:00:00');
INSERT INTO `kg_rit_convert` VALUES ('1', '1', '150-1', '8000', '8000', '1', '1', '2018-09-28 10:00:00', '2018-09-28 10:00:00');
