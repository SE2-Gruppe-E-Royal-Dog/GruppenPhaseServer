package com.uni.game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Slf4j
@Getter
public class Lobby {
    private final String id;
    private final List<Player> players;
    private final Map<String, WebSocketSession> sessions;

    public Lobby() {
        id = UUID.randomUUID().toString();
        players = new ArrayList<>();
        sessions = new HashMap<>();
    }

    public void addPlayer(Player player, WebSocketSession webSocketSession) {
        players.add(player);
        sessions.put(player.getId(), webSocketSession);
    }

    public boolean isWaitingForPlayers() {
        return players.size() < 4;
    }

    public boolean isNotStarted() {
        return players.size() < 4;
    }
}
