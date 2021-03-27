CREATE TABLE `kg_activity_competion` (
  `competion_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '赛事ID',
  `home_team_logo` varchar(255) DEFAULT NULL COMMENT '主场球队LOGO',
  `guest_team_logo` varchar(255) DEFAULT NULL COMMENT '客场球队LOGO',
  `home_team_name` varchar(255) DEFAULT NULL COMMENT '主场球队名',
  `home_team_id` int(25) DEFAULT NULL COMMENT '主场球队编号',
  `guest_team_name` varchar(255) DEFAULT NULL COMMENT '客场球队名',
  `guest_team_id` int(25) DEFAULT NULL COMMENT '客场球队编号',
  `competition_date` timestamp NULL DEFAULT NULL COMMENT '比赛时间',
  `home_team_panda` varchar(255) DEFAULT NULL COMMENT '主场球队熊猫图片地址',
  `guest_team_panda` varchar(255) DEFAULT NULL COMMENT '客场球队熊猫图片地址',
  PRIMARY KEY (`competion_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COMMENT='世界杯活动赛事表';

-- ----------------------------
-- Records of kg_activity_competion
-- ----------------------------
INSERT INTO `kg_activity_competion` VALUES ('1', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/eluosi@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/shate@3x.png', '俄罗斯', '1', '沙特', '25', '2018-06-14 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/eluosi_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/shate_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('2', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aiji@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/wulagui@3x.png', '埃及', '2', '乌拉圭', '17', '2018-06-15 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aiji_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/wulagui_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('3', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moluoge@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yilang@3x.png', '摩洛哥', '3', '伊朗', '18', '2018-06-15 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moluoge_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yilang_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('4', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/putaoya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/xibanya@3x.png', '葡萄牙', '4', '西班牙', '26', '2018-06-16 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/putaoya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/xibanya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('5', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/faguo@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aodaliya@3x.png', '法国', '5', '澳大利亚', '27', '2018-06-16 18:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/faguo_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aodaliya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('6', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/agenting@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bingdao@3x.png', '阿根廷', '6', '冰岛', '28', '2018-06-16 21:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/agenting_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bindao_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('7', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/milu@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/danmai@3x.png', '秘鲁', '7', '丹麦', '19', '2018-06-17 00:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/milu_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/danmai_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('8', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/keluodiya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/niriliya@3x.png', '克罗地亚', '8', '尼日利亚', '20', '2018-06-17 03:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/keluodiya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/niriliya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('9', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gesidalijia@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya1@3x.png', '哥斯达黎加', '9', '塞尔维亚', '21', '2018-06-17 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gesidalijia_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('10', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/deguo@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moxige@3x.png', '德国', '10', '墨西哥', '29', '2018-06-17 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/deguo_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moxige_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('11', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/baxi@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian1@3x.png', '巴西', '11', '瑞士', '30', '2018-06-18 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/baxi_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruishi_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('12', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/hanguo@3x.png', '瑞典', '12', '韩国', '22', '2018-06-18 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/hanguo_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('13', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bilishi@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/banama@3x.png', '比利时', '13', '巴拿马', '32', '2018-06-18 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bilishi_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/banama_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('14', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/tunisi@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yinggelan@3x.png', '突尼斯', '14', '英格兰', '23', '2018-06-19 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/tunisi_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yinggelan_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('15', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gelunbiya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/riben@3x.png', '哥伦比亚', '15', '日本', '24', '2018-06-19 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gelunbiya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/riben_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('16', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bolan@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya@3x.png', '波兰', '16', '塞内加尔', '31', '2018-06-19 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bolan_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saineijiaer_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('17', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/eluosi@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aiji@3x.png', '俄罗斯', '1', '埃及', '2', '2018-06-20 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/eluosi_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aiji_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('18', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/putaoya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moluoge@3x.png', '葡萄牙', '4', '摩洛哥', '3', '2018-06-20 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/putaoya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moluoge_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('19', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/wulagui@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/shate@3x.png', '乌拉圭', '17', '沙特', '25', '2018-06-20 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/wulagui_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/shate_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('20', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yilang@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/xibanya@3x.png', '伊朗', '18', '西班牙', '26', '2018-06-21 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yilang_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/xibanya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('21', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/danmai@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aodaliya@3x.png', '丹麦', '19', '澳大利亚', '27', '2018-06-21 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/danmai_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aodaliya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('22', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/faguo@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/milu@3x.png', '法国', '5', '秘鲁', '7', '2018-06-21 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/faguo_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/milu_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('23', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/agenting@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/keluodiya@3x.png', '阿根廷', '6', '克罗地亚', '8', '2018-06-22 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/agenting_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/keluodiya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('24', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/baxi@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gesidalijia@3x.png', '巴西', '11', '哥斯达黎加', '9', '2018-06-22 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/baxi_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gesidalijia_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('25', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/niriliya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bingdao@3x.png', '尼日利亚', '20', '冰岛', '28', '2018-06-22 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/niriliya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bindao_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('26', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya1@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian1@3x.png', '塞尔维亚', '21', '瑞士', '30', '2018-06-23 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruishi_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('27', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bilishi@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/tunisi@3x.png', '比利时', '13', '突尼斯', '14', '2018-06-23 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bilishi_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/tunisi_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('28', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/hanguo@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moxige@3x.png', '韩国', '22', '墨西哥', '29', '2018-06-23 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/hanguo_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moxige_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('29', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/deguo@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian@3x.png', '德国', '10', '瑞典', '12', '2018-06-24 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/deguo_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('30', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yinggelan@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/banama@3x.png', '英格兰', '23', '巴拿马', '32', '2018-06-24 20:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yinggelan_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/banama_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('31', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/riben@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya@3x.png', '日本', '24', '塞内加尔', '31', '2018-06-24 23:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/riben_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saineijiaer_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('32', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bolan@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gelunbiya@3x.png', '波兰', '16', '哥伦比亚', '15', '2018-06-25 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bolan_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gelunbiya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('33', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/wulagui@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/eluosi@3x.png', '乌拉圭', '17', '俄罗斯', '1', '2018-06-25 22:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/wulagui_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/eluosi_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('34', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/shate@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aiji@3x.png', '沙特', '25', '埃及', '2', '2018-06-25 22:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/shate_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aiji_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('35', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yilang@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/putaoya@3x.png', '伊朗', '18', '葡萄牙', '4', '2018-06-26 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yilang_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/putaoya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('36', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/xibanya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moluoge@3x.png', '西班牙', '26', '摩洛哥', '3', '2018-06-26 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/xibanya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moluoge_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('37', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/danmai@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/faguo@3x.png', '丹麦', '19', '法国', '5', '2018-06-26 22:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/danmai_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/faguo_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('38', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aodaliya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/milu@3x.png', '澳大利亚', '27', '秘鲁', '7', '2018-06-26 22:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/aodaliya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/milu_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('39', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/niriliya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/agenting@3x.png', '尼日利亚', '20', '阿根廷', '6', '2018-06-27 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/niriliya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/agenting_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('40', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bingdao@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/keluodiya@3x.png', '冰岛', '28', '克罗地亚', '8', '2018-06-27 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bindao_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/keluodiya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('41', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moxige@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian@3x.png', '墨西哥', '29', '瑞典', '12', '2018-06-27 22:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/moxige_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('42', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/hanguo@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/deguo@3x.png', '韩国', '22', '德国', '10', '2018-06-27 22:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/hanguo_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/deguo_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('43', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya1@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/baxi@3x.png', '塞尔维亚', '21', '巴西', '11', '2018-06-28 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/baxi_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('44', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruidian1@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gesidalijia@3x.png', '瑞士', '30', '哥斯达黎加', '9', '2018-06-28 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/ruishi_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gesidalijia_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('45', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saierweiya@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gelunbiya@3x.png', '塞内加尔', '31', '哥伦比亚', '15', '2018-06-28 22:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/saineijiaer_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/gelunbiya_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('46', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/riben@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bolan@3x.png', '日本', '24', '波兰', '16', '2018-06-28 22:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/riben_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bolan_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('47', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yinggelan@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bilishi@3x.png', '英格兰', '23', '比利时', '13', '2018-06-29 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/yinggelan_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/bilishi_panda@3x.png');
INSERT INTO `kg_activity_competion` VALUES ('48', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/banama@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/tunisi@3x.png', '巴拿马', '32', '突尼斯', '14', '2018-06-29 02:00:00', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/banama_panda@3x.png', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/tunisi_panda@3x.png');
