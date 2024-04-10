package com.wraitnell.scheduler.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wraitnell.scheduler.config.Config;
import com.wraitnell.scheduler.session.Session;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="players")
public class Player {
    @Id
    private Long Id = 0L;            // Discord ID

    // This is the date when the player last got into a queued session; this is needed to determine the sort order for the queue
    @Column(name="last_queue_date")
    private Timestamp dateLastQueueSession = new Timestamp(System.currentTimeMillis());

    @Column(name = "name")
    private String name;

    @Column(name = "last_session_id")
    private Long lastSessionId;      // stores the last session that the player was in

    @JsonIgnore
    @ManyToMany(mappedBy = "tokenPlayers", cascade = CascadeType.ALL)

    private Set<Session> tokenSessions;

    @JsonIgnore
    @ManyToMany(mappedBy = "queuePlayers", cascade = CascadeType.ALL)
    private Set<Session> queueSessions;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_timestamp")
    private Timestamp createTimestamp;    // real-world date that the user joined the campaign (i.e. row created)

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "change_timestamp")
    private Timestamp changeTimestamp;    // real-world date that the user joined the campaign

    public Player() {
    }

    public Player(Long id,
                  Long lastSessionId,
                  Set<Session> tokenSessions,
                  Set<Session> queueSessions,
                  Timestamp createTimestamp,
                  Timestamp changeTimestamp) {
        this.Id = id;
        this.lastSessionId = lastSessionId;
        this.tokenSessions = tokenSessions;
        this.queueSessions = queueSessions;
        this.createTimestamp = createTimestamp;
        this.changeTimestamp = changeTimestamp;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Timestamp getDateLastQueueSession() {
        return dateLastQueueSession;
    }

    public void setDateLastQueueSession(Timestamp dateLastQueueSession) {
        this.dateLastQueueSession = dateLastQueueSession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLastSessionId() {
        return lastSessionId;
    }

    public void setLastSessionId(Long lastSessionId) {
        this.lastSessionId = lastSessionId;
    }

    public Set<Session> getTokenSessions() {
        // This method returns all sessions the player is tokened for, including archived ones.
        // Use sessionService.getTokenSessionsByPlayerId() instead, by default it returns only un-archived

        return tokenSessions;
    }

    public void setTokenSessions(Set<Session> tokenSessions) {
        this.tokenSessions = tokenSessions;
    }

    public Set<Session> getQueueSessions() {
        // This method returns all sessions the player is queued for, including archived ones.
        // Use sessionService.getQueueSessionsByPlayerId() instead, by default it returns only un-archived

        return queueSessions;
    }

    public void setQueueSessions(Set<Session> queueSessions) {
        this.queueSessions = queueSessions;
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
}