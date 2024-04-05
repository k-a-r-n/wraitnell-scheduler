package com.wraitnell.scheduler.webhook;

import java.util.ArrayList;

public class DiscordMessage {
    private String id;
    private float type;
    private String content;
    private String channel_id;
    DiscordAuthor AuthorObject;
    ArrayList<Object> attachments = new ArrayList<Object>();
    ArrayList<Object> embeds = new ArrayList<Object>();
    ArrayList<Object> mentions = new ArrayList<Object>();
    ArrayList<Object> mention_roles = new ArrayList<Object>();
    private boolean pinned;
    private boolean mention_everyone;
    private boolean tts;
    private String timestamp;
    private String edited_timestamp = null;
    private float flags;
    ArrayList<Object> components = new ArrayList<Object>();
    private String webhook_id;


    // Getter Methods

    public String getId() {
        return id;
    }

    public float getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public DiscordAuthor getAuthor() {
        return AuthorObject;
    }

    public boolean getPinned() {
        return pinned;
    }

    public boolean getMention_everyone() {
        return mention_everyone;
    }

    public boolean getTts() {
        return tts;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getEdited_timestamp() {
        return edited_timestamp;
    }

    public float getFlags() {
        return flags;
    }

    public String getWebhook_id() {
        return webhook_id;
    }

    // Setter Methods

    public void setId( String id ) {
        this.id = id;
    }

    public void setType( float type ) {
        this.type = type;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    public void setChannel_id( String channel_id ) {
        this.channel_id = channel_id;
    }

    public void setAuthor( DiscordAuthor authorObject ) {
        this.AuthorObject = authorObject;
    }

    public void setPinned( boolean pinned ) {
        this.pinned = pinned;
    }

    public void setMention_everyone( boolean mention_everyone ) {
        this.mention_everyone = mention_everyone;
    }

    public void setTts( boolean tts ) {
        this.tts = tts;
    }

    public void setTimestamp( String timestamp ) {
        this.timestamp = timestamp;
    }

    public void setEdited_timestamp( String edited_timestamp ) {
        this.edited_timestamp = edited_timestamp;
    }

    public void setFlags( float flags ) {
        this.flags = flags;
    }

    public void setWebhook_id( String webhook_id ) {
        this.webhook_id = webhook_id;
    }
}