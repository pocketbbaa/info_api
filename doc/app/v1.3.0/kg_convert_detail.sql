/*
Navicat MySQL Data Transfer

Source Server         : mysql_开发
Source Server Version : 50721
Source Host           : 47.104.165.109:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-09-06 17:16:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_convert_detail
-- ----------------------------
DROP TABLE IF EXISTS `kg_convert_detail`;
CREATE TABLE `kg_convert_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人用户名',
  `user_id` bigint(50) NOT NULL COMMENT '用户id',
  `user_type` int(1) DEFAULT NULL COMMENT '用户类型',
  `user_nick` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人姓名',
  `update_time` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作时间',
  `update_info` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改信息',
  `operation_type` int(10) NOT NULL COMMENT '操作类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
