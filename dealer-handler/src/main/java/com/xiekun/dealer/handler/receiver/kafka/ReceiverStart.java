package com.xiekun.dealer.handler.receiver.kafka;

import com.xiekun.dealer.handler.utils.GroupIdMappingUtils;
import com.xiekun.dealer.support.constant.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
@ConditionalOnProperty(name = "dealer.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
@Slf4j
public class ReceiverStart {

    /**
     * receiver 的消费方法常量
     */
    private static final String RECEIVER_METHOD_NAME = "Receiver.consumer";

    /**
     * 获取得到所有的groupId
     */
    private static final List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 下标(用于迭代 groupIds 位置)
     */
    private static Integer index = 0;

    /**
     * 给每个 Receiver 对象的 consumer 方法 @KafkaListener 赋值相应的 groupId
     */
    @Bean
    public static KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer groupIdEnhancer() {
        return (attrs, element) -> {
            if (element instanceof Method) {
                String name = ((Method) element).getDeclaringClass().getSimpleName() + "." + ((Method) element).getName();
                if (RECEIVER_METHOD_NAME.equals(name)) {
                    attrs.put("groupId", groupIds.get(index++));
                }
            }
            return attrs;
        };
    }
}
