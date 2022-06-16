package com.uni.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.carddeck.Card;
import com.uni.carddeck.Deck;
import com.uni.communication.dto.Message;
import com.uni.communication.dto.MessageType;
import com.uni.communication.dto.SendCardsPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@Getter
@Setter
public class Lobby {
    private final String id;
    private final List<Player> players;
    private final Map<String, WebSocketSession> sessions;
    private Deck deck;
    private boolean isStarted;

    public Lobby() {
        id = UUID.randomUUID().toString();
        players = new ArrayList<>();
        sessions = new HashMap<>();
        deck = new Deck();
    }

    public void addPlayer(Player player, WebSocketSession webSocketSession) {
        players.add(player);
        sessions.put(player.getId(), webSocketSession);
    }

    public String removePlayerAndReturnPlayerName(String playerId) {
        var player = players.stream().filter(c -> c.getId().equals(playerId)).findFirst().orElseThrow();
        var playerName = player.getName();

        players.remove(player);
        sessions.remove(playerId);

        return playerName;
    }

    public boolean isWaitingForPlayers() {
        return players.size() < 4;
    }

    public boolean isNotStarted() {
        return !isStarted;
    }

    public Player getPlayerByID(String playerID){
        for(Player p:players){
            if(p.getId().equals(playerID)){
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid Player ID");
    }

    public void dealCards() throws JsonProcessingException {
        for(Player p:getPlayers()){
            if(p.getNumOfCardsLeft()!=0){
                return;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();

        for(Player p:players){
            LinkedList<Card> cards = (LinkedList<Card>) deck.drawCards(5);

            var payload = new SendCardsPayload(cards);
            var message = new Message();
            message.setType(MessageType.SEND_CARDS);
            message.setPayload(objectMapper.writeValueAsString(payload));
            var textMessage = new TextMessage(objectMapper.writeValueAsString(message));

            var lobby = this;
            try {
                lobby.getSessions().get(p.getId()).sendMessage(textMessage);
            } catch (IOException e) {
                log.error("Unable to notify player {} about new Player", p.getId());
            }
        }
    }

    public List<String> getPlayerNames (){
        LinkedList<String> names = new LinkedList<>();
        for (Player p: players){
            names.add(p.getNameOfPlayer());
        }
        return names;
    }
}
