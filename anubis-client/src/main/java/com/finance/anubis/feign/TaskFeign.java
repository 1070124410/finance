package com.finance.anubis.feign;

import com.finance.anubis.constant.AnubisServiceConstant;
import com.finance.anubis.req.TaskReq;
import com.finance.anubis.res.TaskRes;
import com.guming.api.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 09:54
 * @Description
 **/
@FeignClient(name = AnubisServiceConstant.APPLICATION_NAME,
        decode404 = true,
        contextId = "taskFeign")
public interface TaskFeign {

    String PREFIX_PATH = "task";

    /**
     * 根据id查询任务
     *
     * @param taskId
     * @return
     */
    @GetMapping(PREFIX_PATH + "/selectById")
    Result<TaskRes> selectById(@RequestParam("taskId") Long taskId);

    /**
     * 分页查询任务列表
     *
     * @param taskReq
     * @return
     */
    @GetMapping(PREFIX_PATH + "/selectTaskPage")
    Result<List<TaskRes>> selectTaskPage(@RequestBody TaskReq taskReq);

    /**
     * 根据任务条件查询任务
     *
     * @param taskReq
     * @return
     */
    @GetMapping(PREFIX_PATH + "/selectTaskByParam")
    Result<List<TaskRes>> selectTaskByParam(@RequestBody TaskReq taskReq);

    /**
     * 新增任务
     *
     * @param taskReq
     * @return
     */
    @PostMapping(PREFIX_PATH + "/add")
    Result<TaskRes> add(@RequestBody TaskReq taskReq);

    /**
     * 更新任务
     *
     * @param taskReq
     * @return
     */
    @PostMapping(PREFIX_PATH + "/updateTask")
    Result<TaskRes> updateTask(@RequestBody TaskReq taskReq);


    /**
     * 开始任务
     *
     * @param taskId
     */
    @GetMapping(PREFIX_PATH + "/startTask")
    Result<String> startTask(@RequestParam("taskId") Long taskId);


    /**
     * 停止任务
     *
     * @param taskId
     */
    @GetMapping(PREFIX_PATH + "/stopTask")
    Result<String> stopTask(@RequestParam("taskId") Long taskId);


    /**
     * 手动补偿任务mq
     *
     * @param messageBodyStr 消息体
     * @param taskId
     * @return
     */
    @PostMapping(PREFIX_PATH + "/compensateMessage")
    Result<String> compensateMessage(@RequestParam("messageBodyStr") String messageBodyStr, @RequestParam("taskId") Long taskId);
}
