package org.ylc.frame.im.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 代码千万行，注释第一行
 * 注释不规范，同事泪两行
 * <p>
 * 管理客户端连接后的 Channel和用户信息
 *
 * @author YuLc
 * @version 1.0.0
 * @date 2021-07-07
 */
@Slf4j
@Component
public class NettyChannelManager {

    /**
     * {@link Channel#attr(AttributeKey)} 属性中，表示 Channel 对应的用户
     */
    private static final AttributeKey<Long> CHANNEL_ATTR_KEY_USER = AttributeKey.newInstance("user");


    /**
     * Channel 映射
     */
    private ConcurrentMap<ChannelId, Channel> channels = new ConcurrentHashMap<>();

    /**
     * 用户ID与 Channel 的映射。
     * <p>
     * 通过它，可以获取用户对应的 Channel。这样，我们可以向指定用户发送消息。
     */
    private ConcurrentMap<Long, Channel> userChannels = new ConcurrentHashMap<>();

    /**
     * 添加 Channel 到 {@link #channels} 中
     *
     * @param channel Channel
     */
    public void add(Channel channel) {
        channels.put(channel.id(), channel);
        log.info("一个连接({})加入", channel.id());
    }

    /**
     * 添加指定用户到 {@link #userChannels} 中
     *
     * @param channel Channel
     * @param userId  用户ID
     */
    public void addUser(Channel channel, Long userId) {
        Channel existChannel = channels.get(channel.id());
        if (existChannel == null) {
            log.error("[addUser][连接({}) 不存在]", channel.id());
            return;
        }
        // 设置属性
        channel.attr(CHANNEL_ATTR_KEY_USER).set(userId);
        // 添加到 userChannels
        userChannels.put(userId, channel);
    }

    /**
     * 将 Channel 从 {@link #channels} 和 {@link #userChannels} 中移除
     *
     * @param channel Channel
     */
    public void remove(Channel channel) {
        // 移除 channels
        channels.remove(channel.id());
        // 移除 userChannels
        if (channel.hasAttr(CHANNEL_ATTR_KEY_USER)) {
            userChannels.remove(channel.attr(CHANNEL_ATTR_KEY_USER).get());
        }
        log.info("[remove][一个连接({})离开]", channel.id());
    }

    /**
     * 发送给指定人员
     *
     * @param userId 用户ID
     * @param msg    消息
     */
    public void send(Long userId, String msg) {
        Channel channel = userChannels.get(userId);
        if (channel == null) {
            log.error("对方不在线");
            return;
        }
        if (!channel.isActive()) {
            log.error("对方[{}]的连接[{}]未激活", userId, channel.id());
            return;
        }
        channel.writeAndFlush(msg);
    }

    /**
     * 群组发送
     *
     * @param groupId 群组ID
     * @param msg     消息
     */
    public void sendGroup(Long groupId, String msg) {
        // todo 根据 组ID ——> 所有的用户ID ——> 对应的 Channel

    }

    /**
     * 发送所有人
     *
     * @param msg 消息
     */
    public void sendAll(String msg) {
        for (Channel channel : channels.values()) {
            if (!channel.isActive()) {
                log.error("[send][连接({})未激活]", channel.id());
                return;
            }
            // 发送消息
            channel.writeAndFlush(msg);
        }
    }

}
