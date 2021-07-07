package org.ylc.frame.im.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ylc.frame.im.server.NettyChannelManager;

/**
 * 代码全万行，注释第一行
 * 注释不规范，同事泪两行
 * <p>
 * Channel连接、断开、异常处理
 *
 * @author YuLc
 * @version 1.0.0
 * @date 2021-07-07
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private NettyChannelManager nettyChannelManager;


    /**
     * 在客户端和服务端建立连接完成时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        nettyChannelManager.add(ctx.channel());
    }

    /**
     * 在客户端和服务端断开连接时
     * 移除对应的 Channel
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        nettyChannelManager.remove(ctx.channel());
    }

    /**
     * Channel 发生异常，断开和客户端的连接。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[exceptionCaught][连接({}) 发生异常]", ctx.channel().id(), cause);
        ctx.channel().close();
    }
}
