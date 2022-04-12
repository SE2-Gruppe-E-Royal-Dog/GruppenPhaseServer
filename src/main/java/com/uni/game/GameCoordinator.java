package com.uni.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GameCoordinator {
    private Map<String, Lobby> lobbies;

    public GameCoordinator() {
        this.lobbies = new HashMap<>();
    }

    public String addNewPlayerToLobby(Player player, WebSocketSession webSocketSession) {
        var maybeAvailableLobby = lobbies.values().stream().filter(Lobby::isNotStarted).filter(Lobby::isWaitingForPlayers).findAny();

        if (maybeAvailableLobby.isPresent()) {
            maybeAvailableLobby.get().addPlayer(player, webSocketSession);
            log.info("Added player {} to lobby {}", player.getName(), maybeAvailableLobby.get().getLobbyId());
            return maybeAvailableLobby.get().getLobbyId();
        }

        var lobby = new Lobby();
        lobby.addPlayer(player, webSocketSession);

        lobbies.put(lobby.getLobbyId(), lobby);

        log.info("Added player {} to lobby {}", player.getName(), lobby.getLobbyId());
        return lobby.getLobbyId();
    }

    public Lobby getLobby(String lobbyId) {
        return lobbies.get(lobbyId);
    }

    public Map<String, Lobby> getLobbies() {
        return lobbies;
    }
}
