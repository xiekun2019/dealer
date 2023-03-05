package com.xiekun.dealer.handler.handler.deduplication.service;

import com.xiekun.dealer.handler.handler.deduplication.DeduplicationParam;

public interface DeduplicationService {

    /**
     * 去重方法抽象
     */
    void deduplication(DeduplicationParam param);
}
