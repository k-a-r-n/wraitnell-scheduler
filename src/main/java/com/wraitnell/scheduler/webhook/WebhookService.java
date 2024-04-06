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

        // This method handles both posting a new message and can be overloaded to edit an existing message.
        // We append ?wait=true to the URL in this method because we need the message ID back

        final RestClient sessionQueueClient = RestClient.builder()
                .baseUrl(config.getUrl() + "?wait=true")
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
                .baseUrl(config.getUrl()) // We don't actually need to wait here, ID does not change
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

        // Maybe this stuff should be in its own object?

        String message = "**༺  ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ༻**"
                // this line needs to send the time in ms, defaults to microseconds so div by 1000
                + "\n**Session **" + session.getId() + " <t:" + session.getScheduledTimestamp()/1000 + ">"
                + "\n*Goal: " + session.getSessionGoal() + "*"
                + "\n*Level: " + session.getSessionLevel() + "*";

        // Add tokened players and leave a blank spot for empty token slots
        Set<Player>playersToPrint = session.getTokenPlayers();

        // This section iterates over the playersToPrint set and removes them as they're printed,
        // and when there are no more players we fill the rest with blank spaces.

        for (int i = 0; i < config.getMaxSessionPlayers(); i++) {

            if (!playersToPrint.isEmpty()) {

                // We have some players to add

                // We always print the captain first, so if we're not on the first iteration we either already
                // printed them or there isn't one

                // Java evaluates if expressions left to right, so we only check for the captain on the first iteration
                // Maybe there's a better way to do this?

                if ( i==0 && playersToPrint.contains(session.getSessionCaptain())) {

                    // We have the captain in the set, add them to the post and then remove them from the set
                    message += "\n- <@" + session.getSessionCaptain().getId() + "> (Captain)";
                    playersToPrint.remove(session.getSessionCaptain());

                } else {

                    // non-captain players to add to post and remove from set
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

    private String buildPlayerRoster(Set<Player> players) {

        // This method builds the player roster that should appear above the session queue in the discord. It
        // should be re-built any time a player is tokened for a session, to reflect the number of tokens remaining.
        // It should also update to reflect the number of sessions the player has been on this cycle.

        String message = "**༻ 𝐒𝐞𝐬𝐬𝐢𝐨𝐧 𝐈𝐧𝐢𝐭𝐢𝐚𝐭𝐢𝐯𝐞 ༺**";

        for (Player player : players ) {
            // String should look like (0) Karn ●●
            // Num in parens is total sessions, dots are tokens remaining

            // TODO the total sessions attended number isn't going to show queued sessions, fix
            message += "\n(" + player.getTokenSessions().size() + ") " + player.getName() + " ";

            // print one dot for each token remaining
            for (int i = config.getMaxTokens(); i > player.getTokenSessions().size(); i-- ) {
                message += "●";
            }
        }

        return message + "༺~~༻";

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
