package com.wraitnell.scheduler.webhook;

public class DiscordAuthor {
    private String id;
    private String username;
    private String avatar;
    private String discriminator;
    private float public_flags;
    private float flags;
    private boolean bot;
    private String global_name = null;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public float getPublic_flags() {
        return public_flags;
    }

    public float getFlags() {
        return flags;
    }

    public boolean getBot() {
        return bot;
    }

    public String getGlobal_name() {
        return global_name;
    }

    // Setter Methods

    public void setId( String id ) {
        this.id = id;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public void setAvatar( String avatar ) {
        this.avatar = avatar;
    }

    public void setDiscriminator( String discriminator ) {
        this.discriminator = discriminator;
    }

    public void setPublic_flags( float public_flags ) {
        this.public_flags = public_flags;
    }

    public void setFlags( float flags ) {
        this.flags = flags;
    }

    public void setBot( boolean bot ) {
        this.bot = bot;
    }

    public void setGlobal_name( String global_name ) {
        this.global_name = global_name;
    }
}
