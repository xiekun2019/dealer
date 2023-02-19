package com.xiekun.dealer.handler.utils;

import com.xiekun.dealer.common.domain.TaskInfo;
import com.xiekun.dealer.common.enums.ChannelType;
import com.xiekun.dealer.common.enums.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * groupId 标识着每一个消费者组
 *
 * @author 3y
 */
public class GroupIdMappingUtils {

    /**
     * 获取所有的groupIds
     * (不同的渠道不同的消息类型拥有自己的groupId)
     */
    public static List<String> getAllGroupIds() {
        List<String> groupIds = new ArrayList<>();
        for (ChannelType channelType : ChannelType.values()) {
            for (MessageType messageType : MessageType.values()) {
                groupIds.add(channelType.getCodeEn() + "." + messageType.getCodeEn());
            }
        }
        return groupIds;
    }


    /**
     * 根据 TaskInfo 获取当前消息的 groupId，例如短信通知类消息的消费者组为：sms.notice
     *
     * @param taskInfo
     * @return
     */
    public static String getGroupIdByTaskInfo(TaskInfo taskInfo) {
        String channelCodeEn = Objects.requireNonNull(ChannelType.getEnumByCode(taskInfo.getSendChannel())).getCodeEn();
        String msgCodeEn = Objects.requireNonNull(MessageType.getEnumByCode(taskInfo.getMsgType())).getCodeEn();
        return channelCodeEn + "." + msgCodeEn;
    }
}