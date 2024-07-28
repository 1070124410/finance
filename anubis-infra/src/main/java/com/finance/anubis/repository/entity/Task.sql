DROP TABLE IF EXISTS task;
CREATE TABLE `task`
(
    `id`             bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time`    datetime    NOT NULL COMMENT '创建时间',
    `update_time`    datetime    NOT NULL COMMENT '更新时间',
    `task_config_id` bigint      NOT NULL COMMENT '配置id',
    `status`         varchar(50) NOT NULL COMMENT '任务状态',
    `type`           varchar(50) NOT NULL COMMENT '任务类型',
    PRIMARY KEY (`id`),
    UNIQUE KEY `task_pk` (`task_config_id`),
    KEY              `idx_task_status` (`status`),
    KEY              `idx_task_config_id` (`task_config_id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对账任务'