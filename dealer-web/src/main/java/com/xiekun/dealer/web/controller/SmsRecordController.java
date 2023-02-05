package com.xiekun.dealer.web.controller;

import com.xiekun.dealer.support.dao.SmsRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class SmsRecordController {

    @Autowired
    private SmsRecordDao smsRecordDao;

    /**
     * test insert
     */
    @GetMapping("/insert")
    public String insert(Integer phone) {
        return null;
    }

    /**
     * test query
     */
    @GetMapping("/query")
    public String query() {

        return null;
    }
}