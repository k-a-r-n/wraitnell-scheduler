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
    public void addPlayer(@RequestBody Player player) {
        playerService.addPlayer(player);
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
    // TODO: This method is dumb maybe, the ID doesn't go anywhere - revisit
    public void updatePlayer (@RequestBody Player player) {
        playerService.updatePlayer(player);
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

}
