package com.xiekun.dealer.handler.script;

import com.xiekun.dealer.support.domain.SmsRecord;
import com.xiekun.dealer.common.pojo.SmsParam;

import java.util.List;

public interface SmsScript {


    /**
     * @param smsParam 发送短信参数
     * @return 渠道商接口返回值
     */
    List<SmsRecord> send(SmsParam smsParam);

}