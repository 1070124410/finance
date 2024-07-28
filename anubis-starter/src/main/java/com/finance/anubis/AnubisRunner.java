package com.finance.anubis;

import com.finance.anubis.core.TaskInitializer;
import com.finance.anubis.core.model.Task;
import com.finance.anubis.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AnubisRunner implements ApplicationRunner {

    @Autowired
    private TaskInitializer taskInitializer;

    @Resource
    private TaskRepository taskRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Task> tasks = taskRepository.activeTaskList();
        tasks.forEach(task -> {
            taskInitializer.startTask(task);
        });
    }
}
