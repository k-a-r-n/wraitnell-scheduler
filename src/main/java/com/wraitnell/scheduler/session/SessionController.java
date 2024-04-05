package com.wraitnell.scheduler.session;

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
    public Session addSession (@RequestBody Session session) {
        return sessionService.addSession(session);
    }

    @GetMapping("/sessions")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/session/{id}")
    public Session getSessionById (@PathVariable Long id) {
        return sessionService.getSessionById(id);
    }

    @PutMapping("/session/{id}")
    public Session updateSession (@RequestBody Session session) {
        // TODO: I think this method is dumb? The ID is not going anywhere. Revisit
        return sessionService.updateSession(session);
    }

    @DeleteMapping("/session/{id}")
    public void deleteSession (@PathVariable Long id) {
        sessionService.deleteSession(id);
    }

    @GetMapping("/session/{id}/token") // Will return a list of all tokened players for the session
    public Set<Player> getPlayersForSession (@PathVariable Long id) {
        return sessionService.getTokenPlayersForSessionId(id);
    }

    @PostMapping("/session/{sessionId}/token/{playerId}")
    public void addPlayerToSession (@PathVariable Long sessionId, @PathVariable String playerId) {
        sessionService.tokenPlayerForSession(playerId,sessionId);
    }

    // These verbs anger the REST gods, but I need a way to start and stop the session from the VTT
    @PutMapping("/session/{id}/start")
    public void startSession (@PathVariable Long id) {
            sessionService.startSession(id);
    }

    @PutMapping("/session/{id}/stop")
    public void stopSession (@PathVariable Long id) {
        sessionService.stopSession(id);
    }
}