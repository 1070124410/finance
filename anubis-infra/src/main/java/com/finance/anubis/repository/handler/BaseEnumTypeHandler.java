package com.finance.anubis.repository.handler;

import com.finance.anubis.enums.*;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(value = {Action.class, ActionResult.class, MessageInfraType.class, ResourceType.class, TaskStatus.class, TaskType.class})
public class BaseEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    public final static Logger log = LoggerFactory.getLogger(BaseEnumTypeHandler.class);
    private Class<E> type;

    public BaseEnumTypeHandler() {
    }

    public BaseEnumTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, parameter.toString());
        } else {
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return get(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return get(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return get(cs.getString(columnIndex));
    }

    private <E extends Enum<E>> E get(String v) {
        if (v == null) {
            return null;
        }
        Method method = null;
        E result = null;
        try {
            method = type.getMethod("of", String.class);
            result = (E) method.invoke(type, v);
        } catch (NoSuchMethodException e) {
            log.error("enum class need have 'of' static method {}", type.getClass().getName());
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("enum class need have public 'of' static method {}", type.getClass().getName());
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.error("method {} not in class {} ", method.getName(), type.getClass().getName());
            throw new RuntimeException(e);
        }
        return result;
    }

}
