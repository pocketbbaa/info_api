ALTER TABLE kg_siteimage ADD  `ios_switch` tinyint(1) DEFAULT '1' COMMENT 'IOS的banner开关 0：关闭 1：打开';
ALTER TABLE kg_siteimage ADD  `android_switch` tinyint(1) DEFAULT '1' COMMENT 'Android的banner开关 0：关闭 1：打开';