package com.cmcorg20240415.livestream.ai.model.enums;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.hutool.core.map.MapUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 助手类型，枚举类
 */
@Getter
@AllArgsConstructor
public enum AIModelTypeEnum {

    CHAT_CHAT_GPT(101, "gpt-3.5-turbo", AIModelCategoryEnum.CHAT, 4096), // 聊天：chatGpt：gpt-3.5-turbo

    CHAT_CHAT_GPT_GPT_4(111, "gpt-4", AIModelCategoryEnum.CHAT, 8192), // 聊天：chatGpt：gpt-4

    CHAT_CHAT_GPT_GPT_4_32_K(112, "gpt-4-32k", AIModelCategoryEnum.CHAT, 32768), // 聊天：chatGpt：gpt-4-32k

    CHAT_CHAT_GPT_GPT_4_VISION_PREVIEW(113, "gpt-4-vision-preview", AIModelCategoryEnum.CHAT, 128000), // 聊天：chatGpt：gpt-4-vision-preview

    GPT_4_TURBO_PREVIEW(114, "gpt-4-turbo-preview", AIModelCategoryEnum.CHAT, 128000), // 聊天：chatGpt：gpt-4-turbo-preview

    DRAW_MIDJOURNEY(10101, "midjourney", AIModelCategoryEnum.DRAW, 4096), // 绘画：midjourney

    DRAW_DALL_E_3(10201, "dall-e-3", AIModelCategoryEnum.DRAW, 4000), // 绘画：dall-e-3

    ;

    @JsonValue
    private final int code; // 类型编码，备注，一万的范围表示一个类型，比如：聊天从 101开始，绘画从 10101开始，图片处理从 20101开始，注意：如这里不是一万的范围了，则前端也要跟着修改

    private final String modelName; // 模型名称

    private final AIModelCategoryEnum category; // 模型分类

    private final int maxToken; // 最大 token数量

    public int getMaxToken() {
        return maxToken - 500;
    }

    public int getOriginMaxToken() {
        return maxToken;
    }

    public static final AIModelTypeEnum DEFAULT_CHAT = AIModelTypeEnum.CHAT_CHAT_GPT;

    public static final AIModelTypeEnum DEFAULT_DRAW = AIModelTypeEnum.DRAW_DALL_E_3;

    public static final Map<Integer, Integer> MAX_TOKEN_MAP = MapUtil.newHashMap();

    static {

        for (AIModelTypeEnum item : values()) {

            MAX_TOKEN_MAP.put(item.getCode(), item.getOriginMaxToken());

        }

    }

}
