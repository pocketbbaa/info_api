INSERT INTO `kg_sys_menu` (`menu_id`, `menu_name`, `menu_link`, `menu_icon`, `menu_type`, `prev_menu`, `menu_order`, `menu_status`, `create_user`, `create_date`, `update_user`, `update_date`) VALUES (34, 'APP管理', NULL, 'mobile', 'sys', 0, 71, 1, NULL, '2017-11-15 16:05:24', NULL, '2017-11-15 16:05:24');
INSERT INTO `kg_sys_menu` (`menu_id`, `menu_name`, `menu_link`, `menu_icon`, `menu_type`, `prev_menu`, `menu_order`, `menu_status`, `create_user`, `create_date`, `update_user`, `update_date`) VALUES (35, 'APP版本管理', 'app/editon', '', 'sys', 34, 11, 1, NULL, '2017-11-15 16:05:24', NULL, '2017-11-15 16:05:24');



INSERT INTO `kg_sys_auth` (`sys_auth_id`, `sys_auth_name`, `menu_id`, `prv_auth_id`, `operation_type`) VALUES (34, 'APP管理', 34, 0, '');
INSERT INTO `kg_sys_auth` (`sys_auth_id`, `sys_auth_name`, `menu_id`, `prv_auth_id`, `operation_type`) VALUES (35, 'APP版本管理', 35, 34, '');
