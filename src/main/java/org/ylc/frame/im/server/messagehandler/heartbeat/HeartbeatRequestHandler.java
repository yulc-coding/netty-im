package org.ylc.frame.im.server.messagehandler.heartbeat;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylc.frame.im.server.message.heartbeat.HeartbeatResponse;
import org.ylc.frame.im.server.messagehandler.MessageHandler;
import org.ylc.frame.im.server.message.heartbeat.HeartbeatRequest;

/**
 * 代码千万行，注释第一行
 * 注释不规范，同事泪两行
 * <p>
 * 心跳
 *
 * @author YuLc
 * @date 2021-07-13
 * @since 1.0.0
 */
@Slf4j
@Component
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {

    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        log.info("收到{}的心跳请求", channel.id());
        channel.writeAndFlush(HeartbeatResponse.TYPE);
    }

    @Override
    public String getMessageType() {
        return HeartbeatRequest.TYPE;
    }
}
