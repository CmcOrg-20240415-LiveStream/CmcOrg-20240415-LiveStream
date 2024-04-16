package com.cmcorg20240415.livestream.ai.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import com.cmcorg20230301.be.engine.util.util.RetryUtil;
import com.cmcorg20240415.livestream.ai.model.dto.AIMessageItemDTO;
import com.cmcorg20240415.livestream.ai.model.enums.AIMessageItemContentTypeEnum;
import com.cmcorg20240415.livestream.ai.model.enums.AIModelTypeEnum;
import com.cmcorg20240415.livestream.ai.properties.LiveStreamAiProperties;
import com.cmcorg20240415.livestream.model.constant.LiveStreamLogTopicConstant;
import com.cmcorg20240415.livestream.util.configuration.BaseConfiguration;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j(topic = LiveStreamLogTopicConstant.AI)
public class LiveStreamAiUtil {

    private static LiveStreamAiProperties liveStreamAiProperties;

    @Resource
    public void setLiveStreamAiProperties(LiveStreamAiProperties liveStreamAiProperties) {
        LiveStreamAiUtil.liveStreamAiProperties = liveStreamAiProperties;
    }

    /**
     * 聊天
     */
    public static String chat(List<AIMessageItemDTO> messageListTemp) {

        String apiKey = getApiKey(); // 获取：apiKey

        if (StrUtil.isBlank(apiKey)) {
            throw new RuntimeException("apiKey为空");
        }

        JSONObject paramMap = new JSONObject();

        List<Object> messageList = getMessageList(messageListTemp);

        paramMap.set("messages", messageList);

        paramMap.set("model", AIModelTypeEnum.DEFAULT_CHAT.getModelName());

        String paramStr = paramMap.toString();

        HttpRequest httpRequest = HttpRequest.post("https://api.chatanywhere.com.cn/v1/chat/completions")
            .header("Authorization", apiKey).header("Content-Type", "application/json").body(paramStr);

        // 执行
        String resultStr = RetryUtil.execHttpRequest(httpRequest);

        // log.info("入参：{}\n返回：{}", paramStr, StrUtil.cleanBlank(resultStr));

        JSONObject result = JSONUtil.parseObj(resultStr);

        JSONArray choices = result.getJSONArray("choices");

        JSONObject messageItem = choices.get(0, JSONObject.class, false);

        JSONObject messageJson = messageItem.getJSONObject("message");

        return messageJson.getStr("content");

    }

    /**
     * 获取：消息列表
     */
    @NotNull
    private static List<Object> getMessageList(List<AIMessageItemDTO> messageListTemp) {

        List<Object> messageList = new ArrayList<>();

        for (AIMessageItemDTO item : messageListTemp) {

            // 如果是：图片，则需要额外进行处理
            if (AIMessageItemContentTypeEnum.IMAGE_URL.equals(item.getContentType())) {

                messageList.add(item.toText()); // 转换为：文字

            } else {

                messageList.add(item);

            }

        }

        return messageList;

    }

    /**
     * 获取：apiKey
     */
    private static String getApiKey() {

        if (BaseConfiguration.devFlag()) {

            return liveStreamAiProperties.getChatApiKeyDev();

        } else {

            return liveStreamAiProperties.getChatApiKeyProd();

        }

    }

}
