package com.finance.anubis.repository.impl

import cn.hutool.extra.spring.SpringUtil
import com.guming.api.json.JsonUtil
import com.guming.api.pojo.page.Limit
import com.guming.common.exception.StatusCodeException
import com.finance.anubis.core.config.OnLineTaskConfig
import com.finance.anubis.repository.dto.TaskConfigDTO
import com.finance.anubis.repository.dto.TaskDTO
import com.finance.anubis.repository.entity.TaskConfigEntity
import com.finance.anubis.repository.entity.TaskEntity
import com.finance.anubis.repository.mapper.TaskConfigMapper
import com.finance.anubis.report.common.BaseSpecification
import mockit.Mock
import mockit.MockUp
import org.springframework.context.annotation.Import

import java.time.LocalDateTime

@Import([SpringUtil])
class TaskRepositorySpec extends BaseSpecification {

    def taskRepository

    def taskMapper

    def taskConfigMapper

    def dataSource

    def taskConfigEntity

    def task

    def setup() {
        dataSource = com.finance.anubis.utils.MapperUtil.inMemoryDataSource()
        taskMapper = com.finance.anubis.utils.MapperUtil.getMapper(com.finance.anubis.repository.mapper.TaskMapper.class, dataSource)
        taskConfigMapper = com.finance.anubis.utils.MapperUtil.getMapper(TaskConfigMapper.class, dataSource)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        com.finance.anubis.utils.MapperUtil.createAppMainTable(dataSource)

        def configJson = "{\"sourceConfig\":{\"key\":\"LIQ_COUPON_EVENT\",\"resourceType\":\"EventResourceConfig\",\"dataMapping\":{\"orderId\":\"\$.orderId\"},\"group\":\"GID_CONSIST_GROUP\",\"topic\":\"liq_coupon_verify_topic\",\"tags\":[\"byte_dance_coupon_tag\"],\"messageMode\":\"CLUSTERING\",\"messageInfraType\":\"RocketMq\",\"urlResourceConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\"},\"url\":\"http://liq-service/coupon\",\"method\":\"GET\",\"requestParamMapping\":{\"orderId\":{\"sourceKey\":\"LIQ_COUPON_EVENT\",\"path\":\"orderId\"}}}]},\"targetConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\"},\"url\":\"http://platform-promotion-center/query\",\"method\":\"GET\",\"requestParamMapping\":{\"orderId\":{\"sourceKey\":\"LIQ_COUPON_EVENT\",\"path\":\"orderId\"}}}],\"compareKeys\":[\"couponUserId\"],\"delay\":1,\"uniqueKeys\":[\"couponUserId\"],\"name\":\"liqCouponCompare\",\"id\":1,\"version\":0}"
        def taskConfig = JsonUtil.of(configJson, OnLineTaskConfig.class)
        taskConfigEntity = TaskConfigDTO.toEntity(taskConfig)
        taskConfigEntity.setCreateTime(LocalDateTime.now())
        taskConfigEntity.setUpdateTime(LocalDateTime.now())
        taskConfigMapper.insert(taskConfigEntity)
        task = new com.finance.anubis.core.task.model.Task()
        task.setTaskConfig(taskConfig)
        task.setCreateTime(LocalDateTime.now())
        task.setUpdateTime(LocalDateTime.now())
        task.setStatus(com.finance.anubis.core.constants.enums.TaskStatus.START)
        task.setTaskType(com.finance.anubis.core.constants.enums.TaskType.ONLINE)
        task.setId(1)

        new MockUp<TaskConfigDTO>() {
            @Mock
            static OnLineTaskConfig toModel(TaskConfigEntity taskConfigEntity) {
                def json = "{\"sourceConfig\":{\"key\":\"LIQ_COUPON_EVENT\",\"resourceType\":\"EventResourceConfig\",\"dataMapping\":{\"orderId\":\"\$.orderId\"},\"group\":\"GID_CONSIST_GROUP\",\"topic\":\"liq_coupon_verify_topic\",\"tags\":[\"byte_dance_coupon_tag\"],\"messageMode\":\"CLUSTERING\",\"messageInfraType\":\"RocketMq\",\"urlResourceConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\"},\"url\":\"http://liq-service/coupon\",\"method\":\"GET\",\"requestParamMapping\":{\"orderId\":{\"sourceKey\":\"LIQ_COUPON_EVENT\",\"path\":\"orderId\"}}}]},\"targetConfigs\":[{\"key\":\"GET_WRITEOff_STATE\",\"resourceType\":\"URLResourceConfig\",\"dataMapping\":{\"couponUserId\":\"\$.couponUserId\"},\"url\":\"http://platform-promotion-center/query\",\"method\":\"GET\",\"requestParamMapping\":{\"orderId\":{\"sourceKey\":\"LIQ_COUPON_EVENT\",\"path\":\"orderId\"}}}],\"compareKeys\":[\"couponUserId\"],\"delay\":1,\"uniqueKeys\":[\"couponUserId\"],\"name\":\"liqCouponCompare\",\"id\":1,\"version\":0}"
                return JsonUtil.of(json, OnLineTaskConfig.class)
            }
        }
    }

