package org.ylc.frame.im.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.stereotype.Component;

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
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {


    private static final Long READ_TIMEOUT_SECOND = 60 * 3L;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 获取 pipeline
        ChannelPipeline channelPipeline = ch.pipeline();
        // 添加相关的 Handler，心跳检查、编码器、解码器、消息分发器、服务端处理器 TODO
        channelPipeline.addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECOND, TimeUnit.SECONDS));

    }
}
