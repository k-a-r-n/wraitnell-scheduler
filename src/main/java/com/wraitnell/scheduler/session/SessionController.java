package com.wraitnell.scheduler.session;

import com.wraitnell.scheduler.exception.SchedulerExceptionBadRequest;
import com.wraitnell.scheduler.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/sessions")
    // Add a new session
    public Session addSession (@RequestBody Session session) {
        return sessionService.addSession(session);
    }

    @GetMapping("/sessions")
    // Returns a list of all sessions
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/session/{id}")
    // Returns session having id = {id}
    public Session getSessionById (@PathVariable Long id) {
        return sessionService.getSessionById(id);
    }

    @PutMapping("/session/{id}")
    // Updates session with id {id}
    public Session updateSession (@RequestBody Session session, @PathVariable Long id) {

        if (session.getId().equals(id)) {
            return sessionService.updateSession(session);
        } else { throw new SchedulerExceptionBadRequest("session object not for this ID."); }
    }

    @DeleteMapping("/session/{id}")
    // Delete session with id {id}
    public void deleteSession (@PathVariable Long id) {
        sessionService.deleteSession(id);
    }

    @GetMapping("/session/{id}/token")
    // Return a set of all tokened players for session with id {id}
    public Set<Player> getPlayersForSession (@PathVariable Long id) {
        return sessionService.getTokenPlayersForSessionId(id);
    }

    @PostMapping("/session/{sessionId}/token/{playerId}")
    // Token a player with id {playerId} for session with id {sessionId}
    public void tokenPlayerForSession (@PathVariable Long sessionId, @PathVariable String playerId) {
        sessionService.tokenPlayerForSession(playerId,sessionId);
    }

    @DeleteMapping("/session/{sessionId}/token/{playerId}")
    // Remove player with id {playerId} from session with id {sessionId}
    public void unTokenPlayerForSession (@PathVariable Long sessionId, @PathVariable String playerId) {
        sessionService.unTokenPlayerForSession(playerId,sessionId);
    }

    @GetMapping("/session/{id}/queue")
    // Return a set of all queued players for session with id {id}
    public Set<Player> getQueuedPlayersForSession (@PathVariable Long id) {
        return sessionService.getQueuedPlayersForSessionId(id);
    }

    @PostMapping("/session/{sessionId}/queue/{playerId}")
    // Queue player with id {playerId} for session with id {sessionId}
    public void queuePlayerForSession (@PathVariable Long sessionId, @PathVariable String playerId) {
        sessionService.queuePlayerForSession(playerId,sessionId);
    }

    @DeleteMapping("/session/{sessionId}/queue/{playerId}")
    // Remove player with id {playerId} from queue for session with id {sessionId}
    public void unQueuePlayerForSession (@PathVariable Long sessionId, @PathVariable String playerId) {
        sessionService.unQueuePlayerForSession(playerId,sessionId);
    }

    @PutMapping("/session/{id}/start/{campaignTime}")
    // This method:
    //     - Sets the start date for campaign with id {id} to {campaignTime}
    //     - Sets lastSessionId for all token players to {id}

    public void startSession (@PathVariable Long id, Long campaignTime) {
            sessionService.startSession(id, campaignTime);
    }

    @PutMapping("/session/{id}/end/{campaignTime}")
    // This method:
    //     - Sets the end date for campaign with id {id} to {campaignTime}
    //     - Creates a post in the expedition log channel with the pertinent info
    //     - Deletes the discord post for the session in the session queue channel
    //     - Should maybe do something like archive the session? Haven't figured this out yet

    public void stopSession (@PathVariable Long id, Long campaignTime) {
        sessionService.endSession(id, campaignTime);
    }
}