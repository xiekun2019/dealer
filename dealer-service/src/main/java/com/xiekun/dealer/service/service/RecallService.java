package com.xiekun.dealer.service.service;

import com.xiekun.dealer.service.domain.SendRequest;
import com.xiekun.dealer.service.domain.SendResponse;

/**
 * 撤回接口
 *
 * @author 3y
 */
public interface RecallService {


    /**
     * 根据模板ID撤回消息
     *
     * @param sendRequest
     * @return
     */
    SendResponse recall(SendRequest sendRequest);
}