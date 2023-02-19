package com.xiekun.dealer.handler.receiver.service.impl;

import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.handler.receiver.service.ConsumeService;
import com.xiekun.dealer.support.domain.MessageTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumeServiceImpl implements ConsumeService {
    @Override
    public void consume2Send(List<TaskInfo> taskInfoLists) {

    }

    @Override
    public void consume2recall(MessageTemplate messageTemplate) {

    }
}
