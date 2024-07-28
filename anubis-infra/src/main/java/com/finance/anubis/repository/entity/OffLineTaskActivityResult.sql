CREATE TABLE offline_task_activity_result
(
    id               BIGINT(15)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    task_activity_id BIGINT(15)   NOT NULL DEFAULT 0 COMMENT '任务执行记录id',
    create_time      DATETIME     NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
    update_time      DATETIME     NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '更新时间',
    result_type      VARCHAR(50)  NOT NULL COMMENT '结果类型',
    compare_data     TEXT         NOT NULL COMMENT '对账数据',
    compare_keys     VARCHAR(255) NOT NULL COMMENT '比较keys',
    verify_result    VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '对账结果',
    biz_key          VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '业务主键',
    times            INT(8)       NOT NULL DEFAULT 1 COMMENT '当前执行次数',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT '离线对账执行结果';
