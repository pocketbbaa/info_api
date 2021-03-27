DROP TABLE IF EXISTS `kg_coin`;
CREATE TABLE `kg_coin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL COMMENT '币种类型 1：TV，2：KG，3：RIT',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '币种名字',
  `name_cn` varchar(20) NOT NULL DEFAULT '' COMMENT '币种名字(中文)',
  `background` varchar(100) NOT NULL DEFAULT '' COMMENT '币种背景图',
  `intro` varchar(500) NOT NULL DEFAULT '' COMMENT '简介',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='币种信息';

-- ----------------------------
-- Records of kg_coin
-- ----------------------------
BEGIN;
INSERT INTO `kg_coin` VALUES (1, 1, 'TV', '钛值', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/17766/482127656708612096.png', '钛链（Ti-Blockchain），致力于发展现有区块链之外的公有链生态，将结合以太坊生态与storj优点，解决现有金融问题和大量网络储存闲置问题。');
INSERT INTO `kg_coin` VALUES (2, 2, 'KG', '氪金', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/17766/482127303246225408.png', '氪金又名KG，是千氪财经平台新推出的用户任务积分，可以直接兑换成RIT。');
INSERT INTO `kg_coin` VALUES (3, 3, 'RIT', 'RIT', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/17766/482127544150269952.png', 'RIGHT新闻平台通过整合区块链及其他互联网领域的媒体资源，通过打造产业生态系统，建立日趋完善的服务标准，为生态中的各个节点及广大用户带来更具洞见、更有价值、更为有效的信息传播服务。');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;