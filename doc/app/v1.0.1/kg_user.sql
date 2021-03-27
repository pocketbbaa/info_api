ALTER TABLE `kgweb`.`kg_user`
ADD COLUMN `column_authed` tinyint(1) NOT NULL DEFAULT 0 COMMENT '专栏是否认证 0  未认证 1 已认证' AFTER `register_origin`;

ALTER TABLE `kgweb`.`kg_user_profile`
ADD COLUMN `column_identity` varchar(200) NULL COMMENT '专栏身份' AFTER `site_link`;