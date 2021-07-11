package org.ylc.frame.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private NettyServerHandlerInitializer nettyServerHandlerInitializer;

    /**
     * boss 线程组，用于处理客户端连接
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();

    /**
     * worker 线程组，处理客户端数据读写
     */
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;


    /**
     * 启动 Netty Server
     */
    @PostConstruct
    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 指定服务端口
                    .localAddress(port)
                    // accept队列大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(nettyServerHandlerInitializer);

            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                channel = future.channel();
                log.info("Netty Server 启动成功，port：{}", port);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅的退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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
