INSERT INTO `kg_common_setting` VALUES ('4', 'hotAuthor_info', '{"hotAuthorLocation": 3}', 'APP热门作者插入位置', '1');
INSERT INTO `kg_common_setting` VALUES ('5', 'push_newsFlash_limit', '{"pushNewsFlashLimit": 8}', '每天快讯推送上限', '1');
UPDATE `kg_common_setting` SET  `setting_value`='{"activityBg": "https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/cup/WorldCup_big.png", "activityKey": "worldcup_activity_date", "activityUrl": "http://kg.btc123.com/dist/app/millActivityIndex", "activityDesc": "", "activityTitle": "", "activityBtnText": "", "activitySmallIcon": "https://pro-kg-oss.oss-cn-beijing.aliyuncs.com/worldcup/cup/active.png"}', `setting_desc`='APP活动弹窗信息。', `setting_status`='1' WHERE (`setting_key`='activity_pop_info');
INSERT INTO `kg_common_setting` (`setting_id`, `setting_key`, `setting_value`, `setting_desc`, `setting_status`) VALUES ('8', 'mine_banner_info', '[{"mineBannerImg": "http://p0.so.qhimgs1.com/t015e3568e6b6e39980.jpg", "mineBannerLink": "https://www.baidu.com", "mineBannerType": 2}]', 'APP个人中心Banner信息（mineBannerType:1：资讯 2：广告 3：其他 4：活动）', '1');