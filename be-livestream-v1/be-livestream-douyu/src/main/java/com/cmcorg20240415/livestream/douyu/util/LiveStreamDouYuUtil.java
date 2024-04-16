package com.cmcorg20240415.livestream.douyu.util;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PreDestroy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cmcorg20240415.livestream.ai.model.dto.AIMessageItemDTO;
import com.cmcorg20240415.livestream.ai.model.enums.AIMessageItemRoleEnum;
import com.cmcorg20240415.livestream.ai.util.LiveStreamAiUtil;
import com.cmcorg20240415.livestream.douyu.properties.LiveStreamDouYuProperties;
import com.cmcorg20240415.livestream.util.configuration.BaseConfiguration;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LiveStreamDouYuUtil {

    public static LiveStreamDouYuProperties liveStreamDouYuProperties;

    public static TaskExecutor taskExecutor;

    public static TaskScheduler taskScheduler;

    public LiveStreamDouYuUtil(LiveStreamDouYuSeleniumUtil liveStreamDouYuSeleniumUtil,
        LiveStreamDouYuProperties liveStreamDouYuProperties, BaseConfiguration baseConfiguration,
        TaskExecutor taskExecutor, TaskScheduler taskScheduler) {

        LiveStreamDouYuUtil.liveStreamDouYuProperties = liveStreamDouYuProperties;

        LiveStreamDouYuUtil.taskExecutor = taskExecutor;

        LiveStreamDouYuUtil.taskScheduler = taskScheduler;

        // 连接：webSocket
        connectWs();

        // 初始：WebDriver
        LiveStreamDouYuSeleniumUtil.initWebDriver(true);

        // 打开浏览器
        openSelenium();

    }

    /**
     * 打开浏览器
     */
    private static void openSelenium() {

        LiveStreamDouYuSeleniumUtil.getCrawlerResult(liveStreamDouYuProperties.getRoomUrl(), null, null);

    }

    private static LiveStreamDouYuWebSocketClient webSocketClient;

    /**
     * 定时任务，心跳
     */
    @PreDestroy
    @Scheduled(fixedDelay = 40000)
    public void scheduledHeartBeat() {

        if (webSocketClient.isOpen()) {

            webSocketClient.send(heartBeat());

        }

    }

    public static final List<String> DAN_MU_LIST = new CopyOnWriteArrayList<>();

    /**
     * 定时任务，发送弹幕
     */
    @PreDestroy
    @Scheduled(fixedDelay = 10000)
    public void scheduledSendDanMu() {

        // 检查
        check();

        if (CollUtil.isEmpty(DAN_MU_LIST)) {
            return;
        }

        String danMu = DAN_MU_LIST.get(DAN_MU_LIST.size() - 1);

        DAN_MU_LIST.clear();

        if (!BooleanUtil.isTrue(liveStreamDouYuProperties.getStopFlag())) {

            try {

                // 执行：发送弹幕
                doSendDanMu(danMu);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    // 是否登录成功
    public static boolean SIGN_IN_FLAG = false;

    // 是否存在登录框
    public static boolean SIGN_IN_FRAME_FLAG = false;

    /**
     * 检查
     */
    private void check() {

        if (SIGN_IN_FLAG) {
            return;
        }

        WebElement webElement =
            LiveStreamDouYuSeleniumUtil.getWebElement(null, By.xpath("//*[@id=\"login-passport-frame\"]"), null);

        if (webElement != null) {

            log.info("未登录成功，存在登录框");

            SIGN_IN_FRAME_FLAG = true;

            return;

        }

        if (SIGN_IN_FRAME_FLAG) {

            log.info("登录成功");

            SIGN_IN_FLAG = true;

        }

    }

    /**
     * 执行：发送弹幕
     */
    public static void doSendDanMu(String danMu) {

        if (!LiveStreamDouYuUtil.SIGN_IN_FLAG) {
            return;
        }

        List<AIMessageItemDTO> messageList = new ArrayList<>();

        String preset = liveStreamDouYuProperties.getPreset();

        if (StrUtil.isNotBlank(preset)) {

            messageList.add(AIMessageItemDTO.text(AIMessageItemRoleEnum.SYSTEM, preset));

        }

        messageList.add(AIMessageItemDTO.text(AIMessageItemRoleEnum.USER, danMu));

        // 获取：助手回复
        String res = LiveStreamAiUtil.chat(messageList);

        // 执行：发送弹幕
        LiveStreamDouYuSeleniumUtil.sendDanMu(res);

    }

    /**
     * 连接：webSocket
     */
    @SneakyThrows
    private static void connectWs() {

        URI uri = new URI(liveStreamDouYuProperties.getWsUrl());

        webSocketClient = new LiveStreamDouYuWebSocketClient(uri, new MyDraft());

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
