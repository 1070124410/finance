package com.finance.anubis.core.context;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.finance.anubis.enums.OffLineResourceType;
import com.finance.anubis.enums.SourceDataStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yezhaoyang
 * @Date 2023/03/01 11:16
 * @Description
 **/
@Data
@Accessors
@NoArgsConstructor
public class OffLineActivityContext implements Serializable {


    /**
     * 数据获取执行上下文
     */
    private Map<String, SourceContext> sourceContext;

    /**
     * 对账日期
     */
    private LocalDateTime verifyDate;

    public static OffLineActivityContext initActivityContext() {
        OffLineActivityContext context = new OffLineActivityContext();
        context.setSourceContext(new HashMap<>());
        return context;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "resourceType",
            visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(
                    value = OffLineActivityContext.FileSourceContext.class,
                    name = "OffLineFileResource"),
            @JsonSubTypes.Type(
                    value = OffLineActivityContext.HttpSourceContext.class,
                    name = "OffLineHttpResource")})
    public static abstract class SourceContext implements Serializable {


        protected OffLineResourceType resourceType;

        /**
         * 上游接口请求参数
         */
        protected String requestParams;

        /**
         * 格式化文件的文件头key集合
         */
        protected List<String> fileHeader;

        /**
         * 请求对账数据参数
         */
        protected Map<String, Object> requestParamMapping;

        /**
         * 小文件路径集合
         */
        protected List<String> pathList;

        /**
         * 对账文件路径
         */
        protected String reconciliationFile;

        /**
         * oss对账文件路径
         */
        protected String ossReconciliationFile;

        /**
         * 总账
         */
        protected BigDecimal totalAmount;

        /**
         * 上游自定义参数,对账完成mq返回
         */
        protected String custom;

        /**
         * 数据抓取状态
         */
        protected SourceDataStatus sourceDataStatus;

        /**
         * 开始获取对账数据的时间
         */
        protected LocalDateTime fetchingTime;
    }


    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class FileSourceContext extends SourceContext {
        /**
         * 读取文件出错记录错误出现所在行
         */
        private Integer line;


    }

    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Data
    public static class HttpSourceContext extends SourceContext {


    }
}
