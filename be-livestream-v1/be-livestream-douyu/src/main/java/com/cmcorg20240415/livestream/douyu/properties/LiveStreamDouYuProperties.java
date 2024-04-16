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

    /**
     * 房间链接
     */
    private String roomUrl;

    /**
     * 预设内容
     */
    private String preset;

    /**
     * 是否停止，默认：false
     */
    private Boolean stopFlag;

}
