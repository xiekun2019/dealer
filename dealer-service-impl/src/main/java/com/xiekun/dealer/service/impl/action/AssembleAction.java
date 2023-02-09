package com.xiekun.dealer.service.impl.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiekun.dealer.common.constant.CommonConstant;
import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.common.dto.model.ContentModel;
import com.xiekun.dealer.common.enums.ChannelType;
import com.xiekun.dealer.common.enums.RespStatusEnum;
import com.xiekun.dealer.common.vo.BasicResultVO;
import com.xiekun.dealer.service.domain.MessageParam;
import com.xiekun.dealer.service.enums.BusinessCode;
import com.xiekun.dealer.service.impl.domain.SendTaskModel;
import com.xiekun.dealer.support.dao.MessageTemplateDao;
import com.xiekun.dealer.support.domain.MessageTemplate;
import com.xiekun.dealer.support.pipeline.BusinessProcess;
import com.xiekun.dealer.support.pipeline.ProcessContext;
import com.xiekun.dealer.support.utils.TaskInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class AssembleAction implements BusinessProcess<SendTaskModel> {
    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Override
    public void process(ProcessContext<SendTaskModel> context) {
        SendTaskModel sendTaskModel = context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();

        // 查询数据库，看是否有这个 messageTemplate
        Optional<MessageTemplate> messageTemplate = messageTemplateDao.findById(messageTemplateId);
        if (messageTemplate.isEmpty() || messageTemplate.get().getIsDeleted().equals(CommonConstant.TRUE)) {
            context.setNeedBreak(true).setResponse(BasicResultVO.fail(RespStatusEnum.TEMPLATE_NOT_FOUND));
            return;
        }

        if (BusinessCode.COMMON_SEND.getCode().equals(context.getCode())) {

        }
    }

    /**
     * 组装 TaskInfo 任务消息
     *
     * @param sendTaskModel
     * @param messageTemplate
     */
    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplate messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();

        for (MessageParam messageParam : messageParamList) {

            TaskInfo taskInfo = TaskInfo.builder()
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .idType(messageTemplate.getIdType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .templateType(messageTemplate.getTemplateType())
                    .msgType(messageTemplate.getMsgType())
                    .shieldType(messageTemplate.getShieldType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam)).build();

            taskInfoList.add(taskInfo);
        }

        return taskInfoList;
    }

    /**
     * 获取 contentModel，替换模板msgContent中占位符信息
     */
    private static ContentModel getContentModelValue(MessageTemplate messageTemplate, MessageParam messageParam) {

        // 得到真正的ContentModel 类型
        Integer sendChannel = messageTemplate.getSendChannel();
        Class<? extends ContentModel> contentModelClass = ChannelType.getChanelModelClassByCode(sendChannel);

        // 得到模板的 msgContent 和 入参
        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());


        // 通过反射 组装出 contentModel
        Field[] fields = ReflectUtil.getFields(contentModelClass);
        ContentModel contentModel = ReflectUtil.newInstance(contentModelClass);
        for (Field field : fields) {
            String originValue = jsonObject.getString(field.getName());

            if (StrUtil.isNotBlank(originValue)) {
                String resultValue = ContentHolderUtil.replacePlaceHolder(originValue, variables);
                Object resultObj = JSONUtil.isJsonObj(resultValue) ? JSONUtil.toBean(resultValue, field.getType()) : resultValue;
                ReflectUtil.setFieldValue(contentModel, field, resultObj);
            }
        }

        // 如果 url 字段存在，则在url拼接对应的埋点参数
        String url = (String) ReflectUtil.getFieldValue(contentModel, LINK_NAME);
        if (StrUtil.isNotBlank(url)) {
            String resultUrl = TaskInfoUtils.generateUrl(url, messageTemplate.getId(), messageTemplate.getTemplateType());
            ReflectUtil.setFieldValue(contentModel, LINK_NAME, resultUrl);
        }
        return contentModel;
    }
}
