package com.xiekun.dealer.handler.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于记录 channel->Handler 的映射关系
 */
@Component
public class HandlerHolder {
    private final Map<Integer, Handler> handlers = new HashMap<>(128);

    public void putHandler(Integer channelCode, Handler handler) {
        handlers.put(channelCode, handler);
    }

    /**
     * 根据发送渠道 Id 获取对应的 Handler 实例对象
     */
    public Handler route(Integer channelCode) {
        return handlers.get(channelCode);
    }
}
