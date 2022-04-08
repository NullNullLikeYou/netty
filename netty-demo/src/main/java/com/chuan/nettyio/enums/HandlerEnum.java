package com.chuan.nettyio.enums;

import com.chuan.nettyio.handler.QuitHandler;
import com.chuan.nettyio.handler.req.ChatRequestMessageHandler;
import com.chuan.nettyio.handler.req.GroupChatRequestMessageHandler;
import com.chuan.nettyio.handler.req.GroupCreateRequestMessageHandler;
import com.chuan.nettyio.handler.req.GroupJoinRequestMessageHandler;
import com.chuan.nettyio.handler.req.GroupMembersRequestMessageHandler;
import com.chuan.nettyio.handler.req.GroupQuitRequestMessageHandler;
import com.chuan.nettyio.handler.req.LoginRequestMessageHandler;
import com.chuan.nettyio.handler.req.RpcRequestMessageHandler;
import com.chuan.nettyio.protocol.MessageCodecSharable;
import io.netty.channel.ChannelHandler;
import io.netty.handler.logging.LoggingHandler;

import java.util.function.Supplier;

/**
 * @author chuan
 */

public enum HandlerEnum {
    /**
     * 日志处理器
     */
    LOGGING_HANDLER(LoggingHandler::new),
    MESSAGE_CODEC(MessageCodecSharable::new),
    LOGIN_HANDLER(LoginRequestMessageHandler::new),
    CHAT_HANDLER(ChatRequestMessageHandler::new),
    GROUP_CREATE_HANDLER(GroupCreateRequestMessageHandler::new),
    GROUP_JOIN_HANDLER(GroupJoinRequestMessageHandler::new),
    GROUP_MEMBERS_HANDLER(GroupMembersRequestMessageHandler::new),
    GROUP_QUIT_HANDLER(GroupQuitRequestMessageHandler::new),
    QUIT_HANDLER(QuitHandler::new),
    GROUP_CHAT_HANDLER(GroupChatRequestMessageHandler::new),
    RPC_HANDLER(RpcRequestMessageHandler::new);
    /**
     * 对应实例
     */
    private final Supplier<? extends ChannelHandler> supplier;

    HandlerEnum(Supplier<? extends ChannelHandler> supplier) {
        this.supplier = supplier;
    }

    public Supplier<? extends ChannelHandler> getSupplier() {
        return supplier;
    }
}
