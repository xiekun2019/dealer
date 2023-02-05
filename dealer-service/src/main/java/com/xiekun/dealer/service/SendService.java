package com.xiekun.dealer.service;

import com.xiekun.dealer.domain.BatchSendRequest;
import com.xiekun.dealer.domain.SendRequest;
import com.xiekun.dealer.domain.SendResponse;

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
