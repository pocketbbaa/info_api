/*
Navicat MySQL Data Transfer

Source Server         : kg_new_develop
Source Server Version : 50721
Source Host           : 47.104.165.109:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-21 10:36:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_mobile_img
-- ----------------------------
DROP TABLE IF EXISTS `kg_mobile_img`;
CREATE TABLE `kg_mobile_img` (
  `img_id` int(5) NOT NULL AUTO_INCREMENT,
  `normal_img` varchar(30) DEFAULT NULL COMMENT '每个导航正常状态图片',
  `choise_img` varchar(30) DEFAULT NULL COMMENT '每个导航点击后的图片',
  `is_del` int(4) DEFAULT '0' COMMENT '0：正常 1：删除',
  `orderby` int(5) DEFAULT NULL COMMENT '排序 可依次表示导航按钮位置',
  `remark` varchar(10) DEFAULT '' COMMENT '预留字段',
  PRIMARY KEY (`img_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
ALTER TABLE kg_mobile_img COMMENT='APP底部导航按钮图片';