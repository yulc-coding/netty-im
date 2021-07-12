package org.ylc.frame.im.server.dispatcher;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 代码千万行，注释第一行
 * 注释不规范，同事泪两行
 * <p>
 * 消息分发器
 *
 * @author YuLc
 * @version 1.0.0
 * @date 2021-07-11
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("new msg:{}", msg.text());

        ctx.writeAndFlush(new TextWebSocketFrame(LocalDateTime.now() + " 收到msg:" + msg.text()));
    }
}
