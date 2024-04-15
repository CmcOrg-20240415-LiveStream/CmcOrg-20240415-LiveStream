package com.cmcorg20240415.livestream.ai.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 助手消息内容类型，枚举类
 */
@AllArgsConstructor
@Getter
public enum AIMessageItemContentTypeEnum {

    TEXT(), // 文字，备注：chatGpt，不要设置该字段

    IMAGE_URL(), // 图片链接

    ;

}
