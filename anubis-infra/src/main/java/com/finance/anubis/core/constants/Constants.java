package com.finance.anubis.core.constants;

public class Constants {
    public static final String ANUBIS_MQ_CONSUMER_GROUP = "GID_CONSIST_GROUP";

    public static final String OFFLINE_TASK_CONSUMER_GROUP = "GID_OFFLINE_TASK_GROUP";

    public static final String ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC = "TASK_ACTIVITY_ACTION";

    public static final Integer ACTIVITY_INIT_EXEC_TIMES = 1;

    /**
     * 下划线分隔符,用来拼接文件名等
     */
    public static final String UNDERLINE_SPLIE = "_";

    /**
     * 文件路径分隔符
     */
    public static final String FOLDER_SPLIT = "/";

    public static class LocalFile {
        /**
         * 对账信息上游源文件
         */
        public static final String ORIGIN_RECONCILIATION_FILE = "_origin";

        /**
         * 源文件分割小文件
         */
        public static final String PART_RECONCILIATION_FILE = "_part";

        /**
         * 整理就绪的对账文件
         */
        public static final String RECONCILIATION_FILE = "_reconciliation";

        /**
         * 比较阶段从oss保存至本地的对账文件
         */
        public static final String OSS_COMPARE_FILE = "_compare";

    }


    /**
     * 文件存放目录,从nacos获取
     */
    public static final String FILE_PATH = "file_path";

    public static class CacheKey {

        /**
         * 默认对账文件存储路径
         */
        public static final String DEFAULT_FILE_PATH = "/log/offline-reconciliation-file/";


        /**
         * 缓存键,data_fetch开始时间
         */
        public static final String FETCHTIME_CACHE_KEY = "_time";

        /**
         * 缓存键,任务状态表示
         */
        public static final String DATA_FETCH_CACHE_KEY = "_fetch";

    }


    public static class OffLineTask {

        /**
         * 对账平台内部文件分隔符
         */
        public static final String LOCAL_FILE_SPLIT = ",";

        /**
         * 当前对账任务失败已重试次数
         */
        public static final String RETRY_TIMES = "retry";
    }

    public static final String PAGENO = "pageNo";

    public static final String PAGESIZE = "pageSize";


}
