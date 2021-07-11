package org.ylc.frame.im.server.dispatcher;

import io.netty.channel.Channel;

/**
 * 代码千万行，注释第一行
 * 注释不规范，同事泪两行
 * <p>
 * 消息处理器
 *
 * @author YuLc
 * @version 1.0.0
 * @date 2021-07-11
 */
public interface MessageHandler<T extends Message> {

    /**
     * 处理消息
     *
     * @param channel NettyChannel
     * @param message 消息
     */
    void execute(Channel channel, T message);


    /**
     * 获取消息类型
     *
     * @return type
     */
    String getMessageType();
}
