ALTER TABLE kg_account
ADD COLUMN `rit_balance` decimal(20,8) NOT NULL DEFAULT '0.00000000'  COMMENT 'rit余额' ,
ADD COLUMN `rit_frozen_balance` decimal(20,8) NOT NULL DEFAULT '0.00000000'  COMMENT 'rit冻结余额';