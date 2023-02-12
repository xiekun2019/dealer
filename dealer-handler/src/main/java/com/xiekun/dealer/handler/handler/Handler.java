package com.xiekun.dealer.handler.handler;

import com.xiekun.dealer.common.domain.TaskInfo;

public interface Handler {
    boolean doHandler(TaskInfo TaskInfo);
}