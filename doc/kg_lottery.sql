/*
Navicat MySQL Data Transfer

Source Server         : kg
Source Server Version : 50721
Source Host           : 47.104.165.109:3306
Source Database       : kgweb

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-04-24 21:54:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for kg_lottery
-- ----------------------------
DROP TABLE IF EXISTS `kg_lottery`;
CREATE TABLE `kg_lottery` (
  `lottery_id` bigint(20) NOT NULL COMMENT '奖励id ',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `prize_id` int(4) NOT NULL DEFAULT '1' COMMENT '奖项ID 0:BTC纪念币  1:谢谢惠顾 2:手环',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `activity_id` int(4) NOT NULL DEFAULT '0' COMMENT '活动ID  1周杰伦活动',
  `lottery_status` int(1) NOT NULL DEFAULT '0' COMMENT '领取状态  0 未中奖 1未领取  2已发放  ',
  `recieve_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '领取时间',
  PRIMARY KEY (`lottery_id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户中奖信息表';
