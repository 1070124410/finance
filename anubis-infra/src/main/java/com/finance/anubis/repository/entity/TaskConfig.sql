CREATE TABLE `task_config`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time`     datetime     NOT NULL COMMENT '创建时间',
    `update_time`     datetime     NOT NULL COMMENT '更新时间',
    `source_config`   text         NOT NULL COMMENT '任务比对源数据配置',
    `target_config`   text         NOT NULL COMMENT '任务比对目标源配置',
    `error_threshold` int          NOT NULL COMMENT '对细账允许的错误阈值',
    `detail_switch`   tinyint(1) NOT NULL COMMENT '细账开关',
    `compare_keys`    varchar(255) NOT NULL COMMENT '比对属性列表',
    `delay`           int          NOT NULL DEFAULT '0' COMMENT '延迟时间（秒）',
    `retry_time`      int          NOT NULL DEFAULT '0' COMMENT '错误重试次数',
    `unique_keys`     varchar(255) NOT NULL DEFAULT '' COMMENT '数据源唯一属性，compareKeys 子集',
    `name`            varchar(50)  NOT NULL DEFAULT '' COMMENT '任务名称',
    `version`         int          NOT NULL DEFAULT '1' COMMENT '配置版本',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对账配置'

