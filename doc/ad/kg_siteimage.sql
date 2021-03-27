alter TABLE kg_siteimage add (
	`display_port` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '显示端口  1 千氪WEB 2 千氪APP 3 千氪专栏WEB',
	`adver_style` TINYINT(1) NOT NULL DEFAULT '2' COMMENT '广告样式 1 信息流 2 图片广告',
	`adver_img_type` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '广告图片类型 1 单张小图 2 单张大图 3 三张小图',
	`adver_title` VARCHAR(200) NULL DEFAULT NULL COMMENT '广告标题',
	`adver_intro` VARCHAR(200) NULL DEFAULT NULL COMMENT '广告简介',
	`adver_link` VARCHAR(500) NULL DEFAULT NULL COMMENT '广告链接',
	`adver_owner` VARCHAR(50) NULL DEFAULT NULL COMMENT '广告主名称',
	`adver_target` VARCHAR(100) NULL DEFAULT NULL COMMENT '定向设置',
	`spread_time` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '推广时段 1 全部 2 自定义',
	`spread_time_s` TIMESTAMP NULL DEFAULT NULL COMMENT '推广时段开始',
	`spread_time_e` TIMESTAMP NULL DEFAULT NULL COMMENT '推广时段结束'
)
