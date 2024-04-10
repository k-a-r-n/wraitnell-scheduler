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

    // Handles all the session-related business logic and I/O

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private Config config;

    @Autowired
    private WebhookService webhookService;

    public Session addSession(Session session) {

        // I feel like I should be able to do this without two writes. Is there a method to get the next ID from the db?
        //   Would that be reliable if this scales up? I can't think of another way that wouldn't run into problems

        session = sessionRepository.save(session); // We have to do this first so we have a session ID
        // post the message to the discord channel, and we should wait to get the ID back so we can save it to the table
        String messageId =  webhookService.postNewSession(session);
        session.setDiscordMessageId(messageId);
        // Sending false here means we won't update the post, so that makes this a bit faster at least
        updateSession(session,false);
        return session;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElseThrow(
                () -> new SchedulerExceptionNotFound("No session with ID: " + id));
    }

    public Session updateSession(Session session) {

        // Something weird was happening here. When I updated the session first, it was not saving
        // the player sets. I can only assume editSessionPost was fucking it up somehow, but I have no idea
        // why that would happen. Reversing the order solved this problem, but someday I'd like to figure out why
        // it happened. It would be nice to do the update and return on one line.

        session = sessionRepository.save(session);

        // Edit discord message with updated info
        //  We could do more testing here, we really only need to update the post when certain attributes change.
        //  Then we wouldn't need the overloaded method

        webhookService.editSessionPost(session);

        return session;
    }

    public Session updateSession(Session session, Boolean updatePost) {

        // Overloaded method to optionally not edit the discord post
        // Only use this method when there are changes to the session that don't need to be written to the Discord server

        session = sessionRepository.save(session);

        // Edit discord message with updated info if requested (should probably use the method that just takes the ID)
        if (updatePost) {
            webhookService.editSessionPost(session);
        }

        return session;
    }

    public void deleteSession(Long id) {
        // Delete the discord message first
        webhookService.deleteMessageFromSessionQueue(getSessionById(id).getDiscordMessageId());
        sessionRepository.delete(getSessionById(id));
    }

    public void tokenPlayerForSession (String playerId, Long sessionId) {

        // This method adds a player to the session with a token
        // If the player is already queued, they'll be removed from the queue and added to the tokened players list

        Session session = getSessionById(sessionId);                  // Session we're adding to
        Player newPlayer = playerService.getPlayerById(playerId);     // Player we're adding

        if(session.getTokenPlayers().size() < config.getMaxSessionPlayers()) {
            // Don't add more players than the max
            throw new SchedulerExceptionCantToken("session with ID: " + sessionId + " is full.");

        } else  if(playerService.getTokens(playerId) < 1 ) {
            // Don't add if player doesn't have any tokens
            throw new SchedulerExceptionCantToken("player with ID: " + playerId + " has no tokens.");

        } else { // OK to proceed

            if(newPlayer.getQueueSessions().contains(session)) {
                // Player is already queued, remove them from the queue
                session.unQueuePlayerForSession(newPlayer);
            }

            session.tokenPlayerForSession(newPlayer);
            updateSession(session);
        }
    }

    public void unTokenPlayerForSession (String playerId, Long sessionId) {

        // Remove a player from the session's tokened players

        Session session = getSessionById(sessionId);
        Player byePlayer = playerService.getPlayerById(playerId);

        session.unTokenPlayerForSession(byePlayer);
        updateSession(session);
    }

    public boolean queuePlayerForSession (String playerID, Long sessionId) {

        // Add player to session queue
        // If the player is already tokened for this session, they will be un-tokened and added to the queue

        Session session = getSessionById(sessionId);                  // Session we're adding to
        Player newPlayer = playerService.getPlayerById(playerID);     // Player we're adding

        if(newPlayer.getTokenSessions().contains(session)) {
            // Player is already tokened for this session, remove them first
            session.unTokenPlayerForSession(newPlayer);
        }

        session.queuePlayerForSession(newPlayer);                     // We don't care how many players queue
        updateSession(session);

        return true;
    }

    public boolean unQueuePlayerForSession (String playerID, Long sessionId) {

        // Remove player from session queue

        Session session = getSessionById(sessionId);                  // Session we're adding to
        Player byePlayer = playerService.getPlayerById(playerID);     // Player we're adding
        session.unQueuePlayerForSession(byePlayer);
        updateSession(session);

        return true;
    }

    public Set<Player> getTokenPlayersForSessionId(Long id) {
        return getSessionById(id).getTokenPlayers();
    }

    public Set<Player> getQueuedPlayersForSessionId(Long id) {
        return getSessionById(id).getQueuePlayers();
    }

    public void startSession(Long id, Long campaignStartTime) {

        //  set the campaignStartDate for the session
        Session session = getSessionById(id);
        session.setCampaignStartDate(campaignStartTime);
        updateSession(session, false); // We don't need to update the discord post with the start time

        // Promote - move players from queue to token set

        if (getSessionById(id).getTokenPlayers().size() < config.getMaxSessionPlayers()
                && (!getSessionById(id).getTokenPlayers().isEmpty())) {

            // There are open slots and we have queued players. Move enough queued players for a full session
            // into the token list, but do not save

            // Sort queued players into lastQueueSession date order, then lastSessionDate order
            //    This will be fun

            // Add enough queue players to hit max players

        }

        // set last campaign id for all players on session
        //   At this point all players (even those who queued) are in the token set thanks to the promote step,
        //   so this just works

        Set<Player> players = session.getTokenPlayers();
        for( Player player : players) {
            player.setLastSessionId(id);
            playerService.updatePlayer(player);
        }

        // TODO at this point we need to write this to the archive so we keep the list of players
        //      and we can't store that in the live db because it will screw up the tokens

    }

    public void endSession(Long id, Long campaignEndTime) {
        //  set the campaignEndDate for the session
        Session session = getSessionById(id);
        session.setCampaignStartDate(campaignEndTime);
        updateSession(session, false); // We don't need to update the discord post with the end time

        // Optionally delete the discord post
        if (config.isDeletePost()) {
            webhookService.deleteMessageFromSessionQueue(session.getDiscordMessageId());
        }

        //  TODO Tell the webhook to add a thread for the expedition log


    }

//    public Set<Session> getTokenSessionsByPlayerId(String id) {
//        // Base method for retrieving all token sessions for a player
//        // Retrieves only un-archived sessions for the player, so this method can be used to calculate tokens for the current cycle
//
//        return sessionRepository.findQueuedSessionsForPlayerId(id,false);
//    }
}