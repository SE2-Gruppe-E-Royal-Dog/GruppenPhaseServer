package com.uni.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.communication.dto.*;
import com.uni.game.GameCoordinator;
import com.uni.game.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class WebSocketHandlerTest {
    @MockBean
    private WebSocketSession webSocketSession;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void givenJoinLobbyMessage_whenNoLobbyExists_expectSendJoinedLobbyMessageIsSend() throws Exception {
        var msg = new Message();
        msg.setType(MessageType.JOIN_LOBBY);
        var payload = new NewPlayerPayload();
        payload.setPlayerName("player-1");
        msg.setPayload(objectMapper.writeValueAsString(payload));
        var gamecoordinator = new GameCoordinator();
        var webSocketHandler = new WebSocketHandler(gamecoordinator);
        webSocketHandler.handleTextMessage(webSocketSession, new TextMessage(objectMapper.writeValueAsString(msg)));
        var argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession).sendMessage(argumentCaptor.capture());

        var sendMessage = objectMapper.readValue(argumentCaptor.getValue().getPayload(), Message.class);
        assertThat(sendMessage.getType()).isEqualTo(MessageType.JOINED_LOBBY);

        assertThat(gamecoordinator.getLobbies()).hasSize(1);
        var lobby = gamecoordinator.getLobbies().get(0);
        assertThat(lobby.getPlayers()).hasSize(1);

        var player = lobby.getPlayers().get(0);
        assertThat(player.getName()).isEqualTo(payload.getPlayerName());

        var joinedLobbyPayload = objectMapper.readValue(sendMessage.getPayload(), JoinedLobbyPayload.class);
        assertThat(joinedLobbyPayload.getLobbyId()).isEqualTo(lobby.getId());
    }

    @Test
    public void givenJoinLobbyMessage_whenALobbyWithAnotherPlayerExists_expectSendJoinedLobbyMessageIsSendAndNewPlayerJoinedMessageIsSend() throws Exception {
        var msg = new Message();
        msg.setType(MessageType.JOIN_LOBBY);
        var payload = new NewPlayerPayload();
        payload.setPlayerName("player-2");
        msg.setPayload(objectMapper.writeValueAsString(payload));

        var gamecoordinator = new GameCoordinator();

        var player1WebSocketSession = mock(WebSocketSession.class);
        gamecoordinator.addNewPlayerToLobby(new Player("player-1"), player1WebSocketSession);

        var webSocketHandler = new WebSocketHandler(gamecoordinator);
        webSocketHandler.handleTextMessage(webSocketSession, new TextMessage(objectMapper.writeValueAsString(msg)));
        var joinedLobbyCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession).sendMessage(joinedLobbyCaptor.capture());

        var joinedLobbyMessage = objectMapper.readValue(joinedLobbyCaptor.getValue().getPayload(), Message.class);
        assertThat(joinedLobbyMessage.getType()).isEqualTo(MessageType.JOINED_LOBBY);

        assertThat(gamecoordinator.getLobbies()).hasSize(1);
        var lobby = gamecoordinator.getLobbies().get(0);
        assertThat(lobby.getPlayers()).hasSize(2);

        var player1InPayload = lobby.getPlayers().get(0);
        assertThat(player1InPayload.getName()).isEqualTo("player-1");

        var player2InPayload = lobby.getPlayers().get(1);
        assertThat(player2InPayload.getName()).isEqualTo("player-2");

        var joinedLobbyPayload = objectMapper.readValue(joinedLobbyMessage.getPayload(), JoinedLobbyPayload.class);
        assertThat(joinedLobbyPayload.getLobbyId()).isEqualTo(lobby.getId());

        var playerJoinedLobbyCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(player1WebSocketSession).sendMessage(playerJoinedLobbyCaptor.capture());

        var anotherPlayerJoinedTheLobbyMessage = objectMapper.readValue(playerJoinedLobbyCaptor.getValue().getPayload(), Message.class);
        assertThat(anotherPlayerJoinedTheLobbyMessage.getType()).isEqualTo(MessageType.NEW_PLAYER_JOINED);

        var newPlayerJoinedPayload = objectMapper.readValue(anotherPlayerJoinedTheLobbyMessage.getPayload(), NewPlayerJoinedLobbyPayload.class);
        assertThat(newPlayerJoinedPayload.getPlayerName()).isEqualTo("player-2");

    }
}