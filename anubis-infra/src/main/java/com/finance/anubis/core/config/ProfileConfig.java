package com.finance.anubis.core.config;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author: linjuanjuan
 * @time: 6/5/21 3:07 PM
 */
@Slf4j
public class ProfileConfig {

    private volatile static Map<String, String> properties = Maps.newConcurrentMap();


    private static final Splitter splitter = Splitter.on(',');

    protected Map<String, String> getProperties() {
        return properties;
    }

    public static String getValue(String key) {
        return properties.get(key);
    }

    public static String getValue(String key, String defaultValue) {
        return Optional.ofNullable(getValue(key)).orElse(defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, Boolean.FALSE);
    }

    public static boolean getBoolean(String key, Boolean defaultValue) {
        return Boolean.parseBoolean(getValue(key, Optional.ofNullable(defaultValue).orElse(Boolean.FALSE).toString()));
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return NumberUtils.toInt(properties.get(key), defaultValue);
    }

    public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        try {
            if (!properties.containsKey(key)) {
                return defaultValue;
            }
            return new BigDecimal(properties.get(key));
        } catch (Exception e) {
            log.warn("获取Scale2Decimal异常", e);
            return defaultValue;
        }
    }
    public static Set<String> getSet(String key) {
        return getSet(key, Sets.newHashSet());
    }

    public static Set<String> getSet(String key, Set<String> defaultValue) {
        String value = getValue(key);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Sets.newHashSet(splitter.split(value));
            } catch (Exception e) {
                log.warn("value切割成set失败，key={},value={}", key, value);
                return defaultValue;
            }
        } else {
            log.warn("当前key={}对应的value为空", key);
            return defaultValue;
        }
    }

    public static Map<String, String> getMap(String key) {
        Map<String, String> stringStringMap;
        try {
            stringStringMap = JsonUtil.of(properties.getOrDefault(key, "{}"), new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            log.warn("value装换程map失败，key={}", key);
            stringStringMap = Maps.newHashMap();
        }
        return stringStringMap;
    }

    public static List<String> getList(String key) {
        return getList(key, Lists.newArrayList());
    }

    public static List<String> getList(String key, List<String> defaultValue) {
        String value = getValue(key);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Lists.newArrayList(splitter.split(value));
            } catch (Exception e) {
                log.warn("value切割成list失败，key={},value={}", key, value);
                return defaultValue;
            }
        } else {
            log.warn("当前key={}对应的value为空", key);
            return defaultValue;
        }
    }

    public static LocalDateTime getLocalDateTime(String key) {
        String value = getValue(key);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(value, df);
        return date.atStartOfDay();
    }

    protected static void setProperties(Map<String, String> config) {
        if (MapUtil.isEmpty(config)) {
            properties.clear();
            return;
        }

        properties.clear();
        properties.putAll(config);
    }

}
