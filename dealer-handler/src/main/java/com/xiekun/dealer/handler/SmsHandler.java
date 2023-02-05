package com.xiekun.dealer.handler;

import cn.hutool.core.collection.CollUtil;
import com.xiekun.dealer.support.dao.SmsRecordDao;
import com.xiekun.dealer.support.domain.SmsRecord;
import com.xiekun.dealer.common.pojo.SmsParam;
import com.xiekun.dealer.common.pojo.TaskInfo;
import com.xiekun.dealer.handler.script.SmsScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmsHandler implements Handler {
    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScript smsScript;

    @Override
    public boolean doHandler(TaskInfo taskInfo) {

        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(taskInfo.getContent())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .supplierId(10)
                .supplierName("腾讯云通知类消息渠道").build();
        List<SmsRecord> recordList = smsScript.send(smsParam);

        if (CollUtil.isNotEmpty(recordList)) {
            smsRecordDao.saveAll(recordList);
            return true;
        }

        return false;
    }
}