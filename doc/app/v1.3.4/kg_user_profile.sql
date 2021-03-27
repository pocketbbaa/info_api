ALTER TABLE `kgweb`.`kg_user_profile`
ADD COLUMN `column_level` tinyint(2) NULL DEFAULT 0 COMMENT '专栏等级 0：见习作者 1：一星作者 2：二星作者 3：三星作者 4：四星作者 5：五星作者' AFTER `regist_channel`;



update kg_user_profile set column_level = 1 where user_id in (select user_id from kg_user where user_role != 1)