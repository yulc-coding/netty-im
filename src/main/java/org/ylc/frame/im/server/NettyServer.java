package org.ylc.frame.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.ylc.frame.im.server.handler.NettyServerHandlerInitializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 代码千万行，注释第一行
 * 注释不规范，同事泪两行
 * <p>
 * 服务类
 *
 * @author YuLc
 * @version 1.0.0
 * @date 2021-07-07
 */
@Slf4j
@Component
public class NettyServer {

    @Value("${netty.port}")
    private Integer port;

    private final NettyServerHandlerInitializer nettyServerHandlerInitializer;

    /**
     * boss 线程组，用于处理客户端连接
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();

    /**
     * worker 线程组，处理客户端数据读写
     */
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    public NettyServer(NettyServerHandlerInitializer nettyServerHandlerInitializer) {
        this.nettyServerHandlerInitializer = nettyServerHandlerInitializer;
    }

    /**
     * 启动 Netty Server
     */
    @PostConstruct
    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 指定服务端口
                    .localAddress(port)
                    // accept队列大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 保持长连接，开启心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 允许较小的数据包的发送，降低延迟
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(nettyServerHandlerInitializer);

            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                channel = future.channel();
                log.info("Netty Server 启动成功，port：{}", port);
            }
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动IM Server失败：{}", e.getMessage());
        } finally {
            // 退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            if (channel != null) {
                channel.close();
            }
        }
    }

    /**
     * 关闭服务，释放资源
     */
    @PreDestroy
    public void shutDown() {
        if (channel != null) {
            channel.close();
        }
        // 优雅的退出，释放线程池资源
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


}
