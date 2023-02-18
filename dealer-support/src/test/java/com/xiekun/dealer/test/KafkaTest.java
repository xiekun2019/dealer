package com.xiekun.dealer.test;

import com.xiekun.dealer.support.mq.kafka.KafkaSendMqServiceImpl;

public class KafkaTest {

    public static void main(String[] args) {
        KafkaSendMqServiceImpl sendMqService = new KafkaSendMqServiceImpl();
        sendMqService.send("austinBusiness", "{a:b}", "kafka_tag_id");
    }
}
