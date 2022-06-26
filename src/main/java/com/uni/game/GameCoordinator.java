package com.uni.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasse, die die Lobbies anlegt, Spieler zu lobbies hinzufügt und diese auch wieder entfernt.
 */
@Slf4j
public class GameCoordinator {
    private final List<Lobby> lobbies;

    public GameCoordinator() {
        this.lobbies = new ArrayList<>();
    }

    public String addNewPlayerToLobby(Player player, WebSocketSession webSocketSession) {
        var maybeAvailableLobby = lobbies.stream().filter(Lobby::isNotStarted).filter(Lobby::isWaitingForPlayers).findAny();

        if (maybeAvailableLobby.isPresent()) {
            maybeAvailableLobby.get().addPlayer(player, webSocketSession);
            log.info("Added player {} to lobby {}", player.getName(), maybeAvailableLobby.get().getId());
            return maybeAvailableLobby.get().getId();
        }

        var lobby = new Lobby();
        lobby.addPlayer(player, webSocketSession);

        lobbies.add(lobby);

        log.info("Added player {} to lobby {}", player.getName(), lobby.getId());
        return lobby.getId();
    }

    public String removePlayerFromLobbyAndReturnPlayerName(String lobbyId, String playerId) {
        var lobby = getLobby(lobbyId);
        return lobby.removePlayerAndReturnPlayerName(playerId);
    }

    public Lobby getLobby(String lobbyId) {
        return lobbies.stream().filter(c -> Objects.equals(c.getId(), lobbyId)).findFirst().orElseThrow();
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }


}
