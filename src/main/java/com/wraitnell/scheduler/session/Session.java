package com.wraitnell.scheduler.session;

import com.wraitnell.scheduler.player.Player;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name="sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_timestamp")
    private Timestamp createTimestamp;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "change_timestamp")
    private Timestamp changeTimestamp;
    @Column(name = "campaign_start_date")   // Start timestamp in seconds from SimpleCalendar
    private long campaignStartDate;
    @Column(name = "campaign_end_date")     // End timestamp in seconds from SimpleCalendar
    private long campaignEndDate;
    @Column(name = "scheduled_timestamp")   // The real world start timestamp for the session
    private Long scheduledTimestamp = System.currentTimeMillis();
    @Column(name = "level")
    private int sessionLevel;
    @Column(name = "goal")
    private String sessionGoal;
    @Column(name = "discord_message_id")
    private String discordMessageId; // this is the ID of the discord message so we know what we need to edit if it changes
    @JoinColumn(name = "captain_id")
    @ManyToOne
    private Player sessionCaptain;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "player_token_session", joinColumns = {@JoinColumn(referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(referencedColumnName = "id")})
    private Set<Player> tokenPlayers;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "player_queue_session", joinColumns = {@JoinColumn(referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(referencedColumnName = "id")})
    private Set<Player> queuePlayers;

    public void queuePlayerForSession (Player player) { // Adds a player object to the players set
        this.queuePlayers.add(player);
    }
    public void unQueuePlayerForSession (Player player) { // Adds a player object to the players set
        this.queuePlayers.remove(player);
    }
    public void tokenPlayerForSession (Player player) { // Adds a player object to the players set

        System.out.println("I'm adding " + player.getId() + " to session " + this.getId());
        this.tokenPlayers.add(player);
    }
    public void unTokenPlayerForSession(Player player) { this.tokenPlayers.remove(player); }


    public Session() {
    }

    public Session(Long id,
                   Timestamp createTimestamp,
                   Timestamp changeTimestamp,
                   long campaignStartDate,
                   long campaignEndDate,
                   Long scheduledTimestamp,
                   int sessionLevel,
                   String sessionGoal,

                   Player sessionCaptain,
                   Set<Player> tokenPlayers,
                   Set<Player> queuePlayers) {

        this.id = id;
        this.createTimestamp = createTimestamp;
        this.changeTimestamp = changeTimestamp;
        this.campaignStartDate = campaignStartDate;
        this.campaignEndDate = campaignEndDate;
        this.scheduledTimestamp = scheduledTimestamp;
        this.sessionLevel = sessionLevel;
        this.sessionGoal = sessionGoal;
        this.sessionCaptain = sessionCaptain;
        this.tokenPlayers = tokenPlayers;
        this.queuePlayers = queuePlayers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Timestamp getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(Timestamp changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public long getCampaignStartDate() {
        return campaignStartDate;
    }

    public void setCampaignStartDate(long campaignStartDate) {
        this.campaignStartDate = campaignStartDate;
    }

    public long getCampaignEndDate() {
        return campaignEndDate;
    }

    public void setCampaignEndDate(long campaignEndDate) {
        this.campaignEndDate = campaignEndDate;
    }

    public Long getScheduledTimestamp() {
        return scheduledTimestamp;
    }

    public void setScheduledTimestamp(Long scheduledTimestamp) {
        this.scheduledTimestamp = scheduledTimestamp;
    }

    public int getSessionLevel() {
        return sessionLevel;
    }

    public void setSessionLevel(int sessionLevel) {
        this.sessionLevel = sessionLevel;
    }

    public String getSessionGoal() {
        return sessionGoal;
    }

    public void setSessionGoal(String sessionGoal) {
        this.sessionGoal = sessionGoal;
    }

    public Player getSessionCaptain() {
        return sessionCaptain;
    }

    public void setSessionCaptain(Player sessionCaptain) {
        this.sessionCaptain = sessionCaptain;
    }

    public Set<Player> getTokenPlayers() {
        return tokenPlayers;
    }

    public void setTokenPlayers(Set<Player> tokenPlayers) {
        this.tokenPlayers = tokenPlayers;
    }

    public Set<Player> getQueuePlayers() {
        return queuePlayers;
    }

    public void setQueuePlayers(Set<Player> queuePlayers) {
        this.queuePlayers = queuePlayers;
    }

    public String getDiscordMessageId() {
        return discordMessageId;
    }

    public void setDiscordMessageId(String discordMessageId) {
        this.discordMessageId = discordMessageId;
    }
}