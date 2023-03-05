package com.xiekun.dealer.handler.handler.deduplication.service;

import cn.hutool.core.collection.CollUtil;
import com.xiekun.dealer.common.constant.CommonConstant;
import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.handler.handler.deduplication.DeduplicationParam;
import com.xiekun.dealer.support.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public abstract class AbstractDeduplicationService implements DeduplicationService {

    private static final String LIMIT_TAG = "SP_";

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void deduplication(DeduplicationParam param) {
        TaskInfo taskInfo = param.getTaskInfo();
        // 用来存储需要过滤的接收者
        HashSet<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());

        // 获取 redis 记录
        Map<String, String> readyToPutRedisReceiver = new HashMap<>(taskInfo.getReceiver().size());
        List<String> keys = deduplicationAllKey(taskInfo);
        Map<String, String> inRedisValue = redisUtils.mGet(keys);

        for (String receiver : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(taskInfo, receiver);
            String value = inRedisValue.get(key);

            if (value != null && Integer.parseInt(value) >= param.getCountNum()) {
                // 符合条件的用户
                filterReceiver.add(receiver);
            } else {
                // 不符合条件的用户，可以发送信息
                readyToPutRedisReceiver.put(receiver, key);
            }
        }
        // 不符合条件的用户：需要更新Redis(无记录添加，有记录则累加次数)
        putInRedis(readyToPutRedisReceiver, inRedisValue, param.getDeduplicationTime());
        // 剔除符合去重条件的用户
        taskInfo.getReceiver().removeAll(filterReceiver);
    }

    private void putInRedis(Map<String, String> readyToPutRedisReceiver,
                            Map<String, String> inRedisValue,
                            Long deduplicationTime) {
        // 用于存储需要插入到 redis 的信息
        Map<String, String> keyValues = new HashMap<>(readyToPutRedisReceiver.size());
        for (Map.Entry<String, String> entry : readyToPutRedisReceiver.entrySet()) {
            String key = entry.getValue();
            if(Objects.nonNull(inRedisValue.get(key))) {
                keyValues.put(key, String.valueOf(Integer.parseInt(inRedisValue.get(key)) + 1));
            } else {
                keyValues.put(key, String.valueOf(CommonConstant.TRUE));
            }
        }
        if (CollUtil.isNotEmpty(keyValues)) {
            redisUtils.pipelineSetExpireTime(keyValues, deduplicationTime);
        }
    }

    /**
     * 获取得到当前消息模板所有的去重 Key
     */
    protected List<String> deduplicationAllKey(TaskInfo taskInfo) {
        List<String> result = new ArrayList<>(taskInfo.getReceiver().size());
        for (String receiver : taskInfo.getReceiver()) {
            String key = deduplicationSingleKey(taskInfo, receiver);
            result.add(key);
        }
        return result;
    }

    public abstract String deduplicationSingleKey(TaskInfo taskInfo, String receiver);
}
