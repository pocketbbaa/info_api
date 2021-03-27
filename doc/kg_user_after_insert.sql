DELIMITER //
CREATE TRIGGER `kg_user_after_insert` AFTER INSERT ON `kg_user` FOR EACH ROW BEGIN IF(NEW.user_id NOT IN 
 (
SELECT user_id
FROM kg_user_active)) THEN
INSERT INTO kg_user_active (user_id) VALUES (new.user_id);
 END IF; 
 
 
 
IF(NEW.user_id NOT IN 
 (
SELECT user_id
FROM kg_user_profile)) THEN
INSERT INTO kg_user_profile (user_id,role_id) VALUES (new.user_id,new.user_role);
 END IF; 
 
 
 END//
DELIMITER ;