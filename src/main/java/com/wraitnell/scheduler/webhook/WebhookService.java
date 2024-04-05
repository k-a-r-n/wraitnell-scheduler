package com.wraitnell.scheduler.webhook;

import com.wraitnell.scheduler.config.Config;
import com.wraitnell.scheduler.player.Player;
import com.wraitnell.scheduler.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

@Service
public class WebhookService {
    // This is the service that handles talking to the discord bot. All the nitty gritty string parsing is done in here.

    @Autowired
    private Config config;

    private DiscordMessage sendMessageToSessionQueue(String message) {

        final RestClient sessionQueueClient = RestClient.builder()
                .baseUrl(config.getUrl())
                .build();

        WebhookMessage testMessage = new WebhookMessage(message);

        return sessionQueueClient.post()

                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(testMessage)
                .retrieve()
                .body(DiscordMessage.class);
    }

    private DiscordMessage sendMessageToSessionQueue(String message, String id) {

        // Overloaded method to handle editing an existing message

        final RestClient sessionQueueClient = RestClient.builder()
                .baseUrl(config.getUrl())
                .build();

        WebhookMessage testMessage = new WebhookMessage(message);
        // Discord wants a patch for editing messages, weird
        return sessionQueueClient.patch()

                .uri("/messages/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(testMessage)
                .retrieve()
                .body(DiscordMessage.class);
    }


    private String buildSessionPost (Session session) {

        // TODO: This whole method could probably use reworking
        // There has to be a better way to iterate over object sets that might be null
        // Instantiate them to empty in the constructor maybe?

        String message = "**༺  ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ༻**"
                // this line needs to send the time in ms, defaults to microseconds so div by 1000
                + "\n**Session **" + session.getId() + " <t:" + session.getScheduledTimestamp()/1000 + ">"
                + "\n*Goal: " + session.getSessionGoal() + "*"
                + "\n*Level: " + session.getSessionLevel() + "*";

        // Add tokened players and leave a blank spot for empty token slots
        Set<Player>playersToPrint = session.getTokenPlayers();


        System.out.println("I'm in buildSessionPost and I have to print " + session.getTokenPlayers() + " players");

        for (int i = 0; i < config.getMaxSessionPlayers(); i++) {

            if (playersToPrint != null && !playersToPrint.isEmpty()) {

                // We have some players to add
                //TODO: this trash checks to see if the captain is there every time, I can find a better solution
                if (playersToPrint.contains(session.getSessionCaptain())
                        && session.getSessionCaptain() != null) {

                    // We have the captain in the set, add them then remove from the set
                    message += "\n- @" + session.getSessionCaptain().getId() + " (Captain)";
                    playersToPrint.remove(session.getSessionCaptain());

                } else if (!playersToPrint.isEmpty()) { // more players to add

                    message += "\n- @" + playersToPrint.iterator().next();

                }
            } else {
                // We need to print some empty spaces
                message += "\n-";
            }
        }

        // Add queued players

        message += "\n**༺ Initiative Queue: ༻**";

        playersToPrint = session.getQueuePlayers();

        if (playersToPrint != null && !playersToPrint.isEmpty()) {
            for (Player player : session.getQueuePlayers()) {
                message += "\n- @" + player.getId();
            }
        }

        return message;
    }

    public String postNewSession(Session session) {

        // return message ID to caller
        return sendMessageToSessionQueue(buildSessionPost(session)).getId();
    }

    public DiscordMessage editSessionPost (Session session) {
        return sendMessageToSessionQueue(buildSessionPost(session),session.getDiscordMessageId());
    }

    public WebhookService() {
    }

    public WebhookService(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
