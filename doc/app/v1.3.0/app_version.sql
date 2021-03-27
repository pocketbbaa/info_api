ALTER TABLE `kgweb`.`kg_app_version_manage`
ADD COLUMN `channel` varchar(200) NULL COMMENT '安卓渠道' AFTER `download_url_apk`;

UPDATE kg_app_version_manage
SET channel = 'defalut_channel'
WHERE
	system_type = 1