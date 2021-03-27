/*
Navicat MySQL Data Transfer

Source Server         : mysql_开发
Source Server Version : 50721
Source Host           : 47.104.165.109:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-08-03 10:31:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_rule
-- ----------------------------
DROP TABLE IF EXISTS `kg_rule`;
CREATE TABLE `kg_rule` (
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '映射名称 用于身份证验证的规则名',
  `rule` int(10) NOT NULL COMMENT '最大上限数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of kg_rule
-- ----------------------------
INSERT INTO `kg_rule` VALUES ('personage_column', '3');
INSERT INTO `kg_rule` VALUES ('tissue', '1');
