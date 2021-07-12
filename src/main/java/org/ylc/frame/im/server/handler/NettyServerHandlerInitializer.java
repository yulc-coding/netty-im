package org.ylc.frame.im.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylc.frame.im.server.dispatcher.MessageDispatcher;

import java.util.concurrent.TimeUnit;

/**
 * 代码千万行，注释第一行
 * 注释不规范，同事泪两行
 * <p>
 * 每一个客户端与服务端建立完成连接时，服务端会创建一个 Channel 与之对应
 * #initChannel(channel ch) 进行自定义初始化
 *
 * @author YuLc
 * @version 1.0.0
 * @date 2021-07-07
 */
@Slf4j
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 心跳超时时间（秒）
     */
    private static final Long READ_TIMEOUT_SECONDS = 60 * 3L;

    private final MessageDispatcher messageDispatcher;

    private final NettyServerHandler nettyServerHandler;

    public NettyServerHandlerInitializer(MessageDispatcher messageDispatcher, NettyServerHandler nettyServerHandler) {
        this.messageDispatcher = messageDispatcher;
        this.nettyServerHandler = nettyServerHandler;
    }


    @Override
    protected void initChannel(Channel ch) {
        log.info("new channel :{}", ch.id());
        ChannelPipeline channelPipeline = ch.pipeline();
        // 添加 ChannelHandler 到 ChannelPipeline中
        channelPipeline
                // 空闲检测
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                //因为基于http协议，使用http的编码和解码器
                //.addLast(new HttpServerCodec())
                //是以块方式写，添加ChunkedWriteHandler处理器
                //.addLast(new ChunkedWriteHandler())
                //.addLast(new HttpObjectAggregator(8192))
                //.addLast(new WebSocketServerProtocolHandler("/im"))
                .addLast(messageDispatcher)
                .addLast(nettyServerHandler);
    }
}
