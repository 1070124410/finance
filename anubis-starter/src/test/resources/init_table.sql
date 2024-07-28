-- auto Generated on 2023-01-10
DROP TABLE IF EXISTS task_activity;
CREATE TABLE task_activity
(
    id             bigint,
    biz_key        varchar(50),
    create_time    datetime,
    update_time    datetime,
    context        text,
    action     varchar(50),
    task_config_id bigint,
    times          int
);
-- -- auto Generated on 2023-01-05
-- DROP TABLE IF EXISTS task;
-- CREATE TABLE task
-- (
--     id             BIGINT (15) AUTO_INCREMENT COMMENT '主键',
--     create_time    DATETIME COMMENT '创建时间',
--     update_time    DATETIME COMMENT '更新时间',
--     task_config_id BIGINT (15) COMMENT '配置id',
--     `status`       VARCHAR(50) COMMENT '任务状态',
-- );
--
--
--
-- -- auto Generated on 2023-01-10
-- DROP TABLE IF EXISTS task_activity_result;
-- CREATE TABLE task_activity_result
-- (
--     id                  BIGINT (15) AUTO_INCREMENT COMMENT '主键',
--     task_activity_id    BIGINT (15) COMMENT '任务执行记录id',
--     create_time         DATETIME COMMENT '创建时间',
--     update_time         DATETIME COMMENT '更新时间',
--     compare_source_data TEXT COMMENT '用于比较的源数据',
--     compare_target_data TEXT COMMENT '用于比较的目标数据',
--     compare_keys        VARCHAR(255) COMMENT '比较keys',
--     variance_keys       VARCHAR(255) COMMENT '不一致的列',
--     action_result       VARCHAR(50) COMMENT '结果类型',
-- )COMMENT '对账执行结果';
--
-- -- auto Generated on 2023-01-05
-- DROP TABLE IF EXISTS task_config;
-- CREATE TABLE task_config
-- (
--     id            BIGINT(15) AUTO_INCREMENT COMMENT '主键',
--     create_time   DATETIME COMMENT '创建时间',
--     update_time   DATETIME COMMENT '更新时间',
--     source_config TEXT COMMENT '任务比对源数据配置',
--     target_config TEXT COMMENT '任务比对目标源配置',
--     compare_keys  VARCHAR(255) COMMENT '比对属性列表',
--     delay         INT(11) COMMENT '延迟时间（秒）',
--     unique_keys   VARCHAR(255) COMMENT '数据源唯一属性，compareKeys 子集',
--     `name`        VARCHAR(50) COMMENT '任务名称',
--     version       INT(11) COMMENT '配置版本',
-- );
--
-- drop table if exists offline_task_activity_result
-- CREATE TABLE offline_task_activity_result
-- (
--     id               BIGINT(15) AUTO_INCREMENT COMMENT '主键',
--     task_activity_id BIGINT(15) COMMENT '任务执行记录id',
--     create_time      DATETIME COMMENT '创建时间',
--     update_time      DATETIME COMMENT '更新时间',
--     result_type      VARCHAR(50) COMMENT '结果类型',
--     compare_data     TEXT COMMENT '对账数据',
--     compare_keys     VARCHAR(255) COMMENT '比较keys',
--     verify_result    VARCHAR(50) COMMENT '对账结果',
--     biz_key          VARCHAR(50) COMMENT '业务主键',
--     times            INT(8) COMMENT '当前执行次数',
-- ) COMMENT '离线对账执行结果';
