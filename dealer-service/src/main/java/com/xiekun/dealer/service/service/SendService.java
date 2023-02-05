package com.xiekun.dealer.service.service;

import com.xiekun.dealer.service.domain.BatchSendRequest;
import com.xiekun.dealer.service.domain.SendRequest;
import com.xiekun.dealer.service.domain.SendResponse;

public interface SendService {

    /**
     * 单文案发送接口
     * @param sendRequest
     * @return
     */
    SendResponse send(SendRequest sendRequest);


    /**
     * 多文案发送接口
     * @param batchSendRequest
     * @return
     */
    SendResponse batchSend(BatchSendRequest batchSendRequest);
}
