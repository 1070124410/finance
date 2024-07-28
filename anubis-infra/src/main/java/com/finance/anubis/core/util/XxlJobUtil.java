package com.finance.anubis.core.util;

import cn.hutool.http.*;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;


/**
 * @ClassName: ApiUtil
 * @Description:
 * @Author LXYuuuuu
 * @Date 2020/6/30 9:47
 */
public class XxlJobUtil {

    public static Logger logger =  LoggerFactory.getLogger(XxlJobUtil.class);

    private static String cookie="";

    /**
     * 新增/编辑任务
     * @param url
     * @param requestInfo
     * @return
     * @throws IOException
     */
//    public static JSON addJob(String url, JSONObject requestInfo) {
//        String path = "/jobinfo/add";
//        String targetUrl = url + path;
//        String response = HttpUtil.post(targetUrl, requestInfo);
//        return JSONUtil.parse(response);
//    }

//    public static JSON updateJob(String url, JSONObject requestInfo)  {
//        String path = "/jobinfo/update";
//        String targetUrl = url + path;
//        String response = HttpUtil.post(targetUrl, requestInfo);
//        return JSONUtil.parse(response);
//    }

    /**
     * 删除任务
     * @param url
     * @param id
     * @return
     * @throws IOException
     */
//    public static JSON deleteJob(String url,int id) {
//        String path = "/jobinfo/delete?id="+id;
//        String targetUrl = url + path;
//        return doPost(targetUrl);
//    }

    /**
     * 开始任务
     * @param url
     * @param id
     * @return
     * @throws IOException
     */
//    public static JSON startJob(String url,int id) {
//        String path = "/jobinfo/start?id="+id;
//        return doPost(path);
//    }

    public static JSON triggerJob(String url,int id,JSON params) {
        String path = "/jobinfo/trigger?id="+id;
        return doPost(path,params);
    }

    private static JSON doPost(String path,JSON params) {
        HttpRequest httpRequest = HttpUtil.createGet(path);
        httpRequest.header("cookie",cookie);
        HttpResponse httpResponse = httpRequest.execute();
        return JSONUtil.parse(httpResponse.body());
    }

    /**
     * 停止任务
     * @param url
     * @param userName
     * @param password
     * @return
     * @throws IOException
     */
//    public static JSON stopJob(String url,int id) {
//        String path = "/jobinfo/stop?id="+id;
//        String targetUrl = url + path;
//        return doPost(targetUrl);
//    }



    public static String login(String url, String userName, String password)  {
        String path = "/login?userName="+userName+"&password="+password;
        String targetUrl = url + path;
        HttpRequest httpRequest = HttpUtil.createPost(targetUrl);
        httpRequest.header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue());
        HttpResponse httpResponse = httpRequest.execute();
        if (httpResponse.getStatus() == 200) {
            List<HttpCookie> responseCookies = httpResponse.getCookies();
            StringBuffer tmpcookies = new StringBuffer();
            for (HttpCookie c : responseCookies) {
                tmpcookies.append(c.toString() + ";");
            }
            cookie = tmpcookies.toString();
        } else {
            try {
                cookie = "";
            } catch (Exception e) {
                cookie="";
            }
        }
        return cookie;
    }
}