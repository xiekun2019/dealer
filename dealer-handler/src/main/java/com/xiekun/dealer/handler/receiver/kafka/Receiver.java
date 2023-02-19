package com.xiekun.dealer.handler.receiver.kafka;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.handler.receiver.service.ConsumeService;
import com.xiekun.dealer.handler.utils.GroupIdMappingUtils;
import com.xiekun.dealer.support.constant.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(name = "dealer.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class Receiver {
    @Autowired
    private ConsumeService consumeService;

    // 消费监听
    @KafkaListener(topics = "#{'${dealer.business.topic.name}'}")
    public void consumer(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfoList = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            TaskInfo firstTaskInfo = CollUtil.getFirst(taskInfoList.iterator());
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(firstTaskInfo);
            // 每个消费者组只消费对应的消息
            if(topicGroupId.equals(messageGroupId)) {
                consumeService.consume2Send(taskInfoList);
            }
        }
    }
}