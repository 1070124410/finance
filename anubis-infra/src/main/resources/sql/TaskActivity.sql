-- auto Generated on 2023-01-10
-- DROP TABLE IF EXISTS task_activity;
CREATE TABLE task_activity
(
    id             BIGINT(15) NOT NULL AUTO_INCREMENT COMMENT '主键',
    biz_key        VARCHAR(50) NOT NULL DEFAULT '' COMMENT '业务主键',
    create_time    DATETIME    NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
    update_time    DATETIME    NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '更新时间',
    context        TEXT        NOT NULL COMMENT '比对任务上下文',
    `action`       VARCHAR(50) NOT NULL DEFAULT '' COMMENT '下一活动行为',
    task_config_id BIGINT(15) NOT NULL DEFAULT 0 COMMENT '任务配置id',
    times          INT(8) NOT NULL DEFAULT 1 COMMENT '当前执行次数',
    unique INDEX          `idx_biz_key` (biz_key),
    INDEX          `idx_task_config_id` (task_config_id desc),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT '对账执行记录';
