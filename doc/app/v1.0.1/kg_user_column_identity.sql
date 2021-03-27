--用户专栏身份

DROP TABLE IF EXISTS `kg_user_column_identity`;
CREATE TABLE `kg_user_column_identity` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `identity` varchar(200) NOT NULL COMMENT '身份',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='用户专栏身份';


INSERT INTO `kg_user_column_identity` VALUES (1, '官方专栏');
INSERT INTO `kg_user_column_identity` VALUES (2, '特约作者');
INSERT INTO `kg_user_column_identity` VALUES (3, '媒体部编辑');
INSERT INTO `kg_user_column_identity` VALUES (4, '专家');
INSERT INTO `kg_user_column_identity` VALUES (5, '大咖');
INSERT INTO `kg_user_column_identity` VALUES (6, '签约自媒体');
INSERT INTO `kg_user_column_identity` VALUES (7, '媒体人');
INSERT INTO `kg_user_column_identity` VALUES (8, '资深媒体人');
INSERT INTO `kg_user_column_identity` VALUES (9, '自由撰稿人');
INSERT INTO `kg_user_column_identity` VALUES (10, '记者');
INSERT INTO `kg_user_column_identity` VALUES (11, '签约记者');

