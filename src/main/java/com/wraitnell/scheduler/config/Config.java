package com.wraitnell.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("config")
public class Config {
    private Integer maxSessionPlayers;
    private Integer maxTokens;
    private String sessionQueueUrl; // webhook URL for session queue channel

    public Config() {
    }

    public Config(Integer maxSessionPlayers, Integer maxTokens, String sessionQueueUrl) {
        this.maxSessionPlayers = maxSessionPlayers;
        this.maxTokens = maxTokens;
        this.sessionQueueUrl = sessionQueueUrl;
    }

    public Integer getMaxSessionPlayers() {
        return maxSessionPlayers;
    }

    public void setMaxSessionPlayers(Integer maxSessionPlayers) {
        this.maxSessionPlayers = maxSessionPlayers;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public String getSessionQueueURL() {
        return sessionQueueUrl;
    }

    public void setSessionQueueURL(String sessionQueueURL) {
        this.sessionQueueUrl = sessionQueueURL;
    }
}