    def add() {
        given:

        when:
        def res = taskRepository.add(task)
        then:
        print(res)
    }

    def addFail() {
        given:
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        taskMapper.insert(_) >> 0
        when:
        try {
            taskRepository.add(task)
        } catch (StatusCodeException e) {
            print("fail")
        }
        then:
        true
    }

    def update() {
        given:
        def id = taskRepository.add(task).getId()
        task.setTaskType(com.finance.anubis.core.constants.enums.TaskType.OFFLINE)
        task.setId(id)
        when:
        def res = taskRepository.updateTask(task)
        then:
        print(res)
    }

    def updateFail() {
        given:
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        taskMapper.updateByPrimaryKeySelective(_) >> 0
        when:
        try {
            taskRepository.updateTask(task)
        } catch (StatusCodeException e) {
            print("fail")
        }
        then:
        true
    }

    def selectById() {
        given:
        def id = taskRepository.add(task).getId()

        when:
        def res = taskRepository.selectById(id)
        then:
        res.getTaskType().equals(com.finance.anubis.core.constants.enums.TaskType.ONLINE)
    }

    def selectByIdNullId() {
        given:
        when:
        def res = taskRepository.selectById(0)
        then:
        res == null
    }

    def selectByIdNullEntity() {
        given:
        when:
        def res = taskRepository.selectById(10)
        then:
        res == null
    }

    def selectByIdNullConfigId() {
        given:
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        def e = new TaskEntity()
        taskMapper.selectByPrimaryKey(_) >> e
        when:
        def res = taskRepository.selectById(1)
        then:
        res == null
    }

    def selectByIdNullConfig() {
        given:
        def taskConfigMapper = Mock(TaskConfigMapper)
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        def e = new TaskEntity()
        e.setId(1)
        e.setTaskConfigId(1)
        taskMapper.selectByPrimaryKey(_) >> e
        taskConfigMapper.selectByPrimaryKey(_) >> null
        when:
        try {
            taskRepository.selectById(1)
        } catch (StatusCodeException ex) {
            print(ex.getStatusCode().getReason())
        }
        then:
        true
    }

    def selectTaskPageEmpty() {
        given:
        taskRepository.add(task).getId()
        def limit = new Limit(1, 0, 2)
        def newTask = new com.finance.anubis.core.task.model.Task()
        newTask.setTaskType(com.finance.anubis.core.constants.enums.TaskType.ONLINE)
        newTask.setTaskConfig(task.getConfig())
        when:
        def res = taskRepository.selectTaskPage(limit, newTask)
        then:
        print(res)
    }

    def selectTaskPageNull() {
        given:
        def limit = new Limit(2, 0, 2)
        when:
        def res = taskRepository.selectTaskPage(limit, null)
        then:
        res.size() == 0
    }

    def selectTaskPage() {
        given:
        taskRepository.add(task).getId()
        def limit = new Limit(1, 0, 2)
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        taskMapper.selectPagesByParams(_, _, _) >> Collections.singletonList(TaskDTO.toEntity(task))
        taskConfigMapper.selectListByIds(_) >> Collections.singletonList(taskConfigEntity)
        when:
        def res = taskRepository.selectTaskPage(limit, task)
        then:
        print(res)
    }

    def selectTaskPageError() {
        given:
        taskRepository.add(task).getId()
        def limit = new Limit(1, 0, 2)
        def mapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        mapper.selectPagesByParams(_, _, _) >> Collections.singletonList(TaskDTO.toEntity(task))
        taskConfigMapper.selectListByIds(_) >> Collections.emptyList()
        taskRepository = new TaskRepositoryImpl(mapper, taskConfigMapper)

        when:
        try {
            taskRepository.selectTaskPage(limit, task)
        } catch (StatusCodeException e) {
            print(e.getStatusCode().getReason())
        }
        then:
        true
    }

    def selectTaskByParam() {
        given:
        def id = taskRepository.add(task).getId()
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        def param = new TaskEntity()
        param.setId(id)
        taskConfigMapper.selectListByIds(_) >> Collections.singletonList(taskConfigEntity)
        when:
        def res = taskRepository.selectTaskByParam(param)
        then:
        print(res)
    }

    def selectTaskByParamNull() {
        given:

        when:
        def res = taskRepository.selectTaskByParam(null)
        then:
        res.size() == 0
    }

