package com.wraitnell.scheduler.player;

import com.wraitnell.scheduler.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/players")
    public Player addPlayer(@RequestBody Player player) {
        return playerService.addPlayer(player);
    }

    @GetMapping("/players")
    public List<Player> getAllPLayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/player/{id}")
    public Player getPlayerById (@PathVariable String id) {
        return playerService.getPlayerById(id);
    }

    @PutMapping("/player/{id}")
    public Player updatePlayer (@RequestBody Player player) {
        return playerService.updatePlayer(player);
    }

    @DeleteMapping("/player/{id}")
    public void deletePlayer (@PathVariable String id) {
        playerService.deletePlayer(id);
    }

    @GetMapping("/player/{id}/token")
    public Set<Session> getTokenSessionsForPlayerID (@PathVariable String id) {
        return playerService.getTokenSessionsForPlayerById(id);
   }

   @GetMapping("/player/{id}/queue")
    public Set<Session> getQueueSessionsForPlayerID (@PathVariable String id) {
        return playerService.getQueueSessionsForPlayerById(id);
    }

    // Method not implemented
    @GetMapping("/player/{id}/downtime")
    public Integer getDowntimeForPlayerID (@PathVariable String id) {
        return playerService.getDowntimeForPlayerById(id,0L);
    }
}