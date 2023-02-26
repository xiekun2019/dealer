package com.xiekun.dealer.handler.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.common.dto.model.SmsContentModel;
import com.xiekun.dealer.common.enums.ChannelType;
import com.xiekun.dealer.common.pojo.SmsParam;
import com.xiekun.dealer.handler.handler.BaseHandler;
import com.xiekun.dealer.handler.handler.Handler;
import com.xiekun.dealer.handler.script.SmsScript;
import com.xiekun.dealer.support.dao.SmsRecordDao;
import com.xiekun.dealer.support.domain.SmsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmsHandler extends BaseHandler implements Handler {

    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    @Autowired
    private SmsRecordDao smsRecordDao;

    @Autowired
    private SmsScript smsScript;

    @Override
    public boolean handler(TaskInfo taskInfo) {
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .build();

        List<SmsRecord> recordList = smsScript.send(smsParam);
        if (CollUtil.isNotEmpty(recordList)) {
            smsRecordDao.saveAll(recordList);
            return true;
        }
        return false;
    }

    /**
     * 根据 TaskInfo 获取需要发送的信息，如果有 URL 信息拼装
     * @param taskInfo
     * @return
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + StrUtil.SPACE + smsContentModel.getUrl();
        }
        return smsContentModel.getContent();
    }
}