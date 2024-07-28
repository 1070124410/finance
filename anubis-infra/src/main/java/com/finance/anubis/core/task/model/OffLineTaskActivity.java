package com.finance.anubis.core.task.model;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.finance.anubis.core.config.OffLineResourceConfig;
import com.finance.anubis.core.config.OffLineTaskConfig;
import com.finance.anubis.core.config.ProfileConfig;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.constants.enums.*;
import com.finance.anubis.core.context.OffLineActivityContext;
import com.finance.anubis.exception.ErrorMsg;
import com.guming.api.json.JsonUtil;
import com.guming.api.pojo.Status;
import com.guming.common.exception.StatusCodeException;
import lombok.CustomLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.finance.anubis.core.constants.Constants.*;
import static com.finance.anubis.core.constants.Constants.CacheKey.DEFAULT_FILE_PATH;
import static com.finance.anubis.core.constants.Constants.OffLineTask.LOCAL_FILE_SPLIT;
import static com.finance.anubis.core.constants.enums.ActionResult.ACCORD;
import static com.finance.anubis.core.constants.enums.ActionResult.VARIANCE;
import static com.finance.anubis.core.constants.enums.OffLineActivityResultType.DETAIL;
import static com.finance.anubis.core.constants.enums.OffLineResourceType.OffLineFileResource;
import static com.finance.anubis.core.constants.enums.OrderVerifyResultEnum.*;

@EqualsAndHashCode(callSuper = true)
@Data
@CustomLog
public class OffLineTaskActivity extends BaseModel {

    /**
     * 上下文
     */
    private OffLineActivityContext context;

    /**
     * 任务配置
     */
    private OffLineTaskConfig taskConfig;

    /**
     * 对账状态
     */
    private OffLineAction action;

    /**
     * 重试次数
     */
    private Integer times;

    /**
     * 对账结果
     */
    private OffLineActivityResult activityResult;

    public static DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static OffLineTaskActivity init() {
        OffLineTaskActivity taskActivity = new OffLineTaskActivity();
        taskActivity.setAction(OffLineAction.DATA_INIT);
        taskActivity.setTimes(ACTIVITY_INIT_EXEC_TIMES);
        LocalDateTime now = LocalDateTime.now();
        taskActivity.setCreateTime(now);
        taskActivity.setUpdateTime(now);
        return taskActivity;
    }

    /**
     * 获取比较器
     *
     * @param key 配置key
     * @return 比较器
     */
    public Comparator getComparator(String key) {
        OffLineResourceConfig config = getResouceConfig(key);
        List<String> fileHeader = getSourceContext(key).getFileHeader();
        Comparator comparator = (Comparator<Object>) (s1, s2) -> {
            String[] str1 = String.valueOf(s1).split(LOCAL_FILE_SPLIT);
            String[] str2 = String.valueOf(s2).split(LOCAL_FILE_SPLIT);
            String c1, c2;
            for (Map.Entry<String, String> entry : config.getSortKeyMap().entrySet()) {
                int idx = fileHeader.indexOf(entry.getKey());
                c1 = str1[idx];
                c2 = str2[idx];
                if (c1.compareTo(c2) != 0) {
                    if ("DESC".equals(entry.getValue())) {
                        return c2.compareTo(c1);
                    }
                    return c1.compareTo(c2);
                }
            }

            return 0;
        };
        return comparator;
    }

    /**
     * 获取对应的任务配置
     *
     * @param key
     * @return
     */
    public OffLineResourceConfig getResouceConfig(String key) {
        return taskConfig.getSourceConfig().getKey().equals(key) ? taskConfig.getSourceConfig() : taskConfig.getTargetConfig();
    }

    /**
     * 获取对应的上下文
     *
     * @param key
     * @return
     */
    public OffLineActivityContext.SourceContext getSourceContext(String key) {
        return context.getSourceContext().get(key);
    }


    /**
     * 构建唯一键
     *
     * @return
     */
    public static String getBizKey(String taskName, String verifyDate) {
        if (StringUtils.isBlank(verifyDate)) {
            log.info("getBizKey fail,illegal verifyDate,taskName:{}", taskName);
            return null;
        }
        return taskName + Constants.UNDERLINE_SPLIE + dfDate.format(LocalDateTime.parse(verifyDate));
    }


    /**
     * 构建唯一键(注意:需要给context中verifyDate赋值后使用)
     *
     * @return
     */
    public String getBizKey() {
        return getBizKey(taskConfig.getName(), context.getVerifyDate().toString());
    }


