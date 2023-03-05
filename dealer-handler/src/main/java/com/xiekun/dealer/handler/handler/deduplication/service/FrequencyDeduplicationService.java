package com.xiekun.dealer.handler.handler.deduplication.service;

import cn.hutool.core.util.StrUtil;
import com.xiekun.dealer.common.domain.TaskInfo;

public class FrequencyDeduplicationService extends AbstractDeduplicationService {

    private static final String PREFIX = "FRE";

    /**
     * 构建业务规则去重 key
     * <p>
     * key ： receiver + templateId + sendChannel
     * <p>
     * 一天内一个用户只能收到某个渠道的消息 N 次
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
