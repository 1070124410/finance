package com.finance.anubis.utils;

import com.zaxxer.hikari.HikariDataSource;
import groovy.sql.Sql;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class MapperUtil {

    private static final String MAPPER_CONFIG_LOCATIONS = "mapper-config.xml";

    /**
     * 通过MyBatis的SqlSession启动mapper实例
     *
     * @param type
     * @param dataSource
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T getMapper(Class<T> type, DataSource dataSource) throws IOException {

        // 获取资源
        InputStream inputStream = Resources.getResourceAsStream(MAPPER_CONFIG_LOCATIONS);
        JdbcTransactionFactory jdbcTransactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", jdbcTransactionFactory, dataSource);

        // 启动SqlSessionFactory获取mapper
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSessionFactory.getConfiguration().setEnvironment(environment);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.getMapper(type);
    }

    public static DataSource inMemoryDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("org.h2.Driver");
        hikariDataSource.setJdbcUrl("jdbc:h2:mem:consist_platform;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL;DATABASE_TO_UPPER=false;DEFAULT_LOCK_TIMEOUT=10000;LOCK_MODE=0");
//        hikariDataSource.setUsername("root");
//        hikariDataSource.setPassword("test");
        return hikariDataSource;
    }

    public static void createAppMainTable(javax.sql.DataSource dataSource) {
        try {
            new Sql(dataSource).execute("-- auto Generated on 2023-01-05\n" +
                    // "DROP TABLE IF EXISTS task;\n" +
                    "CREATE TABLE IF NOT EXISTS task(\n" +
                    "                     id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "                     create_time DATETIME NOT NULL COMMENT '创建时间',\n" +
                    "                     update_time DATETIME NOT NULL COMMENT '更新时间',\n" +
                    "                     task_config_id BIGINT (15) NOT NULL COMMENT '配置id',\n" +
                    "                     `status` VARCHAR(50) NOT NULL COMMENT '任务状态',\n" +
                    "                     `type` VARCHAR(50) NOT NULL COMMENT '任务类型',\n" +
                    "                     INDEX `idx_task_config_id`(task_config_id),\n" +
                    "                     PRIMARY KEY (id)\n" +
                    ")DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci  COMMENT 'task';\n" +
                    "\n" +
                    "-- auto Generated on 2023-01-10\n" +
                    // "DROP TABLE IF EXISTS task_activity;\n" +
                    "CREATE TABLE IF NOT EXISTS task_activity\n" +
                    "(\n" +
                    "    id             BIGINT(15) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "    biz_key        VARCHAR(50) NOT NULL DEFAULT '' COMMENT '业务主键',\n" +
                    "    create_time    DATETIME    NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',\n" +
                    "    update_time    DATETIME    NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '更新时间',\n" +
                    "    context        TEXT        NOT NULL COMMENT '比对任务上下文',\n" +
                    "    `action`       VARCHAR(50) NOT NULL DEFAULT '' COMMENT '下一活动行为',\n" +
                    "    task_config_id BIGINT(15) NOT NULL DEFAULT 0 COMMENT '任务配置id',\n" +
                    "    times          INT(8) NOT NULL DEFAULT 1 COMMENT '当前执行次数',\n" +
                    "    INDEX          `idx_biz_key` (biz_key),\n" +
                    "--     INDEX          `idx_task_config_id` (task_config_id),\n" +
                    "    PRIMARY KEY (id)\n" +
                    ") \n" +
                    "  DEFAULT CHARSET = utf8mb4\n" +
                    "  COLLATE = utf8mb4_0900_ai_ci COMMENT '对账执行记录';\n" +
                    "\n" +
                    "-- auto Generated on 2023-01-10\n" +
                    // "DROP TABLE IF EXISTS task_activity_result;\n" +
                    "CREATE TABLE IF NOT EXISTS task_activity_result(\n" +
                    "                                     id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "                                     task_activity_id BIGINT (15) NOT NULL DEFAULT 0 COMMENT '任务执行记录id',\n" +
                    "                                     create_time DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',\n" +
                    "                                     update_time DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '更新时间',\n" +
                    "                                     compare_source_data TEXT NOT NULL COMMENT '用于比较的源数据',\n" +
                    "                                     compare_target_data TEXT NOT NULL COMMENT '用于比较的目标数据',\n" +
                    "                                     compare_keys VARCHAR (255) NOT NULL DEFAULT '[]' COMMENT '比较keys',\n" +
                    "                                     variance_keys VARCHAR (255) NOT NULL DEFAULT '[]' COMMENT '不一致的列',\n" +
                    "                                     action_result VARCHAR (50) NOT NULL DEFAULT '' COMMENT '结果类型',\n" +
                    "                                     biz_key VARCHAR (50) NOT NULL DEFAULT '' COMMENT '业务主键',\n" +
                    "                                     times INT NOT NULL DEFAULT '1' COMMENT '当前执行次数',\n" +
                    "                                     INDEX `idx_task_activity_id`(task_activity_id),\n" +
                    "                                     PRIMARY KEY (id)\n" +
                    ")DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci  COMMENT '对账执行结果';\n" +
                    "\n" +
                    "-- auto Generated on 2023-01-05\n" +
                    // "DROP TABLE IF EXISTS task_config;\n" +
                    "CREATE TABLE IF NOT EXISTS  task_config\n" +
                    "(\n" +
                    "    id            BIGINT(15)   NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "    create_time   DATETIME     NOT NULL COMMENT '创建时间',\n" +
                    "    update_time   DATETIME     NOT NULL COMMENT '更新时间',\n" +
                    "    source_config TEXT         NOT NULL COMMENT '任务比对源数据配置',\n" +
                    "    target_config TEXT         NOT NULL COMMENT '任务比对目标源配置',\n" +
                    "    error_threshold INT         NOT NULL COMMENT '对细账允许的错误阈值',\n" +
                    "    detail_switch tinyint(1)         NOT NULL COMMENT '细账开关',\n" +
                    "    compare_keys  VARCHAR(255) NOT NULL DEFAULT '[]' COMMENT '比对属性列表',\n" +
                    "    delay         INT(11)      NOT NULL DEFAULT 0 COMMENT '延迟时间（秒）',\n" +
                    "    unique_keys   VARCHAR(255) NOT NULL DEFAULT '[]' COMMENT '数据源唯一属性，compareKeys 子集',\n" +
                    "    retry_time   INT NOT NULL DEFAULT '0' COMMENT '错误重试次数',\n" +
                    "    `name`        VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '任务名称',\n" +
                    "    version       INT(11)      NOT NULL DEFAULT 1 COMMENT '配置版本',\n" +
                    "    INDEX `ix_create_time` (create_time),\n" +
                    "    PRIMARY KEY (id)\n" +
                    ") \n" +
                    "  DEFAULT CHARSET = utf8mb4\n" +
                    "  COLLATE = utf8mb4_0900_ai_ci COMMENT 'task_config';\n" +
                    "\n" +
                    // "DROP TABLE IF EXISTS offline_task_activity_result;\n" +
                    "CREATE TABLE IF NOT EXISTS offline_task_activity_result\n" +
                    "(\n" +
                    "    id               BIGINT(15)   NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                    "    task_activity_id BIGINT(15)   NOT NULL DEFAULT 0 COMMENT '任务执行记录id',\n" +
                    "    create_time      DATETIME     NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',\n" +
                    "    update_time      DATETIME     NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '更新时间',\n" +
                    "    result_type      VARCHAR(50)  NOT NULL COMMENT '结果类型',\n" +
                    "    compare_data     TEXT         NOT NULL COMMENT '对账数据',\n" +
                    "    compare_keys     VARCHAR(255) NOT NULL COMMENT '比较keys',\n" +
                    "    verify_result    VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '对账结果',\n" +
                    "    biz_key          VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '业务主键',\n" +
                    "    times            INT(8)       NOT NULL DEFAULT 1 COMMENT '当前执行次数',\n" +
                    "    PRIMARY KEY (id)\n" +
                    ") \n" +
                    "  DEFAULT CHARSET = utf8mb4\n" +
                    "  COLLATE = utf8mb4_0900_ai_ci COMMENT '离线对账执行结果';\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
