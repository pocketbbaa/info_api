CREATE TABLE `kg_app_version_manage` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`version_num` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '版本号',
	`prompt` TEXT NULL COMMENT '更新提示语',
	`forced` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '是否强制更新 0：否，1：是',
	`system_type` TINYINT(1) NOT NULL DEFAULT '1' COMMENT '操作系统 1：android。2：ios',
	`download_url` VARCHAR(200) NULL DEFAULT '' COMMENT '下载地址',
	`create_time` TIMESTAMP NULL DEFAULT NULL COMMENT '创建时间',
	`update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
	PRIMARY KEY (`id`),
	INDEX `forced_index` (`forced`),
	INDEX `system_type_index` (`system_type`),
	INDEX `time_index` (`update_time`)
)
COMMENT='APP版本管理'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=183
;
