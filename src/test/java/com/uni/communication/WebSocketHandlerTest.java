package com.uni.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.communication.dto.*;
import com.uni.game.GameCoordinator;
import com.uni.game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class WebSocketHandlerTest {
    @MockBean
    private WebSocketSession webSocketSession;
    private final ObjectMapper objectMapper = new ObjectMapper();

    GameCoordinator gameCoordinator;
    Player player1;
    Player player2;
    WebSocketHandler webSocketHandler;


    @BeforeEach
    void setUp(){
        gameCoordinator = new GameCoordinator();
        player1 = new Player("player-1");
        player2 = new Player("player-2");
        webSocketHandler = new WebSocketHandler(gameCoordinator);
    }

    @Test
    void givenJoinLobbyMessage_whenNoLobbyExists_expectJoinedLobbyMessageIsSend() throws Exception {
        var msg = new Message();
        msg.setType(MessageType.JOIN_LOBBY);
        var payload = new NewPlayerPayload();
        payload.setPlayerName("player-1");
        msg.setPayload(objectMapper.writeValueAsString(payload));
        var gameCoordinator = new GameCoordinator();
        var webSocketHandler = new WebSocketHandler(gameCoordinator);
        webSocketHandler.handleTextMessage(webSocketSession, new TextMessage(objectMapper.writeValueAsString(msg)));
        var argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession).sendMessage(argumentCaptor.capture());

        var sendMessage = objectMapper.readValue(argumentCaptor.getValue().getPayload(), Message.class);
        assertThat(sendMessage.getType()).isEqualTo(MessageType.JOINED_LOBBY);

        assertThat(gameCoordinator.getLobbies()).hasSize(1);
        var lobby = gameCoordinator.getLobbies().get(0);
        assertThat(lobby.getPlayers()).hasSize(1);

        var player = lobby.getPlayers().get(0);
        assertThat(player.getName()).isEqualTo(payload.getPlayerName());

        var joinedLobbyPayload = objectMapper.readValue(sendMessage.getPayload(), JoinedLobbyPayload.class);
        assertThat(joinedLobbyPayload.getLobbyId()).isEqualTo(lobby.getId());
        assertThat(joinedLobbyPayload.getPlayerId()).isEqualTo(player.getId());
    }

    @Test
    void givenJoinLobbyMessage_whenALobbyWithAnotherPlayerExists_expectJoinedLobbyMessageIsSendAndNewPlayerJoinedMessageIsSend() throws Exception {
        var msg = new Message();
        msg.setType(MessageType.JOIN_LOBBY);
        var payload = new NewPlayerPayload();
        payload.setPlayerName("player-2");
        msg.setPayload(objectMapper.writeValueAsString(payload));

        var gameCoordinator = new GameCoordinator();

        var player1WebSocketSession = mock(WebSocketSession.class);
        gameCoordinator.addNewPlayerToLobby(new Player("player-1"), player1WebSocketSession);

        var webSocketHandler = new WebSocketHandler(gameCoordinator);
        webSocketHandler.handleTextMessage(webSocketSession, new TextMessage(objectMapper.writeValueAsString(msg)));
        var joinedLobbyCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession).sendMessage(joinedLobbyCaptor.capture());

        var joinedLobbyMessage = objectMapper.readValue(joinedLobbyCaptor.getValue().getPayload(), Message.class);
        assertThat(joinedLobbyMessage.getType()).isEqualTo(MessageType.JOINED_LOBBY);

        assertThat(gameCoordinator.getLobbies()).hasSize(1);
        var lobby = gameCoordinator.getLobbies().get(0);
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

    @Test
    void givenLeaveLobbyMessage_expectLeftLobbyMessageIsSend() throws Exception {
        var gameCoordinator = new GameCoordinator();

        var player = new Player("player-1");
        var player2 = new Player("player-2");

        gameCoordinator.addNewPlayerToLobby(player, webSocketSession);
        gameCoordinator.addNewPlayerToLobby(player2, webSocketSession);
        var webSocketHandler = new WebSocketHandler(gameCoordinator);

        var lobby = gameCoordinator.getLobbies().get(0);
        var player1 = lobby.getPlayers().get(0);

        var msg = new Message();
        msg.setType(MessageType.LEAVE_LOBBY);
        var payload = new LeaveLobbyPayload();
        payload.setPlayerId(player1.getId());
        payload.setLobbyId(lobby.getId());
        msg.setPayload(objectMapper.writeValueAsString(payload));

        webSocketHandler.handleTextMessage(webSocketSession, new TextMessage(objectMapper.writeValueAsString(msg)));
        var argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession).sendMessage(argumentCaptor.capture());

        var sendMessage = objectMapper.readValue(argumentCaptor.getValue().getPayload(), Message.class);
        assertThat(sendMessage.getType()).isEqualTo(MessageType.PLAYER_LEFT_LOBBY);
        var leftLobbyPayload = objectMapper.readValue(sendMessage.getPayload(), PlayerLeftLobbyPayload.class);

        assertThat(leftLobbyPayload.getPlayerName()).isEqualTo("player-1");
    }

    @Test
    void testUpdateBoard()throws Exception{

        gameCoordinator.addNewPlayerToLobby(player1, webSocketSession);
        gameCoordinator.addNewPlayerToLobby(player2, webSocketSession);
        var lobby = gameCoordinator.getLobbies().get(0);

        Message message = new Message();
        message.setType(MessageType.UPDATE_BOARD);
        UpdateBoardPayload payload = new UpdateBoardPayload(1, 23, -1, -1, 2, 0, lobby.getId());
        message.setPayload(objectMapper.writeValueAsString(payload));

        webSocketHandler.handleTextMessage(webSocketSession, new TextMessage(objectMapper.writeValueAsString(message)));
        var argumentCaptor = ArgumentCaptor.forClass(TextMessage.class);
        verify(webSocketSession, times(2)).sendMessage(argumentCaptor.capture());

        var sendMessage = objectMapper.readValue(argumentCaptor.getValue().getPayload(), Message.class);
        assertThat(sendMessage.getType()).isEqualTo(MessageType.UPDATE_BOARD);
        var resultPayload = objectMapper.readValue(sendMessage.getPayload(), UpdateBoardPayload.class);

        assertThat(resultPayload.getCardType() == 2);
        assertThat(resultPayload.getCheatModifier() == 0);
        assertThat(resultPayload.getFigure1ID() == 1);
        assertThat(resultPayload.getFigure2ID() == -1);
        assertThat(resultPayload.getLobbyID()== lobby.getId());
        assertThat(resultPayload.getNewField1ID()== 23);
        assertThat(resultPayload.getNewField2ID()== -1);
    }

}