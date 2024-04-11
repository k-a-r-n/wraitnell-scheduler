package com.wraitnell.scheduler.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface SessionRepository extends JpaRepository<Session, Long> {
    // From what I understand queries like this aren't best practice but I wasn't able to find a better way to get this done
    // TODO: research

    @Query(value="SELECT * from sessions", nativeQuery=true)
    Set<Session> findTokenSessionsForPlayerId(String playerId, boolean archived); // this should replace all FindById() calls for session

    @Query(value="SELECT * from sessions", nativeQuery=true)
    Set<Session> findQueuedSessionsForPlayerId(Long id, boolean archived);
}