package com.wraitnell.scheduler.player;

import com.wraitnell.scheduler.config.Config;
import com.wraitnell.scheduler.exception.SchedulerExceptionNotFound;
import com.wraitnell.scheduler.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private Config config;

    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(String id) {
        return playerRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No player with ID: "+id));
    }

    public Player updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(String id) {

    playerRepository.delete(playerRepository.findById(id).orElseThrow(
        () -> new SchedulerExceptionNotFound("No player with ID: "+id)));
    }

    public Integer getDowntimeForPlayerById(String id, Long currentTime) {
        //TODO: method not implemented

        // This method needs to be called after the session is started to get downtime for the current session

        // This isn't going to work, downtime is by character so I am going to have to implement characters here too
        // This could be kind of interesting, though, if I'm able to pull character stats out of Foundry. Waaay down
        // the road

        // OK in theory, if we call this

        return 1;
    }

    public Integer getTokens(String id) {

        // Return player's remaining tokens - Max Tokens minus the size of the tokened sessions set
        // Leaving tokens as abstract, if I stored the number of tokens in the db it would just get messed up

        return config.getMaxTokens() - playerRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No player with ID: " + id)).getTokenSessions().size();
    }

   public Set<Session> getTokenSessionsForPlayerById(String id) {
        // TODO: use methods in the session service for this to allow filtering out archived sessions

        return playerRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No player with ID: " + id)).getTokenSessions();
   }

    public Set<Session> getQueueSessionsForPlayerById(String id) {
        // TODO: use methods in the session service for this to allow filtering out archived sessions

        return playerRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No player with ID: "+id)).getQueueSessions();
    }
}