package org.ylc.frame.im.server.message.auth;

import org.ylc.frame.im.server.message.Message;

/**
 * 代码千万行，注释第一行
 * 注释不规范，同事泪两行
 * <p>
 * 认证请求
 *
 * @author YuLc
 * @version 1.0.0
 * @date 2021-07-11
 */
public class AuthRequest implements Message {

    public static final String TYPE = "AUTH_REQUEST";

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
