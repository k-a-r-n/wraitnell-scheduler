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
    // TODO: Many methods need to tell the discord bot what to do

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private Config config;

    public void addPlayer(Player player) {
        playerRepository.save(player);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(String id) {
        return playerRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No player with ID: "+id));
    }

    public void updatePlayer(Player player) {
        playerRepository.save(player);
    }

    public void deletePlayer(String id) {

    playerRepository.delete(playerRepository.findById(id).orElseThrow(
        () -> new SchedulerExceptionNotFound("No player with ID: "+id)));

    }

    public Integer calculateDowntime(String id) {
        //TODO:
        // For a given player ID, calculate and return the downtime in days
        return 1;
    }

    public Integer getTokens(String id) {
        // Return player's remaining tokens - Max Tokens minus the size of the tokened sessions set
        return config.getMaxTokens() - playerRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No player with ID: " + id)).getTokenSessions().size();
    }

   public Set<Session> getTokenSessionsForPlayerById(String id) {
        return playerRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No player with ID: " + id)).getTokenSessions();
   }

    public Set<Session> getQueueSessionsForPlayerById(String id) {
        return playerRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No player with ID: "+id)).getQueueSessions();
    }


   // }


}
