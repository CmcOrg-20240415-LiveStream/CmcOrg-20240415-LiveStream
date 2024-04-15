package com.cmcorg20240415.livestream.douyu.model.bo;

import com.cmcorg20240415.livestream.douyu.model.enums.MessageTypeEnum;

import lombok.Data;

@Data
public class MessageBO {

    /**
     * 消息类型
     */
    private MessageTypeEnum type;

    /**
     * 消息值
     */
    private String value;

}
