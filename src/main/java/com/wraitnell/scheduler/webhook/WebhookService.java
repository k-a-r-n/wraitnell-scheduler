package com.wraitnell.scheduler.webhook;

import com.wraitnell.scheduler.config.Config;
import com.wraitnell.scheduler.player.Player;
import com.wraitnell.scheduler.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Set;

@Service
public class WebhookService {
    // This is the service that handles talking to the discord webhook.
    // Additionally, all the nitty gritty string parsing that makes the posts pretty is done here.

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
        // Discord wants patch for editing messages, weird
        return sessionQueueClient.patch()

                .uri("/messages/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(testMessage)
                .retrieve()
                .body(DiscordMessage.class);
    }

    public void deleteMessageFromSessionQueue (String id) {
        final RestClient sessionQueueClient = RestClient.builder()
                .baseUrl(config.getUrl())
                .build();

        sessionQueueClient.delete()
                .uri("/messages/"+ id);
    }

    private String buildSessionPost (Session session) {

        // TODO: I don't like the hardcoded values here - I don't see things like the header ever changing,
        //       but it would make me feel better to make all of these values configurable

        String message = "**༺  ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ༻**"
                // this line needs to send the time in ms, defaults to microseconds so div by 1000
                + "\n**Session **" + session.getId() + " <t:" + session.getScheduledTimestamp()/1000 + ">"
                + "\n*Goal: " + session.getSessionGoal() + "*"
                + "\n*Level: " + session.getSessionLevel() + "*";

        // Add tokened players and leave a blank spot for empty token slots
        Set<Player>playersToPrint = session.getTokenPlayers();

        // This section iterates over the playersToPrint set and removes them as they're printed,
        // and when there are no more players we fill the rest with blank spaces. I feel like there
        // should be a better way to do this, but it works for now and performance isn't really a concern.

        for (int i = 0; i < config.getMaxSessionPlayers(); i++) {
            if (playersToPrint != null && !playersToPrint.isEmpty()) {
                // We have some players to add

                // TODO: surely we don't need to do this check every time

                if (playersToPrint.contains(session.getSessionCaptain())
                        && session.getSessionCaptain() != null) {
                    // We have the captain in the set, add them to the post and then remove them from the set

                    message += "\n- <@" + session.getSessionCaptain().getId() + "> (Captain)";
                    // Discord needs format <@{userid}> to successfully ping
                    playersToPrint.remove(session.getSessionCaptain());

                } else if (!playersToPrint.isEmpty()) { // more players to add

                    message += "\n- <@" + playersToPrint.iterator().next().getId() + ">";
                    playersToPrint.remove(playersToPrint.iterator().next()); // apparently next() does not remove
                }
            } else {
                // No more players, we need to print some empty spaces
                message += "\n-";
            }
        }

        // Add queued players

        message += "\n**༺ Initiative Queue: ༻**";

        playersToPrint = session.getQueuePlayers();

        if (playersToPrint != null && !playersToPrint.isEmpty()) {
            for (Player player : session.getQueuePlayers()) {
                message += "\n- <@" + player.getId() + ">";
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
