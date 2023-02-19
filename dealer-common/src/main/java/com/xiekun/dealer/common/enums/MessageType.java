package com.xiekun.dealer.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * `msg_type`           tinyint(4)
 * 发送的消息类型
 * NOT NULL DEFAULT '0' COMMENT '10.通知类消息 20.营销类消息 30.验证码类消息',
 */
@Getter
@ToString
@AllArgsConstructor
public enum MessageType {
    NOTICE(10, "通知类消息", "notice"),
    MARKETING(20, "营销类消息", "marketing"),
    AUTH_CODE(30, "验证码消息", "auth_code");

    /**
     * 编码值
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String description;
    /**
     * 英文标识
     */
    private final String codeEn;

    /**
     * 通过 code 获取 enum
     *
     * @param code 编码值
     * @return enum 类型
     */
    public static MessageType getEnumByCode(Integer code) {
        MessageType[] values = values();
        for (MessageType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}