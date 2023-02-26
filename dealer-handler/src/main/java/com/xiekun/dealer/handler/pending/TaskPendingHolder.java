package com.xiekun.dealer.handler.pending;

import com.xiekun.dealer.handler.handler.config.HandlerThreadPoolConfig;
import com.xiekun.dealer.handler.utils.GroupIdMappingUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class TaskPendingHolder {
    private final Map<String, ExecutorService> threadHolder =
            new HashMap<>(32);

    /**
     * 获取得到所有的 groupId
     */
    private static final List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 给每个发送消息的渠道，每种消息类型都初始化一个线程池
     */
    @PostConstruct
    public void init() {
        for (String groupId : groupIds) {
            ThreadPoolExecutor executor = HandlerThreadPoolConfig.getExecutor();
            threadHolder.put(groupId, executor);
        }
    }

    /**
     * 根据 groupId 获取线程
     */
    public ExecutorService route(String groupId) {
        return threadHolder.get(groupId);
    }
}
