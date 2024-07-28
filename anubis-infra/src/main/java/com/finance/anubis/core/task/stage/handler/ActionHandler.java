package com.finance.anubis.core.task.stage.handler;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.config.URLResourceConfig;
import com.finance.anubis.constants.Constants;
import com.finance.anubis.core.context.ActivityContext;
import com.finance.anubis.core.model.TaskActivity;
import com.finance.anubis.enums.Action;
import com.finance.anubis.mq.MessageProducer;
import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.response.Result;
import lombok.CustomLog;
import lombok.Getter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.finance.anubis.constants.Constants.TASK_ACTIVITY_ACTION_TOPIC;

/**
 * action 执行器
 */
public abstract class ActionHandler {
    @Getter
    protected final Action action;

    protected final TaskActivityRepository taskActivityRepository;

    protected final MessageProducer messageProducer;

    protected final RestTemplate restTemplate;

    public static final TypeReference<Result<JSON>> JSON_TYPE_REFERENCE = new TypeReference<Result<JSON>>() {
    };


    public ActionHandler(Action action,
                         TaskActivityRepository taskActivityRepository,
                         RestTemplate restTemplate,
                         MessageProducer messageProducer
    ) {
        this.action = action;
        this.taskActivityRepository = taskActivityRepository;
        this.restTemplate = restTemplate;
        this.messageProducer = messageProducer;
    }

    public void handle(TaskActivity taskActivity){
        Action originAction = taskActivity.getAction();
        // 先执行action内部操作
        innerHandle(taskActivity);
        //更新activity
        updateActivity(taskActivity, originAction);
        // 发送推进action的消息，如果报错，当前消息会失败，当前消息重试会驱动下一action
        afterHandle(taskActivity);
    }

    private void updateActivity(TaskActivity taskActivity, Action originAction) {
        taskActivity.setAction(originAction.nextAction());
        boolean success = false;
        if (originAction == Action.INIT) {
            success = taskActivityRepository.save(taskActivity);
        } else {
            success = taskActivityRepository.update(taskActivity);
        }
        if (!success) {
            throw new RuntimeException("activity更新失败" + taskActivity.getId());
        }
    }

    /**
     * action 核心执行方法
     *
     * @param taskActivity 当前activity
     */
    protected abstract void innerHandle(TaskActivity taskActivity) ;

    public void afterHandle(TaskActivity taskActivity) {
        // 推送到mq
        messageProducer.syncSend(TASK_ACTIVITY_ACTION_TOPIC, taskActivity.getAction().name(), JSONUtil.toJsonStr(taskActivity.getResult()));
    }

    public ActivityContext.URLSourceContext executeUrlSource(URLResourceConfig urlResourceConfig, final TaskActivity taskActivity) {
        Method method = Method.valueOf(urlResourceConfig.getMethod());

        Map<String, Map<String, Object>> mappedData = taskActivity.getContextMappedData();
        Map<String, Object> requestParams = new HashMap<>();
        for (Map.Entry<String, URLResourceConfig.ParamPath> entry : urlResourceConfig.getRequestParamMapping().entrySet()) {
            requestParams.put(entry.getKey(), mappedData.get(entry.getValue().getSourceKey()).get(entry.getValue().getPath()));
        }
//        Entry entry = null;
        String body = null;
        String url = urlResourceConfig.getUrl();
        String resourceName = urlResourceConfig.getKey();

        try {
//            entry = SphU.entry(resourceName);
            // 组装请求路径并发送
            switch (method) {
                case GET:
                    StringBuilder urlParamFormatted = new StringBuilder(url + "?");
                    String[] keyArray = requestParams.keySet().toArray(new String[0]);
                    for (int i = 0; i < keyArray.length; i++) {
                        String key = keyArray[i];
                        urlParamFormatted.append(key).append("={").append(key).append("}");
                        if (keyArray.length >= 2 && i < keyArray.length - 1) {
                            urlParamFormatted.append("&");
                        }
                    }
                    body = restTemplate.getForObject(urlParamFormatted.toString(), String.class, requestParams);
                    break;
                case POST:
                    body = restTemplate.postForObject(urlResourceConfig.getUrl(), requestParams, String.class, requestParams);
                    break;

            }

            JSON responseJson = JSONUtil.parse(body);

            Result<JSON> result = responseJson.toBean(JSON_TYPE_REFERENCE);
            if (result.isSuccess()) {
                Map<String, Object> mappedResponseData = new HashMap<>();
                JSON data = result.getData();
                // 从响应结果中抽取按配置提取结果
                for (Map.Entry<String, String> dataMappingEntry : urlResourceConfig.getDataMapping().entrySet()) {
                    if (data == null) {
                        mappedResponseData.put(dataMappingEntry.getKey(), null);
                    } else {
                        mappedResponseData.put(dataMappingEntry.getKey(), data.getByPath(dataMappingEntry.getValue()));
                    }
                }
                //  此处可以根据后续需求把body去掉
                return new ActivityContext.URLSourceContext(urlResourceConfig.getUrl(), body, urlResourceConfig.getKey(), mappedResponseData);
            }
            throw new RuntimeException("url resource action execute failed");
//        } catch (BlockException ex) {
//            log.error("Task [{}] request resource [{}] was blocked", taskActivity.getOnLineTaskConfig().getName(), resourceName);
//            throw new RuntimeException("Request was blocked");
        } finally {
//            if (entry != null) {
//                entry.exit();
//            }
        }

    }
}
