package com.cmcorg20240415.livestream.douyu.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.cmcorg20240415.livestream.model.constant.LiveStreamPropertiesPrefixConstant;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = LiveStreamPropertiesPrefixConstant.DOUYU)
@RefreshScope
public class LiveStreamDouYuProperties {

    /**
     * webSocket连接 url
     */
    private String wsUrl;

    /**
     * 房间 id
     */
    private String roomId;

    /**
     * gid
     */
    private String gid;

}
