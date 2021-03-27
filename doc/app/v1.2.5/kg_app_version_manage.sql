ALTER TABLE `kgweb`.`kg_app_version_manage`
MODIFY COLUMN `download_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '页面下载地址' AFTER `system_type`,
ADD COLUMN `download_url_apk` varchar(200) NULL COMMENT 'apk下载地址' AFTER `download_url`;