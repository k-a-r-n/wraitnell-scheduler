package com.wraitnell.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("config")
public class Config {
    private Integer maxSessionPlayers;
    private Integer maxTokens;
    private String url;

    public Config() {
    }

    public Config(Integer maxSessionPlayers, Integer maxTokens, String url) {
        this.maxSessionPlayers = maxSessionPlayers;
        this.maxTokens = maxTokens;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
