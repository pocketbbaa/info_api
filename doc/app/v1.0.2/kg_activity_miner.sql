CREATE TABLE `kg_activity_miner` (
  `miner_id` bigint(20) NOT NULL COMMENT '矿机ID',
  `miner_name` varchar(250) DEFAULT NULL COMMENT '矿机名',
  `miner_price` decimal(10,0) DEFAULT NULL COMMENT '矿机价格',
  `miner_assist_frequency` int(10) DEFAULT NULL COMMENT '需助力人数（不包括自己）',
  `miner_join_frequency` int(10) DEFAULT NULL COMMENT '参与人数',
  `miner_number` int(5) DEFAULT NULL COMMENT '剩余数量',
  `miner_start_date` timestamp NULL DEFAULT NULL COMMENT '活动开始时间',
  `miner_end_date` timestamp NULL DEFAULT NULL COMMENT '活动结束时间',
  `miner_desc` varchar(255) DEFAULT NULL COMMENT '矿机简介',
  `miner_link` varchar(500) DEFAULT NULL COMMENT '矿机购买链接',
  `miner_photo` varchar(250) DEFAULT NULL COMMENT '矿机主图地址',
  PRIMARY KEY (`miner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='矿机列表';

INSERT INTO `kg_activity_miner` VALUES ('446732081771454464', '熊猫矿机 PandaMiner B3 Pro ETH/ETC/XMR/ZEC等多种GPU可支持算法币种', '16800.00', '1700', '583', '1', '2018-06-05 10:00:00', '2018-06-25 18:00:00', '专业以太币显卡矿机，支持ETH/ETC/ZEC/XMR多币种挖矿。本批次支持托管。', 'https://www.pandaminer.com/default/product_detail?id=30###', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/20180518_active/%E7%86%8A%E7%8C%AB%E7%9F%BF%E6%9C%BA.jpg');
INSERT INTO `kg_activity_miner` VALUES ('446732081771454465', 'K矿机 kcchain', '12888.00', '1250', '422', '3', '2018-06-05 10:00:00', '2018-06-25 18:00:00', 'KC Chain项目合作方虚拟加密数字货币矿厂建设产品之一，也可以用来购买云算力。', '', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/20180518_active/k%E7%9F%BF%E6%9C%BA.jpg');
INSERT INTO `kg_activity_miner` VALUES ('446732081771454466', 'Ledger  Nano S', '998.00', '100', '376', '2', '2018-06-05 10:00:00', '2018-06-20 18:00:00', 'Ledger创建于2014年，是加密货币和区块链应用的安全与基础设施解决方案的领导者。Ledger Nano S发布于2016年8月份，目前是市场认可度最高的硬件钱包产品之一。', 'https://item.taobao.com/item.htm?spm=a1z10.1-c-s.w15750614-18185511682.33.7f2e1d3a3ftAbD&id=546533080574', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/20180518_active/Ledger%20Nano%20S.jpg');
INSERT INTO `kg_activity_miner` VALUES ('446732081771454467', 'Bepal Pro S 全面支持EOS、BTM主网生态', '3280.00', '380', '355', '7', '2018-06-05 10:00:00', '2018-06-20 18:00:00', '全新的Bepal Pro S支持简中、繁中、英、日、韩5种语言，全面支持EOS、BTM主网生态，EOS一键映射和快捷投票功能，拥有更人性化的UI设计，更流畅的硬件操作体验。', 'https://m.bepal.pro/bepal-pro-s', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/20180518_active/Bepal%20Pro%20S.jpg');
INSERT INTO `kg_activity_miner` VALUES ('446732081771454469', 'Trezor捷克全球第一款硬件钱包', '998.00', '100', '291', '3', '2018-06-05 10:00:00', '2018-06-20 18:00:00', '1、 TREZOR 网页端需要配合设备打开，用于查看交易，发送和接收数字货币，查看交易记录等；\r\n3、设备和网页钱包的信息使用USB传输', 'https://item.taobao.com/item.htm?spm=a1z10.1-c-s.w15750614-18185511682.12.72951d3abRApw7&id=548622649016', 'https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/20180518_active/Trezor.jpg');
