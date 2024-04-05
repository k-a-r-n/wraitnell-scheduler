package com.wraitnell.scheduler.webhook;

public class WebhookMessage {
    private String content;    // the message contents (up to 2000 characters)	              REQUIRED: one of content, file, embeds
    private String username;   //	override the default username of the webhook              REQUIRED: false
    private String avatar_url; //	string	override the default avatar of the webhook	      REQUIRED: false
    private Boolean tts;       // true if this is a TTS message                               REQUIRED: false

    public WebhookMessage() {
    }

    public WebhookMessage(String content) {
        this.content = content;
    }

    public WebhookMessage(String content, String username, String avatar_url, Boolean tts) {
        this.content = content;
        this.username = username;
        this.avatar_url = avatar_url;
        this.tts = tts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public Boolean getTts() {
        return tts;
    }

    public void setTts(Boolean tts) {
        this.tts = tts;
    }
}
