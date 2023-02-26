package com.xiekun.dealer.handler.receiver.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.handler.pending.Task;
import com.xiekun.dealer.handler.pending.TaskPendingHolder;
import com.xiekun.dealer.handler.receiver.service.ConsumeService;
import com.xiekun.dealer.handler.utils.GroupIdMappingUtils;
import com.xiekun.dealer.support.domain.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private TaskPendingHolder taskPendingHolder;

    @Autowired
    private ApplicationContext context;

    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {
        String topicGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(CollUtil.getFirst(taskInfoLists.iterator()));
        for (TaskInfo taskInfo : taskInfoLists) {
            Task task = context.getBean(Task.class).setTaskInfo(taskInfo);
            // 根据 topicGroupId 获取线程池后执行 task
            taskPendingHolder.route(topicGroupId).execute(task);
        }
    }

    @Override
    public void consume2recall(MessageTemplate messageTemplate) {

    }
}
