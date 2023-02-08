package com.xiekun.dealer.service.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xiekun.dealer.common.constant.AustinConstant;
import com.xiekun.dealer.common.enums.RespStatusEnum;
import com.xiekun.dealer.common.vo.BasicResultVO;
import com.xiekun.dealer.service.domain.MessageParam;
import com.xiekun.dealer.service.impl.domain.SendTaskModel;
import com.xiekun.dealer.support.pipeline.BusinessProcess;
import com.xiekun.dealer.support.pipeline.ProcessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 3y
 * @date 2021/11/22
 * @description 前置参数校验
 */
@Slf4j
@Service
public class PreParamCheckAction implements BusinessProcess<SendTaskModel> {

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();

        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();

        // 1. 没有传入消息模板 id 或者 messageParam
        if (Objects.isNull(messageTemplateId) || CollUtil.isEmpty(messageParamList)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }

        // 2. 过滤 receiver == null 的 messageParam
        List<MessageParam> resultMessageParamList = messageParamList.stream()
                .filter(messageParam -> !StrUtil.isBlank(messageParam.getReceiver()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(resultMessageParamList)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }

        // 3. 过滤 receiver 大于 100 的请求
        if (resultMessageParamList.stream().anyMatch(messageParam ->
                messageParam.getReceiver().split(StrUtil.COMMA).length > AustinConstant.BATCH_RECEIVER_SIZE)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.TOO_MANY_RECEIVER));
            return;
        }

        sendTaskModel.setMessageParamList(resultMessageParamList);
    }
}