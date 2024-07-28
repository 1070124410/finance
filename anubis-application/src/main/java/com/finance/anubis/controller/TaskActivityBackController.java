//package com.finance.anubis.controller;
//
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @Author yezhaoyang
// * @Date 2023/03/23 14:36
// * @Description
// **/
//@RestController
//@RequestMapping("/back")
//public class TaskActivityBackController {
//
//    private final TaskActivityRepository taskActivityRepository;
//
//    private final OffLineTaskActivityRepository offLineTaskActivityRepository;
//
//    public TaskActivityBackController(TaskActivityRepository taskActivityRepository, OffLineTaskActivityRepository offLineTaskActivityRepository) {
//        this.taskActivityRepository = taskActivityRepository;
//        this.offLineTaskActivityRepository = offLineTaskActivityRepository;
//    }
//
//    @PostMapping("/updateActivity")
//    public Result<Boolean> updateActivity(@RequestBody TaskActivity activity) {
//        if (activity == null) {
//            return Result.error(String.valueOf(false));
//        }
//
//        return Result.success(taskActivityRepository.update(activity));
//    }
//
//    @PostMapping("/updateActivityAction")
//    public Result<Boolean> updateActivityAction(@RequestBody TaskActivity activity) {
//        if (activity == null) {
//            return Result.error(String.valueOf(false));
//        }
//
//        return Result.success(taskActivityRepository.updateAction(activity.getId(), activity.getAction()));
//    }
//
//    @PostMapping("/updateOffLineActivity")
//    public Result<Boolean> updateOffLineActivity(@RequestBody OffLineTaskActivity activity) {
//        if (activity == null) {
//            return Result.error(String.valueOf(false));
//        }
//
//        return Result.success(offLineTaskActivityRepository.update(activity));
//    }
//
//    @PostMapping("/updateOffLineActivityAction")
//    public Result<Boolean> updateOffLineActivityAction(@RequestBody OffLineTaskActivity activity) {
//        if (activity == null) {
//            return Result.error(String.valueOf(false));
//        }
//
//        return Result.success(offLineTaskActivityRepository.updateAction(activity.getId(), activity.getAction()));
//    }
//}
