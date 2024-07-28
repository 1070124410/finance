package com.guming.finance.report.common

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.apache.commons.io.FileUtils

import javax.sql.DataSource

/**
 * @author baocheng
 * @date 2022/12/28 15:10
 */
class SqlUtil {

    static ThreadLocal<DataSource> dataSourceThreadLocal = new ThreadLocal<>()

    static void setDataSourceThreadLocal(DataSource dataSource) {
        dataSourceThreadLocal.set(dataSource)
    }

    static DataSource currDataSource() {
        return dataSourceThreadLocal.get()
    }

    static void sqlExecute(String path) {
        def file = new File(SqlUtil.class.getClassLoader().getResource(path).getFile())
        def content = FileUtils.readFileToString(file, "UTF-8")
        new Sql(currDataSource()).execute(content)
    }

    static List<GroovyRowResult> rows(String select) {
        return new Sql(currDataSource()).rows(select)
    }

    static void truncateTable(String...tables) {
        for (String table : tables) {
            new Sql(currDataSource()).execute(String.format("truncate table %s;", table))
        }
    }
}