    def selectTaskByParamEmpty() {
        given:
        def id = taskRepository.add(task).getId()
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        def param = new TaskEntity()
        param.setId(id)
        taskMapper.selectByParams(_) >> Collections.emptyList()
        taskConfigMapper.selectListByIds(_) >> Collections.singletonList(taskConfigEntity)
        when:
        def res = taskRepository.selectTaskByParam(param)
        then:
        res.size() == 0
    }

    def selectTaskByParamError() {
        given:
        def id = taskRepository.add(task).getId()
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        def param = new TaskEntity()
        param.setId(id)
        taskMapper.selectByParams(_) >> Collections.singletonList(TaskDTO.toEntity(task))
        taskConfigMapper.selectListByIds(_) >> Collections.emptyList()
        when:
        try {
            taskRepository.selectTaskByParam(param)
        } catch (StatusCodeException e) {
            print(e.getStatusCode().getReason())
        }
        then:
        true
    }

    def start() {
        given:
        def id = taskRepository.add(task).getId()
        when:
        taskRepository.start(id)
        then:
        true
    }

    def startNull() {
        given:
        when:
        taskRepository.start(100)
        then:
        true
    }

    def stop() {
        given:
        def id = taskRepository.add(task).getId()
        when:
        taskRepository.stop(id)
        then:
        true
    }

    def stopNull() {
        given:
        when:
        taskRepository.stop(100)
        then:
        true
    }

    def activeTaskList() {
        given:
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        taskMapper.listAllByStatus(_, _) >> Collections.singletonList(TaskDTO.toEntity(task))
        taskConfigMapper.selectListByIds(_) >> Collections.singletonList(taskConfigEntity)
        new MockUp<TaskDTO>() {
            @Mock
            static com.finance.anubis.core.task.model.Task toModel(TaskEntity taskEntity, TaskConfigEntity taskConfigEntity) {
                return new com.finance.anubis.core.task.model.Task();
            }
        }
        when:
        def res = taskRepository.activeTaskList()
        then:
        print(res)
    }

    def activeTaskListEmpty() {
        given:
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        taskMapper.listAllByStatus(_, _) >> Collections.emptyList()
        when:
        def res = taskRepository.activeTaskList()
        then:
        res.size() == 0
    }

    def activeOffLineTaskList() {
        given:
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        taskMapper.listAllByStatus(_, _) >> Collections.singletonList(TaskDTO.toEntity(task))
        def entity = new TaskConfigEntity()
        entity.setSourceConfig("{\"key\":\"source\",\"url\":\"http://liq-service/verifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":1000}")
        entity.setTargetConfig("{\"key\":\"target\",\"url\":\"http://liq-service/verifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":1000}")
        entity.setId(1)
        taskConfigMapper.selectListByIds(_) >> Collections.singletonList(entity)
        when:
        def res = taskRepository.activeOffLineTaskList()
        then:
        res.size() == 1
    }

    def activeOffLineTaskListEmpty() {
        given:
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        taskMapper.listAllByStatus(_, _) >> Collections.emptyList()
        when:
        def res = taskRepository.activeOffLineTaskList()
        then:
        res.size() == 0
    }

    def selectByName() {
        given:
        def taskMapper = Mock(com.finance.anubis.repository.mapper.TaskMapper)
        def taskConfigMapper = Mock(TaskConfigMapper)
        def entity = new TaskConfigEntity()
        entity.setSourceConfig("{\"key\":\"source\",\"url\":\"http://liq-service/verifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":1000}")
        entity.setTargetConfig("{\"key\":\"target\",\"url\":\"http://liq-service/verifyTaskFile\",\"method\":\"POST\",\"requestParamMapping\":{\"fileUrl\":\"\$.fileUrl\"},\"resourceType\":\"OffLineFileResource\",\"sortKeyMap\":{\"orderId\":\"ASC\"},\"compareTotalKeys\":[\"orderAmt\"],\"computeExpressions\":\"orderAmt\",\"compareDetailKeys\":[\"orderId\",\"orderAmt\",\"payChannel\"],\"keyList\":[\"orderId\",\"orderAmt\",\"payChannel\",\"orderType\",\"rateAmt\"],\"fileFormatSplit\":\",\",\"fileType\":\"CSV\",\"skipHead\":\"0\",\"skipTail\":\"0\",\"fetchDelay\":1000}")
        entity.setId(1)
        taskRepository = new TaskRepositoryImpl(taskMapper, taskConfigMapper)
        taskMapper.selectByTaskConfigId(_) >> TaskDTO.toEntity(task)
        taskConfigMapper.selectByTaskName(_) >> entity
        when:
        def res = taskRepository.selectByName(task.getTaskConfig().getName())
        then:
        print(res)
    }
    def selectByNameNull() {
        given:
        when:
        def res = taskRepository.selectByName("")
        then:
        res==null
    }
    def selectByNameEmpty() {
        given:
        when:
        def res = taskRepository.selectByName("123")
        then:
        res==null
    }
}
