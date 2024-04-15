package com.cmcorg20240415.livestream.ai.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 助手类型，模型分类，枚举类
 */
@Getter
@AllArgsConstructor
public enum AIModelCategoryEnum {

    CHAT(101), // 聊天

    DRAW(201), // 绘画

    IMAGE(301), // 图片处理

    ;

    @JsonValue
    private final int code; // 类型编码

}
