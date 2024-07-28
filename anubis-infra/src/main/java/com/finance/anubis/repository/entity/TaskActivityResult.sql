CREATE TABLE `task_activity_result` (
                                        `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                        `task_activity_id` bigint(15) NOT NULL DEFAULT '0' COMMENT '任务执行记录id',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `compare_source_data` text COMMENT '用于比较的源数据',
                                        `compare_target_data` text COMMENT '用于比较的目标数据',
                                        `compare_keys` varchar(255) NOT NULL DEFAULT '[]' COMMENT '比较keys',
                                        `variance_keys` varchar(255) NOT NULL DEFAULT '[]' COMMENT '不一致的列',
                                        `action_result` varchar(50) NOT NULL DEFAULT '' COMMENT '结果类型',
                                        `biz_key` varchar(50) NOT NULL DEFAULT '' COMMENT '业务主键',
                                        `times` int(8) NOT NULL DEFAULT '1' COMMENT '当前执行次数',
                                        PRIMARY KEY (`id`),
                                        KEY `idx__biz_key` (`biz_key`),
                                        KEY `idx_task_activity_id` (`task_activity_id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对账执行结果'

