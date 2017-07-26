package net.riking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by bing.xun on 2017/5/23.
 */
@Component("ssoConfig")
@ConfigurationProperties(prefix = "sso.api")
public class SsoConfig {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
