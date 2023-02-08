package com.xiekun.dealer.service.impl.config;

import com.xiekun.dealer.service.impl.action.PreParamCheckAction;
import com.xiekun.dealer.support.pipeline.ProcessTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class PipelineConfig {

    @Autowired
    private PreParamCheckAction preParamCheckAction;


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
        processTemplate.setProcessList(Arrays.asList(preParamCheckAction));

        return processTemplate;
    }
}
