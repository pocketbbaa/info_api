DROP TABLE IF EXISTS `kg_channel`;
CREATE TABLE `kg_channel` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL COMMENT '渠道名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渠道';

-- ----------------------------
-- Records of kg_channel
-- ----------------------------
BEGIN;
INSERT INTO `kg_channel` VALUES (1, 'Android官方渠道');
INSERT INTO `kg_channel` VALUES (2, '小米应用市场');
INSERT INTO `kg_channel` VALUES (3, 'vivo应用市场');
INSERT INTO `kg_channel` VALUES (4, '搜狗应用市场');
INSERT INTO `kg_channel` VALUES (5, '360应用市场');
INSERT INTO `kg_channel` VALUES (6, '百度应用市场');
INSERT INTO `kg_channel` VALUES (7, '联想应用市场');
INSERT INTO `kg_channel` VALUES (8, '魅族应用市场');
INSERT INTO `kg_channel` VALUES (9, 'oppo应用市场');
INSERT INTO `kg_channel` VALUES (10, '其它应用市场');
INSERT INTO `kg_channel` VALUES (11, 'pp手机应用市场');
INSERT INTO `kg_channel` VALUES (12, '渠道1');
INSERT INTO `kg_channel` VALUES (13, '渠道2');
INSERT INTO `kg_channel` VALUES (14, '渠道3');
INSERT INTO `kg_channel` VALUES (15, '应用宝');
INSERT INTO `kg_channel` VALUES (16, '华为应用市场');
INSERT INTO `kg_channel` VALUES (17, 'IOS官方渠道');
INSERT INTO `kg_channel` VALUES (18, '马甲1');
INSERT INTO `kg_channel` VALUES (19, '马甲2');
INSERT INTO `kg_channel` VALUES (20, '马甲3');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE `kgweb`.`kg_user`
MODIFY COLUMN `register_origin` tinyint(1) NULL DEFAULT 3 COMMENT '1：IOS 2：ANDROID 3：WEB 4：H5 （BTC123 32 钛值 33）5：作者端 6：其他' AFTER `invite_code`;