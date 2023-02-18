package com.xiekun.dealer.service.impl.config;

import com.xiekun.dealer.service.enums.BusinessCode;
import com.xiekun.dealer.service.impl.action.AfterParamCheckAction;
import com.xiekun.dealer.service.impl.action.AssembleAction;
import com.xiekun.dealer.service.impl.action.PreParamCheckAction;
import com.xiekun.dealer.service.impl.action.SendMqAction;
import com.xiekun.dealer.support.pipeline.ProcessController;
import com.xiekun.dealer.support.pipeline.ProcessTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PipelineConfig {

    @Autowired
    private PreParamCheckAction preParamCheckAction;

    @Autowired
    private AssembleAction assembleAction;

    @Autowired
    private AfterParamCheckAction afterParamCheckAction;

    @Autowired
    private SendMqAction sendMqAction;

    /**
     * 默认发送流程
     * 1. 前置参数校验
     * 2. 组装参数
     * 3. 后置参数校验
     * 4. 发送消息值 MQ
     *
     * @return
     */
    @Bean("defaultSendTemplate")
    public ProcessTemplate defaultSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(preParamCheckAction, assembleAction, afterParamCheckAction, sendMqAction));

        return processTemplate;
    }

    /**
     * 消息撤回执行流程
     * 1.组装参数
     * 2.发送MQ
     *
     * @return
     */
    @Bean("recallMessageTemplate")
    public ProcessTemplate recallMessageTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList());
        return processTemplate;
    }

    /**
     * pipeline流程控制器
     * 后续扩展则加BusinessCode和ProcessTemplate
     *
     * @return
     */
    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), defaultSendTemplate());
        templateConfig.put(BusinessCode.RECALL.getCode(), recallMessageTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }
}
