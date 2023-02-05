package com.xiekun.dealer.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.xiekun.dealer.constant.RespStatusEnum;
import com.xiekun.dealer.dao.MessageTemplateDao;
import com.xiekun.dealer.domain.MessageParam;
import com.xiekun.dealer.domain.MessageTemplate;
import com.xiekun.dealer.domain.SendRequest;
import com.xiekun.dealer.domain.SendResponse;
import com.xiekun.dealer.enums.BusinessCode;
import com.xiekun.dealer.pojo.vo.BasicResultVO;
import com.xiekun.dealer.service.SendService;
import com.xiekun.dealer.vo.MessageTemplateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class MessageTemplateController {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Autowired
    private SendService sendService;
    /**
     * test insert
     */
    @GetMapping("/insert")
    public String insert() {

        MessageTemplate messageTemplate = MessageTemplate.builder()
                .name("test短信")
                .auditStatus(10)
                .flowId("yyyy")
                .msgStatus(10)
                .idType(10)
                .sendChannel(10)
                .templateType(10)
                .msgType(10)
                .expectPushTime("0")
                .msgContent("3333333m")
                .sendAccount(66)
                .creator("yyyyc")
                .updater("yyyyu")
                .team("yyyt")
                .proposer("yyyy22")
                .auditor("yyyyyyz")
                .isDeleted(0)
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .deduplicationTime(1)
                .isNightShield(0)
                .build();

        MessageTemplate info = messageTemplateDao.save(messageTemplate);

        return JSON.toJSONString(info);

    }

    /**
     * test query
     */
    @GetMapping("/query")
    public String query() {
        Iterable<MessageTemplate> all = messageTemplateDao.findAll();
        return all.toString();
//        for (MessageTemplate messageTemplate : all) {
//            return JSON.toJSONString(messageTemplate);
//        }
        //return null;
    }

    public BasicResultVO sendServiceTest(MessageTemplateParam messageTemplateParam) {
        Map<String, String> variables = JSON.parseObject(messageTemplateParam.getMsgContent(), Map.class);
        MessageParam messageParam = MessageParam.builder().receiver(messageTemplateParam.getReceiver())
                        .variables(variables).build();
        SendRequest sendRequest = SendRequest.builder().code(BusinessCode.COMMON_SEND.getCode())
                .messageTemplateId(messageTemplateParam.getId()).messageParam(messageParam).build();
        SendResponse response = sendService.send(sendRequest);
        if (!Objects.equals(response.getCode(), RespStatusEnum.SUCCESS.getCode())) {
            return BasicResultVO.fail(response.getMsg());
        }
        return BasicResultVO.success(response);
    }
}