ALTER TABLE `kgweb`.`kg_app_version_manage`
ADD COLUMN `channel` varchar(200) NULL COMMENT '安卓渠道' AFTER `update_time`;