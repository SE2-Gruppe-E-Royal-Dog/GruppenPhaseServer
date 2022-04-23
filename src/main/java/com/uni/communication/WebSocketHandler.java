package com.uni.communication;


import static com.uni.communication.dto.MessageType.CHEATING_TILT_LEFT;
import static com.uni.communication.dto.MessageType.CHEATING_TILT_RIGHT;
import static com.uni.communication.dto.MessageType.JOIN_LOBBY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.communication.dto.*;
import com.uni.game.GameCoordinator;
import com.uni.game.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final GameCoordinator gameCoordinator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketHandler(GameCoordinator gameCoordinator) {
        this.gameCoordinator = gameCoordinator;
    }

    @Override
    public void handleTextMessage(WebSocketSession webSocketSession, TextMessage textMessage) throws Exception {
        super.handleTextMessage(webSocketSession, textMessage);

        var websocketMessage = objectMapper.readValue(textMessage.getPayload(), Message.class);


        switch (websocketMessage.getType()){
            case JOIN_LOBBY:
                handleNewPlayerMessage(webSocketSession, websocketMessage.getPayload());
                break;
            case CHEATING_TILT_RIGHT:
                //TODO add handling of cheating
                break;
            case CHEATING_TILT_LEFT:
                //TODO add handling of cheating
                break;
            default:
                break;
        }

    }

    private TextMessage getJoinedLobbyMessage(String lobbyId) throws JsonProcessingException {
        var payload = new JoinedLobbyPayload(lobbyId);
        var message = new Message();
        message.setType(MessageType.JOINED_LOBBY);
        message.setPayload(objectMapper.writeValueAsString(payload));
        return new TextMessage(objectMapper.writeValueAsString(message));
    }

    private String handleNewPlayerMessage(WebSocketSession webSocketSession, String body) throws IOException {
        var payload = objectMapper.readValue(body, NewPlayerPayload.class);
        var newPlayer = new Player(payload.getPlayerName());
        var lobbyId = gameCoordinator.addNewPlayerToLobby(newPlayer, webSocketSession);

        publishPlayerJoinedMessage(lobbyId, newPlayer);
        webSocketSession.sendMessage(getJoinedLobbyMessage(lobbyId));

        return lobbyId;
    }

    private void publishPlayerJoinedMessage(String lobbyId, Player newPlayer) throws JsonProcessingException {
        var lobby = gameCoordinator.getLobby(lobbyId);
        var playersToNotify = lobby.getPlayers().stream().filter(c -> !Objects.equals(c.getId(), newPlayer.getId())).collect(Collectors.toList());

        var payload = new NewPlayerJoinedLobbyPayload(newPlayer.getName());
        var message = new Message();
        message.setType(MessageType.NEW_PLAYER_JOINED);
        message.setPayload(objectMapper.writeValueAsString(payload));
        var textMessage = new TextMessage(objectMapper.writeValueAsString(message));

        for (Player c : playersToNotify) {
            try {
                lobby.getSessions().get(c.getId()).sendMessage(textMessage);
            } catch (IOException e) {
                log.error("Unable to notify player {} about new Player", c.getId());
            }
        }
    }


}
