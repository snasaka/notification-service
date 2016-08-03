package com.noteif.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "xmpp", locations = "classpath:application.yml")
public class XmppConfig {
    private String host;
    private int port;
    private String httpBind;
    private String prebindUrl;
    private String authKey;
    private int apiPort;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHttpBind() {
        return httpBind;
    }

    public void setHttpBind(String httpBind) {
        this.httpBind = httpBind;
    }

    public String getPrebindUrl() {
        return prebindUrl;
    }

    public void setPrebindUrl(String prebindUrl) {
        this.prebindUrl = prebindUrl;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public int getApiPort() {
        return apiPort;
    }

    public void setApiPort(int apiPort) {
        this.apiPort = apiPort;
    }

}