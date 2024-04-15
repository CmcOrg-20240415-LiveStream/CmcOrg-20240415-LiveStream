package com.cmcorg20240415.livestream.douyu.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    HEART(101), // 心跳检测

    JOIN_ROOM(201), // 新用户加入房间

    MESSAGE(301), // 新消息

    ;

    private final int code;

}
