package org.ylc.frame.im.server.dispatcher;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

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
public class MessageDispatcher extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

    }
}