    /**
     * 准备对账请求接口参数
     *
     * @param key
     */
    public void prepareRequestParams(String key) {
        OffLineResourceConfig config = getResouceConfig(key);
        JSON requestParams = JSONUtil.parse(getSourceContext(key).getRequestParams());
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, String> entry : config.getRequestParamMapping().entrySet()) {
            if (requestParams == null) {
                map.put(entry.getKey(), null);
            } else {
                map.put(entry.getKey(), requestParams.getByPath(entry.getValue()));
            }
        }

        this.getSourceContext(key).setRequestParamMapping(map);
    }


    /**
     * 核对数值型总账
     *
     * @return
     */
    public OffLineActivityResult verifyNumericalTotal() {
        OffLineResourceConfig sourceConfig = taskConfig.getSourceConfig();
        OffLineResourceConfig targetConfig = taskConfig.getTargetConfig();
        BigDecimal sourceTotal = getSourceContext(sourceConfig.getKey()).getTotalAmount();
        BigDecimal targetTotal = getSourceContext(targetConfig.getKey()).getTotalAmount();
        ActionResult result;
        if (sourceTotal == null && targetTotal == null) {
            log.warn("verifyNumericalTotal both total is null,taskName:{}", getTaskConfig().getName());
            result = ACCORD;
        } else if (sourceTotal == null || targetTotal == null) {
            log.warn("verifyNumericalTotal one total is null,taskName:{},sourceTotal:{},targetTotal:{}", getTaskConfig().getName(), sourceTotal, targetTotal);
            result = VARIANCE;
        } else if (sourceTotal.equals(targetTotal)) {
            result = ACCORD;
        } else {
            result = VARIANCE;
        }
        OffLineActivityResult.TotalResultInfo resultInfo = new OffLineActivityResult.TotalResultInfo(sourceTotal, targetTotal);

        return getTotalResult(resultInfo, result);
    }

    /**
     * 核对文件类型的细账
     *
     * @param sourceFile
     * @param targetFile
     * @return
     */
    public OffLineActivityResult verifyFileDetail(String sourceFile, String targetFile) {
        Set<String> sourceUnique = new HashSet<>();
        Set<String> targetUnique = new HashSet<>();
        Set<String> inConsistency = new HashSet<>();
        String sourceKey = taskConfig.getSourceConfig().getKey();
        String targetKey = taskConfig.getTargetConfig().getKey();
        try (BufferedReader sourceReader = new BufferedReader(new FileReader(sourceFile));
             BufferedReader targetReader = new BufferedReader(new FileReader(targetFile))) {
            String source = sourceReader.readLine();
            String target = targetReader.readLine();
            //生成唯一键所在位置下标集合
            List<Integer> uniqueSourceKeyLoc = prepareUniqueKeyLoc(getTaskConfig().getUniqueKeys(), getSourceContext(sourceKey).getFileHeader());
            //生成比较键所在位置下标集合
            List<Integer> compareSourceKeyLoc = prepareCompareKeyLoc(getTaskConfig().getSourceConfig().getCompareDetailKeys(), getSourceContext(sourceKey).getFileHeader());
            List<Integer> uniqueTargetKeyLoc = prepareUniqueKeyLoc(getTaskConfig().getUniqueKeys(), getSourceContext(targetKey).getFileHeader());
            List<Integer> compareTargetKeyLoc = prepareCompareKeyLoc(getTaskConfig().getTargetConfig().getCompareDetailKeys(), getSourceContext(targetKey).getFileHeader());
            //允许的错误阈值
            Integer errorThreshold = taskConfig.getErrorThreshold();
            if (source != null && target != null) {
                do {
                    if ((sourceUnique.size() + targetUnique.size() + inConsistency.size()) >= errorThreshold) {
                        break;
                    }
                    OrderVerifyResultEnum verifyResult = doVerify(source, target, uniqueSourceKeyLoc, compareSourceKeyLoc, uniqueTargetKeyLoc, compareTargetKeyLoc);
                    switch (verifyResult) {
                        //一致
                        case COINCIDENT: {
                            source = sourceReader.readLine();
                            target = targetReader.readLine();
                            break;
                        }
                        //source独有
                        case SOURCE_UNIQUE: {
                            sourceUnique.add(buildUniqueKey(uniqueSourceKeyLoc, source));
                            source = sourceReader.readLine();
                            break;
                        }
                        //target独有
                        case TARGET_UNIQUE: {
                            targetUnique.add(buildUniqueKey(uniqueTargetKeyLoc, target));
                            target = targetReader.readLine();
                            break;
                        }
                        //唯一键一致但内容对不上
                        case DIFFERENT: {
                            inConsistency.add(buildUniqueKey(uniqueSourceKeyLoc, source));
                            source = sourceReader.readLine();
                            target = targetReader.readLine();
                            break;
                        }
                        default:
                            throw new RuntimeException();
                    }
                } while (source != null && target != null);
            }
        } catch (Exception e) {
            ErrorMsg errorMsg = new ErrorMsg(getBizKey(), null, OffLineAction.DATA_COMPARE_DETAIL, "verifyFileDetail异常", e);
            throw new StatusCodeException(Status.error(JsonUtil.toJson(errorMsg)));
        }
        ActionResult verifyResult = VARIANCE;
        if (sourceUnique.isEmpty() && targetUnique.isEmpty() && inConsistency.isEmpty()) {
            verifyResult = ACCORD;
        }

        OffLineActivityResult.DetailResultInfo resultInfo = new OffLineActivityResult.DetailResultInfo(sourceUnique, targetUnique, inConsistency);
        return getDetailResult(resultInfo, verifyResult);
    }

    /**
     * 准备唯一键所在行数据下标位置
     *
     * @param uniqueKeys
     * @param fileHeader
     * @return
     */
    private List<Integer> prepareUniqueKeyLoc(List<String> uniqueKeys, List<String> fileHeader) {
        List<Integer> res = new ArrayList<>();
        for (String key : uniqueKeys) {
            res.add(fileHeader.indexOf(key));
        }
        return res;
    }

    /**
     * 准备比较键所在行数据下标位置
     *
     * @param compareKeys
     * @param fileHeader
     * @return
     */
    private List<Integer> prepareCompareKeyLoc(List<String> compareKeys, List<String> fileHeader) {
        List<Integer> res = new ArrayList<>();
        for (String key : compareKeys) {
            res.add(fileHeader.indexOf(key));
        }
        return res;
    }

    /**
     * 行数据对账
     *
     * @param source
     * @param target
     * @param uniqueSourceKeyLoc  组成唯一键所在列的位置下标
     * @param compareSourceKeyLoc 组成比较键所在列的位置下标
     * @param uniqueTargetKeyLoc
     * @param compareTargetKeyLoc
     * @return
     */
    private OrderVerifyResultEnum doVerify(String source, String target, List<Integer> uniqueSourceKeyLoc, List<Integer> compareSourceKeyLoc,
                                           List<Integer> uniqueTargetKeyLoc, List<Integer> compareTargetKeyLoc) {
        String[] sourceArray = source.split(LOCAL_FILE_SPLIT);
        String[] targetArray = target.split(LOCAL_FILE_SPLIT);
        StringBuilder sourceBuilder = new StringBuilder();
        StringBuilder targetBuilder = new StringBuilder();
        for (Integer loc : uniqueSourceKeyLoc) {
            sourceBuilder.append(sourceArray[loc]);
        }
        for (Integer loc : uniqueTargetKeyLoc) {
            targetBuilder.append(targetArray[loc]);
        }
        //对比唯一键
        if (!sourceBuilder.toString().equals(targetBuilder.toString())) {
            if (sourceBuilder.toString().compareTo(targetBuilder.toString()) < 0) {
                return SOURCE_UNIQUE;
            } else {
                return TARGET_UNIQUE;
            }
        }
        StringBuilder sourceDetailBuilder = new StringBuilder();
        StringBuilder targetDetailBuilder = new StringBuilder();
        for (Integer loc : compareSourceKeyLoc) {
            sourceDetailBuilder.append(sourceArray[loc]);
        }
        for (Integer loc : compareTargetKeyLoc) {
            targetDetailBuilder.append(targetArray[loc]);
        }
        if (!sourceDetailBuilder.toString().equals(targetDetailBuilder.toString())) {
            return DIFFERENT;
        }

        return COINCIDENT;
    }

    /**
     * 构建对细账唯一键
     *
     * @param uniqueKeyLoc
     * @param line
     * @return
     */
    private String buildUniqueKey(List<Integer> uniqueKeyLoc, String line) {
        String[] strs = line.split(LOCAL_FILE_SPLIT);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < uniqueKeyLoc.size(); i++) {
            if (i == uniqueKeyLoc.size() - 1) {
                builder.append(strs[uniqueKeyLoc.get(i)]);
                continue;
            }
            builder.append(strs[uniqueKeyLoc.get(i)]).append(LOCAL_FILE_SPLIT);
        }

        return builder.toString();
    }

    /**
     * 生成总账结果
     *
     * @return
     */
    private OffLineActivityResult getTotalResult(OffLineActivityResult.ResultInfo resultInfo, ActionResult verifyResult) {
        OffLineActivityResult result = new OffLineActivityResult();
        result.setTaskActivityId(getId());
        result.setBizKey(getBizKey());
        result.setResultType(OffLineActivityResultType.TOTAL);
        result.setTimes(getTimes());
        OffLineActivityResult.ResultKey resultKey = new OffLineActivityResult.ResultKey(taskConfig.getSourceConfig().getCompareTotalKeys(), taskConfig.getTargetConfig().getCompareTotalKeys());
        result.setCompareKeys(resultKey);
        result.setCompareData(resultInfo);
        result.setVerifyResult(verifyResult);
        return result;
    }

    /**
     * 生成细账结果
     *
     * @return
     */
    private OffLineActivityResult getDetailResult(OffLineActivityResult.ResultInfo resultInfo, ActionResult verifyResult) {
        OffLineActivityResult result = new OffLineActivityResult();
        result.setTaskActivityId(getId());
        result.setBizKey(getBizKey());
        result.setResultType(DETAIL);
        result.setTimes(getTimes());
        OffLineActivityResult.ResultKey resultKey = new OffLineActivityResult.ResultKey(taskConfig.getSourceConfig().getCompareDetailKeys(), taskConfig.getTargetConfig().getCompareDetailKeys());
        result.setCompareKeys(resultKey);
        result.setCompareData(resultInfo);
        result.setVerifyResult(verifyResult);
        return result;
    }

    /**
     * 对账文件存放绝对路径
     *
     * @return
     */
    public StringBuilder getAbsoluteFilePath() {
        return new StringBuilder().append(ProfileConfig.getValue(FILE_PATH, DEFAULT_FILE_PATH))
                .append(getBizKey()).append(FOLDER_SPLIT);
    }

    /**
     * 根据任务配置初始化对账双方上下文
     */
    public void initSourceContext() {
        OffLineActivityContext context = OffLineActivityContext.initActivityContext();
        this.setContext(context);
        String sourceType = taskConfig.getSourceConfig().getResourceType();
        String sourceKey = taskConfig.getSourceConfig().getKey();
        String targetType = taskConfig.getTargetConfig().getResourceType();
        String targetKey = taskConfig.getTargetConfig().getKey();

        if (OffLineFileResource.getCode().equals(sourceType)) {
            context.getSourceContext().put(sourceKey, new OffLineActivityContext.FileSourceContext());
        } else {
            context.getSourceContext().put(sourceKey, new OffLineActivityContext.HttpSourceContext());
        }
        this.getSourceContext(sourceKey).setResourceType(OffLineResourceType.of(sourceType));
        this.getSourceContext(sourceKey).setSourceDataStatus(SourceDataStatus.DATA_INIT);
        if (OffLineFileResource.getCode().equals(targetType)) {
            context.getSourceContext().put(targetKey, new OffLineActivityContext.FileSourceContext());
        } else {
            context.getSourceContext().put(targetKey, new OffLineActivityContext.HttpSourceContext());
        }
        this.getSourceContext(targetKey).setResourceType(OffLineResourceType.of(targetType));
        this.getSourceContext(targetKey).setSourceDataStatus(SourceDataStatus.DATA_INIT);
    }

    /**
     * 记录对账请求信息,更新数据拉取状态
     *
     * @param key
     * @param requestParam
     * @param custom
     */
    public void initRequestParam(String key, String requestParam, String custom) {
        OffLineActivityContext.SourceContext sourceContext = this.getSourceContext(key);
        sourceContext.setRequestParams(requestParam);
        sourceContext.setCustom(custom);
        prepareRequestParams(key);
        sourceContext.setSourceDataStatus(SourceDataStatus.DATA_FETCH);
        sourceContext.setFetchingTime(LocalDateTime.now());
    }

    public void setVerifyDate(String verifyDate) {
        this.context.setVerifyDate(StringUtils.isBlank(verifyDate) ? LocalDateTime.now() : LocalDateTime.parse(verifyDate));
    }
}
