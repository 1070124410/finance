package com.finance.anubis.core.util;

import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.guming.api.json.JsonUtil;
import com.guming.api.pojo.Status;
import com.guming.common.exception.StatusCodeException;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author: linjuanjuan
 * @time: 3/17/22 9:58 AM
 */
@Slf4j
public class DingTalkWebhookUtil {

    private String secret;
    private String webhook;

    public DingTalkWebhookUtil(String secret, String webhook) {
        Assert.hasText(secret, "secret is empty.");
        Assert.hasText(webhook, "webhook is empty.");

        this.secret = secret;
        this.webhook = webhook;
    }

    /**
     * 加签
     * @return
     */
    private String sign(Long timestamp) {
        String stringToSign = timestamp + "\n" + secret;

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");

            return sign;
        } catch (Exception e) {
            log.info("DingTalk sign error.", e);
            throw new StatusCodeException(Status.error("钉钉群消息加签异常"));
        }
    }

    /**
     * 发送markdown格式消息
     * @param markdownContent 消息内容，markdown格式
     * @param mobiles 被@的成员手机号，手机号必须在群内
     * @return 是否成功
     */
    public boolean sendMarkDown(String title, String markdownContent, List<String> mobiles) {
        Long timestamp = System.currentTimeMillis();
        String content = StrUtil.format("&timestamp={}&sign={}", timestamp, sign(timestamp));

        String msg = StrUtil.format("### {} \n @{} \n {}", title, String.join(", @", mobiles), markdownContent);

        DingTalkClient client = new DefaultDingTalkClient(webhook + content);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(msg);
        request.setMarkdown(markdown);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(mobiles);
        at.setIsAtAll("false");
        request.setAt(at);
        try {
            log.info("DingTalk robot request:{}", JsonUtil.toJson(request));
            OapiRobotSendResponse response = client.execute(request);
            log.info("DingTalk robot response:{}", JsonUtil.toJson(response));
            return response.isSuccess();
        } catch (ApiException e) {
            log.info("钉钉消息通知异常", e);
            return false;
        }
    }

    /**
     * 发送markdown格式消息
     * @param markdownContent 消息内容，markdown格式
     * @return 是否成功
     */
    public boolean sendAtAllMarkDown(String title, String markdownContent) {
        Long timestamp = System.currentTimeMillis();
        String content = StrUtil.format("&timestamp={}&sign={}", timestamp, sign(timestamp));

        String msg = StrUtil.format("### {} \n {}", title, markdownContent);

        DingTalkClient client = new DefaultDingTalkClient(webhook + content);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(msg);
        request.setMarkdown(markdown);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setIsAtAll("true");
        request.setAt(at);
        try {
            log.info("DingTalk robot request:{}", JsonUtil.toJson(request));
            OapiRobotSendResponse response = client.execute(request);
            log.info("DingTalk robot response:{}", JsonUtil.toJson(response));
            return response.isSuccess();
        } catch (ApiException e) {
            log.info("钉钉消息通知异常", e);
            return false;
        }
    }

    private static final int ERROR_NUMBER_LIMIT = 100;



}
