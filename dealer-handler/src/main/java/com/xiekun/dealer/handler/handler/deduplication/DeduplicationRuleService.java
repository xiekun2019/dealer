package com.xiekun.dealer.handler.handler.deduplication;

import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.handler.handler.deduplication.service.ContentDeduplicationService;
import com.xiekun.dealer.handler.handler.deduplication.service.FrequencyDeduplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeduplicationRuleService {
    @Value("${deduplicationRule.num}")
    private String num;

    @Value("${deduplicationRule.time}")
    private String time;

    @Autowired
    private ContentDeduplicationService contentDeduplicationService;

    @Autowired
    private FrequencyDeduplicationService frequencyDeduplicationService;

    public void duplication(TaskInfo taskInfo) {
        // 构造 DeduplicationParam 对象
        DeduplicationParam param = new DeduplicationParam();
        param.setDeduplicationTime(Long.valueOf(time));
        param.setCountNum(Integer.parseInt(num));
        param.setTaskInfo(taskInfo);
        // 分别去重
        contentDeduplicationService.deduplication(param);
        frequencyDeduplicationService.deduplication(param);
    }
}
