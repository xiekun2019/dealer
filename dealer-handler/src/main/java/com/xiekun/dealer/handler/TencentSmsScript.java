package com.xiekun.dealer.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.xiekun.dealer.pojo.SmsParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TencentSmsScript {
    /**
     * 通用参数
     */
    private static final String URL = "sms.tencentcloudapi.com";
    private static final String REGION = "ap-guangzhou";

    /**
     * 账号相关
     */
    private final static String SECRET_ID = "AKIDc6RelofKG82w1T3Xo3Oh0a3KQL7jJjww";
    private final static String SECRET_KEY = "wkOuFncSAgzCGg1ED2gCYgdP2Vq0Kftt";
    private static final String SMS_SDK_APP_ID = "1400792521";
    private static final String TEMPLATE_ID = "1676998";
    private static final String SIGN_NAME = "xiekun个人公众号";

    public String send(SmsParam smsParam) {
        try {
            /**
             * 初始化 client
             */
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(URL);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, REGION, clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
            req.setPhoneNumberSet(phoneNumberSet1);
            req.setSmsSdkAppId(SMS_SDK_APP_ID);
            req.setSignName(SIGN_NAME);
            req.setTemplateId(TEMPLATE_ID);

            String[] templateParamSet1 = {"6666"};
            req.setTemplateParamSet(templateParamSet1);

            SendSmsResponse resp = client.SendSms(req);
            System.out.println(SendSmsResponse.toJsonString(resp));
            return SendSmsResponse.toJsonString(resp);

        } catch (TencentCloudSDKException e) {
            log.error("send tencent sms fail!{}, params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return null;
        }
    }
}
