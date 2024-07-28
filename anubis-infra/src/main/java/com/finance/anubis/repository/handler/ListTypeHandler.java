package com.finance.anubis.repository.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ListTypeHandler<E> extends BaseTypeHandler<List<E>> {

    private Class<E> type;

    public ListTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<E> parameter, JdbcType jdbcType) throws SQLException {
        String x = JSONUtil.toJsonStr(parameter);
        ps.setString(i, x);
    }

    @Override
    public List<E> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        JSONArray jsonArray = JSONUtil.parseArray(s);
        return StrUtil.isBlank(s) ? null : JSONUtil.toList(jsonArray, type);
    }

    @Override
    public List<E> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        JSONArray jsonArray = JSONUtil.parseArray(s);
        return StrUtil.isBlank(s) ? null : JSONUtil.toList(jsonArray, type);
    }

    @Override
    public List<E> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        JSONArray jsonArray = JSONUtil.parseArray(s);
        return StrUtil.isBlank(s) ? null : JSONUtil.toList(jsonArray, type);
    }

}