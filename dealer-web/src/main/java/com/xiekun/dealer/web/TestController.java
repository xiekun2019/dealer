package com.xiekun.dealer.web;

import com.xiekun.dealer.handler.TencentSmsScript;
import com.xiekun.dealer.pojo.SmsParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private TencentSmsScript tencentSmsScript;

    @RequestMapping("/test")
    private String test() {
        log.info("你好呀，罗蕾");
        return "Dealer test";
    }

    @RequestMapping("/hello")
    private String hello() {
        SmsParam smsParam = SmsParam.builder()
                .phones(new HashSet<>(List.of("15979083045")))
                .content("1218")
                .build();

        return tencentSmsScript.send(smsParam);
    }
}
