/*
Navicat MySQL Data Transfer

Source Server         : mysql_开发
Source Server Version : 50721
Source Host           : 47.104.165.109:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-09-06 17:20:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_rit_freeze_log
-- ----------------------------
DROP TABLE IF EXISTS `kg_rit_freeze_log`;
CREATE TABLE `kg_rit_freeze_log` (
  `id` bigint(20) NOT NULL,
  `oper_user_id` bigint(20) DEFAULT NULL COMMENT '操作者userId',
  `user_id` bigint(20) DEFAULT NULL COMMENT '被冻结/解冻用户id',
  `freeze_cnt` bigint(50) DEFAULT NULL COMMENT '冻结数量',
  `type` int(1) DEFAULT NULL COMMENT '操作类型 0冻结 1解冻',
  `cause` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
