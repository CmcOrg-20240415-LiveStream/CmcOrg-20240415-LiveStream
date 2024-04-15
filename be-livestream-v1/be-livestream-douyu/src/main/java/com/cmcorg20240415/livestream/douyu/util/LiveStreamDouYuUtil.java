package com.cmcorg20240415.livestream.douyu.util;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cmcorg20240415.livestream.douyu.properties.LiveStreamDouYuProperties;

import lombok.SneakyThrows;

@Component
public class LiveStreamDouYuUtil {

    public static LiveStreamDouYuProperties liveStreamDouYuProperties;

    @Resource
    public void setLiveStreamDouYuProperties(LiveStreamDouYuProperties liveStreamDouYuProperties) {
        LiveStreamDouYuUtil.liveStreamDouYuProperties = liveStreamDouYuProperties;
    }

    @PostConstruct
    public void postConstruct() {

        // 连接：webSocket
        connectWs();

    }

    private static LiveStreamDouYuWebSocketClient webSocketClient;

    /**
     * 定时任务，心跳
     */
    @PreDestroy
    @Scheduled(fixedDelay = 40000)
    public void scheduledSavaForAssistantLog() {

        webSocketClient.send(heartBeat());

    }

    /**
     * 连接：webSocket
     */
    @SneakyThrows
    private static void connectWs() {

        URI uri = new URI(liveStreamDouYuProperties.getWsUrl());

        webSocketClient = new LiveStreamDouYuWebSocketClient(uri);

        webSocketClient.connect();

    }

    /**
     * 登录
     */
    public static byte[] login(String roomId) {

        return douyuRequestEncode("type@=loginreq/roomid@=" + roomId);

    }

    /**
     * 加入群组请求
     */
    public static byte[] joinGroup(String roomId, String gId) {

        return douyuRequestEncode("type@=joingroup/rid@=" + roomId + "/gid@=" + gId + "/");

    }

    /**
     * 心跳
     */
    public static byte[] heartBeat() {

        return douyuRequestEncode("type@=mrkl/");

    }

    /**
     * 将传入的数据变成符合斗鱼协议要求的字节流返回
     */
    @SneakyThrows
    public static byte[] douyuRequestEncode(String message) {

        int dataLen1 = message.length() + 9; // 4 字节小端整数，表示整条消息（包括自身）长度（字节数）。

        int dataLen2 = message.length() + 9; // 消息长度出现两遍，二者相同。

        int send = 689; // 689 客户端发送给弹幕服务器的文本格式数据,暂时未用，默认为 0。保留字段：暂时未用，默认为 0。

        byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);

        byte[] endBytes = new byte[] {0}; // 结尾必须为‘\0’。详细序列化、反序列化

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(intToBytesLittle(dataLen1));

        byteArrayOutputStream.write(intToBytesLittle(dataLen2));

        byteArrayOutputStream.write(intToBytesLittle(send));

        byteArrayOutputStream.write(msgBytes);

        byteArrayOutputStream.write(endBytes);

        return byteArrayOutputStream.toByteArray();

    }

    /**
     * 将整形转化为4位小端字节流
     */
    public static byte[] intToBytesLittle(int value) {

        return new byte[] {(byte)(value & 0xFF), (byte)((value >> 8) & 0xFF), (byte)((value >> 16) & 0xFF),
            (byte)((value >> 24) & 0xFF)};

    }

}
