package com.wraitnell.scheduler.webhook;

import com.wraitnell.scheduler.config.Config;
import com.wraitnell.scheduler.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Set;

@Service
public class WebhookService {
    // This is the service that handles talking to the discord bot. All the nitty gritty string parsing is done in here.

    //@Autowired
    //private Config config;
    private RestClient sessionQueueClient;

    public WebhookService() {
        // Build our rest client
        sessionQueueClient = RestClient.builder()
                .baseUrl("https://discord.com/api/webhooks/1225545467979497622/Fdw2PyKMjoL-HXBoZ4aQu4-ovNfWFfCK4EhG1-JEg1ei4jk4HTics9wEOFl4iytpn_IY")
                .build();
    }

    public void setServerPlayerList(Set<Player> players) {
        // Make a call to the web hook for the discord to post a message
        // The bot will format the list into the pretty discord message and send it to the server
    }

    public void testWebhook(String message) {

        System.out.println("I'm gonna send " + message);
        WebhookMessage testMessage = new WebhookMessage(message);
        sessionQueueClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"content\": \"Test webhook\"}");

    }

//    public Config getConfig() {
//        return config;
//    }
//
//    public void setConfig(Config config) {
//        this.config = config;
//    }

}
