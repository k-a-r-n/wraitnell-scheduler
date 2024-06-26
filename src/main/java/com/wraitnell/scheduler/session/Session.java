/*
 * Copyright (c)  2024 Josh Hayford (jshayford@gmail.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.wraitnell.scheduler.session;

import com.wraitnell.scheduler.player.Player;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
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
    private long campaignStartDate = 0;
    @Column(name = "campaign_end_date")     // End timestamp in seconds from SimpleCalendar
    private long campaignEndDate = 0;
    @Column(name = "scheduled_timestamp")   // The real world start timestamp for the session
    private Long scheduledTimestamp = System.currentTimeMillis();
    @Column(name = "level")
    private int sessionLevel = 0;
    @Column(name = "goal")
    private String sessionGoal = "";
    @Column(name = "discord_message_id")
    private String discordMessageId = ""; // this is the ID of the discord message so we know what we need to edit if it changes
    @JoinColumn(name = "captain_id")
    @ManyToOne
    private Player sessionCaptain;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "player_token_session", joinColumns = {@JoinColumn(referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(referencedColumnName = "id")})
    private Set<Player> tokenPlayers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "player_queue_session", joinColumns = {@JoinColumn(referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(referencedColumnName = "id")})
    private Set<Player> queuePlayers;

    @Column(name="archived")
    private boolean archived = false; // This marks the session as archived, which means it will not be returned by default when fetching sessions

    public void queuePlayerForSession (Player player) { // Adds a player object to the players set
        this.queuePlayers.add(player);
    }
    public void unQueuePlayerForSession (Player player) { // Adds a player object to the players set
        this.queuePlayers.remove(player);
    }
    public void tokenPlayerForSession (Player player) { // Adds a player object to the players set

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

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}