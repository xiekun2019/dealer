package com.xiekun.dealer.support.utils;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RedisUtils {
    private static final String TAG = "RedisUtils";

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 将从 redis 获取的结果封装为 Map
     */
    public Map<String, String> mGet(List<String> keys) {
        HashMap<String, String> result = new HashMap<>(keys.size());
        try {
            List<String> value = redisTemplate.opsForValue().multiGet(keys);
            assert value != null;
            if (CollUtil.isNotEmpty(value)) {
                for (int i = 0; i < keys.size(); i++) {
                    result.put(keys.get(i), value.get(i));
                }
            }
        } catch (Exception e) {
            log.error(TAG + "#mGet fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }

    /**
     * pipeline 设置 key-value 并设置过期时间
     * @param keyValues 用于插入 redis 的 key-value 数据
     * @param seconds 过期时间
     */
    public void pipelineSetExpireTime(Map<String, String> keyValues, Long seconds) {
        try {
            redisTemplate.executePipelined(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                        connection.setEx(entry.getKey().getBytes(), seconds, entry.getValue().getBytes());
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            log.error(TAG + "#pipelineSetExpireTime fail! e:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
