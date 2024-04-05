package com.wraitnell.scheduler.session;

import com.wraitnell.scheduler.config.Config;
import com.wraitnell.scheduler.exception.SchedulerExceptionCantToken;
import com.wraitnell.scheduler.exception.SchedulerExceptionNotFound;
import com.wraitnell.scheduler.player.Player;
import com.wraitnell.scheduler.player.PlayerService;
import com.wraitnell.scheduler.webhook.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SessionService {

    // TODO: Many methods will need to tell the discord bot what to do

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private Config config;

    @Autowired
    private WebhookService webhookService;

    public void addSession(Session session) {
        webhookService.testWebhook("test");
        sessionRepository.save(session);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        System.out.println("I have ID: "+id);
        return sessionRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No session with ID: " + id));
    }

    public void updateSession(Session session) {
        sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.delete(sessionRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No session with ID: " + id)));
    }

    public void tokenPlayerForSession (String playerId, Long sessionId) {

        Session session = getSessionById(sessionId);                  // Session we're adding to
        Player newPlayer = playerService.getPlayerById(playerId);     // Player we're adding

        if (session.getTokenPlayers().size() < config.getMaxSessionPlayers()-1) { //  -1 for captain

          if(playerService.getTokens(playerId) > 0) {

              session.tokenPlayerForSession(newPlayer);
              sessionRepository.save(session);

          } else { // Player has no tokens
              throw new SchedulerExceptionCantToken("player with ID: " + playerId + " has no tokens.");
          }
        } else { // Session is full
            throw new SchedulerExceptionCantToken("session with ID: " + sessionId + " is full.");
        }
    }

    public boolean queuePlayerForSession (String playerID, Long sessionId) {

        Session session = getSessionById(sessionId);                  // Session we're adding to
        Player newPlayer = playerService.getPlayerById(playerID);     // Player we're adding
        session.queuePlayerForSession(newPlayer);                     // We don't care how many players queue
        sessionRepository.save(session);

        //TODO: how does .save do error handling
        // also do discord stuff

        return true;
    }

    public Set<Player> getTokenPlayersForSessionId(Long id) {
        return getSessionById(id).getTokenPlayers();
    }

    public void startSession(Long id) {
        // TODO:
        //  set the campaignStartDate for the session
        //  Set start date in discord?
        //  Ping?

        // set last campaign id for all players on session
        Set<Player> players = getSessionById(id).getTokenPlayers();
        for( Player player : players) {
            player.setLastSessionId(id);
            playerService.updatePlayer(player);
        }
    }

    public void stopSession(Long id) {
        // TODO:
        //  set the campaign_end_date for the session
        //  Tell the bot to add a thread for the expedition log
    }

}

