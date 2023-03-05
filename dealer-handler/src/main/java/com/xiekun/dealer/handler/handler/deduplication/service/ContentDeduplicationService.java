package com.xiekun.dealer.handler.handler.deduplication.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.xiekun.dealer.common.domain.TaskInfo;
import org.springframework.stereotype.Service;

@Service
public class ContentDeduplicationService extends AbstractDeduplicationService {

    /**
     * 构建内容去重 key
     * <p>
     * key：md5(templateId + receiver + content)
     * <p>
     * 相同的内容、相同的模板，在短时间内发给同一个接收者
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(taskInfo.getMessageTemplateId() + receiver
                + JSON.toJSONString(taskInfo.getContentModel()));
    }
}
