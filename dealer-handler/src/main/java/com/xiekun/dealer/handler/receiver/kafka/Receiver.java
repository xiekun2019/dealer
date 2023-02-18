package com.xiekun.dealer.handler.receiver.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    // 消费监听
    @KafkaListener(id = "consumer1", groupId = "my-group1", topics = "#{'${dealer.business.topic.name}'}")
    public void listen1(String data) {
        System.out.println("消费：" + data);
    }
}