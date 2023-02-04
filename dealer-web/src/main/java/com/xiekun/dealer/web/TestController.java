package com.xiekun.dealer.web;

import com.xiekun.dealer.handler.TencentSmsScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
