package com.uni.game;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.assertThat;

class GameCoordinatorTest {

    @Mock
    private WebSocketSession webSocketSession;

    @Test
    void givenNewGameCoordinator_whenAddingANewPlayer_expectNewLobbyWithThisPlayerIsCreated() {
        var gameCoordinator = new GameCoordinator();

        var player1 = new Player("player-1");

        gameCoordinator.addNewPlayerToLobby(player1, webSocketSession);

        var lobbies = gameCoordinator.getLobbies();

        assertThat(lobbies).hasSize(1);
        var lobby = lobbies.stream().findFirst().orElseThrow();

        assertThat(lobby.getId()).isNotBlank();
        assertThat(lobby.getPlayers()).hasSize(1);

        var playerInLobby = lobby.getPlayers().get(0);

        comparePlayers(playerInLobby, player1);

        assertThat(lobby.getSessions()).hasSize(1);

        var socketSessionOfPlayer1 = lobby.getSessions().get(playerInLobby.getId());

        assertThat(socketSessionOfPlayer1).isEqualTo(webSocketSession);
    }

    @Test
    void givenGameCoordinatorAlreadyHasALobby_whenAddingANewPlayer_expectPlayerIsAddedToExisting() {
        var gameCoordinator = new GameCoordinator();
        var player1 = new Player("player-1");
        gameCoordinator.addNewPlayerToLobby(player1, webSocketSession);

        var player2 = new Player("player-2");
        gameCoordinator.addNewPlayerToLobby(player2, webSocketSession);

        var lobbies = gameCoordinator.getLobbies();

        assertThat(lobbies).hasSize(1);
        var lobby = lobbies.stream().findFirst().orElseThrow();

        assertThat(lobby.getId()).isNotBlank();
        assertThat(lobby.getPlayers()).hasSize(2);

        var player1InLobby = lobby.getPlayers().get(0);

        comparePlayers(player1InLobby, player1);

        assertThat(lobby.getSessions()).hasSize(2);

        var socketSessionOfPlayer1 = lobby.getSessions().get(player1InLobby.getId());

        assertThat(socketSessionOfPlayer1).isEqualTo(webSocketSession);

        var player2InLobby = lobby.getPlayers().get(1);

        comparePlayers(player2InLobby, player2);

        var socketSessionOfPlayer2 = lobby.getSessions().get(player2InLobby.getId());

        assertThat(socketSessionOfPlayer2).isEqualTo(webSocketSession);
    }

    @Test
    void givenGameCoordinatorAlreadyOnlyHasAFullLobby_whenAddingANewPlayer_expectNewLobbyIsCreatedAndNewPlayerIsAddedToIt() {
        var gameCoordinator = new GameCoordinator();
        var player1 = new Player("player-1");
        var player2 = new Player("player-2");
        var player3 = new Player("player-3");
        var player4 = new Player("player-4");
        gameCoordinator.addNewPlayerToLobby(player1, webSocketSession);
        gameCoordinator.addNewPlayerToLobby(player2, webSocketSession);
        gameCoordinator.addNewPlayerToLobby(player3, webSocketSession);
        gameCoordinator.addNewPlayerToLobby(player4, webSocketSession);

        var player5 = new Player("player-5");
        gameCoordinator.addNewPlayerToLobby(player5, webSocketSession);

        var lobbies = gameCoordinator.getLobbies();

        assertThat(lobbies).hasSize(2);

        var firstLobby = lobbies.get(0);
        assertThat(firstLobby.getPlayers()).hasSize(4);

        var player1InLobby = firstLobby.getPlayers().get(0);
        comparePlayers(player1InLobby, player1);
        assertThat(firstLobby.getSessions()).containsEntry(player1InLobby.getId(), webSocketSession);

        var player2InLobby = firstLobby.getPlayers().get(1);
        comparePlayers(player2InLobby, player2);
        assertThat(firstLobby.getSessions()).containsEntry(player2InLobby.getId(), webSocketSession);

        var player3InLobby = firstLobby.getPlayers().get(2);
        comparePlayers(player3InLobby, player3);
        assertThat(firstLobby.getSessions()).containsEntry(player3InLobby.getId(), webSocketSession);

        var player4InLobby = firstLobby.getPlayers().get(3);
        comparePlayers(player4InLobby, player4);
        assertThat(firstLobby.getSessions()).containsEntry(player4InLobby.getId(), webSocketSession);

        assertThat(firstLobby.isWaitingForPlayers()).isFalse();
        assertThat(firstLobby.isNotStarted()).isFalse();

        var secondLobby = lobbies.get(1);
        assertThat(secondLobby.getPlayers()).hasSize(1);
        var player5InLobby = secondLobby.getPlayers().get(0);
        comparePlayers(player5InLobby, player5);
        assertThat(secondLobby.getSessions()).containsEntry(player5InLobby.getId(), webSocketSession);
    }

    private void comparePlayers(Player player2InLobby, Player player2) {
        assertThat(player2InLobby.getId()).isEqualTo(player2.getId());
        assertThat(player2InLobby.getName()).isEqualTo(player2.getName());
    }
}