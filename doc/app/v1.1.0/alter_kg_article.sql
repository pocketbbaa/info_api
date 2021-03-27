ALTER TABLE kg_article ADD  `article_img_size` tinyint(1) DEFAULT '1' COMMENT '1：小图  2：大图';
ALTER TABLE kg_article ADD INDEX audit_date ( `audit_date` );
UPDATE kg_article SET audit_date=update_date where  publish_status=1 and audit_date is null;