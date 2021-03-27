delete from kg_sys_menu where menu_id=11;

INSERT INTO `kg_sys_menu` (`menu_id`, `menu_name`, `menu_link`, `menu_icon`, `menu_type`, `prev_menu`, `menu_order`, `menu_status`, `create_user`, `create_date`, `update_user`, `update_date`) VALUES (11, '广告管理', 'advert/list', NULL, 'sys', 39, 41, 1, NULL, '2017-11-15 16:11:15', NULL, '2017-11-15 16:11:15');
INSERT INTO `kg_sys_menu` (`menu_id`, `menu_name`, `menu_link`, `menu_icon`, `menu_type`, `prev_menu`, `menu_order`, `menu_status`, `create_user`, `create_date`, `update_user`, `update_date`) VALUES (39, '广告中心', NULL, 'picture', 'sys', 0, 81, 1, NULL, '2017-11-15 16:05:24', NULL, '2017-11-15 16:05:24');

