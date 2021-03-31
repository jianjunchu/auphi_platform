package org.firzjb.admin.authorization.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * JWT的token，区分大小写
 * @author Hao
 * @create 2017-04-08
 */

@Repository
public class JwtConfig {

    /**
     * #base64加密字符串
     */
    @Value("#{propertiesReader['jwt.base64.secret']}")
    private String base64Secret;

    /**
     * #base64加密字符串
     */
    @Value("#{propertiesReader['jwt.refresh.base64.secret']}")
    private String refreshBase64Secret;

    /**
     * #jwt token过期时间，毫秒
     */
    @Value("#{propertiesReader['jwt.expires.second']}")
    private long expiresSecond;

    /**
     * #refresh token过期时间
     */
    @Value("#{propertiesReader['jwt.refresh.expires.second']}")
    private long refreshExpiresSecond;

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    public long getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(long expiresSecond) {
        this.expiresSecond = expiresSecond;
    }

    public long getRefreshExpiresSecond() {
        return refreshExpiresSecond;
    }

    public void setRefreshExpiresSecond(long refreshExpiresSecond) {
        this.refreshExpiresSecond = refreshExpiresSecond;
    }

    public String getRefreshBase64Secret() {
        return refreshBase64Secret;
    }

    public void setRefreshBase64Secret(String refreshBase64Secret) {
        this.refreshBase64Secret = refreshBase64Secret;
    }
}