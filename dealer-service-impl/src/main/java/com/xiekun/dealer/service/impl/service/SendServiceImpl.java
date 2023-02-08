package com.xiekun.dealer.service.impl.service;

import com.xiekun.dealer.common.vo.BasicResultVO;
import com.xiekun.dealer.service.domain.BatchSendRequest;
import com.xiekun.dealer.service.domain.SendRequest;
import com.xiekun.dealer.service.domain.SendResponse;
import com.xiekun.dealer.service.impl.domain.SendTaskModel;
import com.xiekun.dealer.service.service.SendService;
import com.xiekun.dealer.support.pipeline.ProcessContext;
import com.xiekun.dealer.support.pipeline.ProcessController;
import com.xiekun.dealer.support.pipeline.ProcessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SendServiceImpl implements SendService {

    @Autowired
    private ProcessController processController;

    @Override
    public SendResponse send(SendRequest sendRequest) {

        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .messageParamList(Collections.singletonList(sendRequest.getMessageParam()))
                .build();

        ProcessContext<ProcessModel> context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success())
                .build();

        ProcessContext process = processController.process(context);

        return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg());
    }

    @Override
    public SendResponse batchSend(BatchSendRequest batchSendRequest) {
        return null;
    }
}
