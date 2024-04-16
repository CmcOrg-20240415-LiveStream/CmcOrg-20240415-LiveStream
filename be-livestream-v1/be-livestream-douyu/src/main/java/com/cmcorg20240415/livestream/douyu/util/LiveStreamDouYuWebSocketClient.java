package com.cmcorg20240415.livestream.douyu.util;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.cmcorg20240415.livestream.douyu.model.bo.MessageBO;
import com.cmcorg20240415.livestream.douyu.model.enums.MessageTypeEnum;

import cn.hutool.core.lang.func.VoidFunc1;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * webSocket客户端
 */
@Slf4j
public class LiveStreamDouYuWebSocketClient extends WebSocketClient {

    public LiveStreamDouYuWebSocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

        log.info("连接成功");

        send(LiveStreamDouYuUtil.login(LiveStreamDouYuUtil.liveStreamDouYuProperties.getRoomId()));

        send(LiveStreamDouYuUtil.joinGroup(LiveStreamDouYuUtil.liveStreamDouYuProperties.getRoomId(),
            LiveStreamDouYuUtil.liveStreamDouYuProperties.getGid()));

    }

    @Override
    public void onMessage(String s) {

        // nothing

    }

    private static final Map<MessageTypeEnum, VoidFunc1<MessageBO>> MESSAGE_HANDLER_MAP = new HashMap<>();

    static {

        MESSAGE_HANDLER_MAP.put(MessageTypeEnum.JOIN_ROOM, messageBo -> {

            log.info("新用户加入：{}", messageBo.getValue());

        });

        MESSAGE_HANDLER_MAP.put(MessageTypeEnum.MESSAGE, messageBo -> {

            String message = messageBo.getValue();

            log.info("新消息：{}", message);

            if (!BooleanUtil.isTrue(LiveStreamDouYuUtil.liveStreamDouYuProperties.getStopFlag())) {

                LiveStreamDouYuUtil.DAN_MU_LIST.add(message);

            }

        });

    }

    @SneakyThrows
    @Override
    public void onMessage(ByteBuffer byteBuffer) {

        Charset charset = StandardCharsets.UTF_8;

        CharBuffer charBuffer = charset.decode(byteBuffer);

        String str = charBuffer.toString();

        String type = ReUtil.getGroup1("type@=(.*?)/", str);

        if ("mrkl".equals(type)) {
            return;
        }

        log.info("收到消息：{}", str);

        MessageBO messageBO = null;

        if ("chatmsg".equals(type)) { // 新消息

            messageBO = new MessageBO();

            messageBO.setType(MessageTypeEnum.MESSAGE);

            String txt = ReUtil.getGroup1("txt@=(.*?)/", str);

            messageBO.setValue(txt);

        } else if ("uenter".equals(type)) { // 新用户加入

            messageBO = new MessageBO();

            messageBO.setType(MessageTypeEnum.JOIN_ROOM);

            String txt = ReUtil.getGroup1("nn@=(.*?)/", str);

            messageBO.setValue(txt);

        }

        if (messageBO == null) {
            return;
        }

        VoidFunc1<MessageBO> voidFunc1 = MESSAGE_HANDLER_MAP.get(messageBO.getType());

        if (voidFunc1 == null) {
            return;
        }

        MessageBO finalMessageBO = messageBO;

        LiveStreamDouYuUtil.taskExecutor.execute(() -> {

            // 执行
            exec(voidFunc1, finalMessageBO);

        });

    }

    /**
     * 执行
     */
    @SneakyThrows
    private static void exec(VoidFunc1<MessageBO> voidFunc1, MessageBO finalMessageBO) {

        voidFunc1.call(finalMessageBO);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

        log.info("连接关闭：{}", code);

    }

    @Override
    public void onError(Exception ex) {

        log.error("报错", ex);

    }

}
