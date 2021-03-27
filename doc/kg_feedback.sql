--修改意见反馈表
ALTER TABLE `kg_feedback`
MODIFY COLUMN `feedback_email` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '反馈邮箱' AFTER `feedback_detail`,
ADD COLUMN `feedback_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '反馈意见类型1：功能建议，2：内容建议，3：体验建议' AFTER `update_user`,
ADD COLUMN `from_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '来源 1：web ，2：app' AFTER `feedback_type`;

ALTER TABLE kg_feedback ADD INDEX IDX_FEEDBACK_STATUS (feedback_status);
ALTER TABLE kg_feedback ADD INDEX IDX_FEEDBACK_TYPE (feedback_type);
ALTER TABLE kg_feedback ADD INDEX IDX_FROM_TYPE (from_type);