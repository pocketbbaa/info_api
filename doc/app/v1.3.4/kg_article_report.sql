/*
 Navicat Premium Data Transfer

 Source Server         : 47.104.165.109-
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 47.104.165.109:3306
 Source Schema         : kgweb

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 30/10/2018 15:07:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for kg_article_report
-- ----------------------------
DROP TABLE IF EXISTS `kg_article_report`;
CREATE TABLE `kg_article_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `article_id` bigint(20) NOT NULL COMMENT '资讯ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `report_id` bigint(20) NOT NULL COMMENT '报告内容',
  `state` tinyint(2) NOT NULL COMMENT '0：未处理，1：已处理',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_article_id` (`article_id`),
  KEY `index_user_id` (`user_id`),
  KEY `index_report_id` (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户举报表';

SET FOREIGN_KEY_CHECKS = 1;
