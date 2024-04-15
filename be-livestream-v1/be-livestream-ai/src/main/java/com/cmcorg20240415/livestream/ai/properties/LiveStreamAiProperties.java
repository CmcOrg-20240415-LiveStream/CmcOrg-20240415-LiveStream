package com.cmcorg20240415.livestream.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.cmcorg20240415.livestream.model.constant.LiveStreamPropertiesPrefixConstant;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = LiveStreamPropertiesPrefixConstant.AI)
@RefreshScope
public class LiveStreamAiProperties {

    /**
     * 开发时的 apiKey
     */
    private String chatApiKeyDev;

    /**
     * 生产时的 apiKey
     */
    private String chatApiKeyProd;

}
