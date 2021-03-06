package com.uni.communication;


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

import static com.uni.communication.dto.MessageType.*;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final GameCoordinator gameCoordinator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String UNABLE_TO_NOTIFY_ERROR = "Unable to notify player {} about new Player";

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
            case LEAVE_LOBBY:
                handleLeaveLobbyMessage(websocketMessage.getPayload());
                break;
            case PUNISHMENT_MESSAGE:
                handlePunishment(websocketMessage.getPayload());
                break;
            case START_GAME:
                handleGameStartMessage(websocketMessage.getPayload());
                break;
            case WORMHOLE_MOVE:
                handleMoveWormholes(websocketMessage.getPayload());
                break;
            case UPDATE_BOARD:
                handleUpdateBoard(websocketMessage.getPayload());
                break;
            default:
                break;

        }

    }

    private void handleLeaveLobbyMessage(String payload) throws JsonProcessingException {
        var leaveLobbyPayload = objectMapper.readValue(payload, LeaveLobbyPayload.class);
        var playerName = gameCoordinator.removePlayerFromLobbyAndReturnPlayerName(leaveLobbyPayload.getLobbyId(), leaveLobbyPayload.getPlayerId());

        publishPlayerLeftMessage(leaveLobbyPayload.getLobbyId(), leaveLobbyPayload.getPlayerId(), playerName);
    }

    private TextMessage getJoinedLobbyMessage(String lobbyId, String playerId) throws JsonProcessingException {
        var payload = new JoinedLobbyPayload(lobbyId, playerId);
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
        webSocketSession.sendMessage(getJoinedLobbyMessage(lobbyId, newPlayer.getId()));

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
                log.error(UNABLE_TO_NOTIFY_ERROR, c.getId());
            }
        }
    }

    private void publishPlayerLeftMessage(String lobbyId, String playerId, String playerName) throws JsonProcessingException {
        var lobby = gameCoordinator.getLobby(lobbyId);
        var playersToNotify = lobby.getPlayers().stream().filter(c -> !Objects.equals(c.getId(), playerId)).collect(Collectors.toList());

        var payload = new PlayerLeftLobbyPayload(playerName);
        var message = new Message();
        message.setType(MessageType.PLAYER_LEFT_LOBBY);
        message.setPayload(objectMapper.writeValueAsString(payload));
        var textMessage = new TextMessage(objectMapper.writeValueAsString(message));

        for (var c : playersToNotify) {
            try {
                lobby.getSessions().get(c.getId()).sendMessage(textMessage);
            } catch (IOException e) {
                log.error(UNABLE_TO_NOTIFY_ERROR, c.getId());
            }
        }
    }

    private void handleGameStartMessage(String payload) throws JsonProcessingException {

        var newPayload = objectMapper.readValue(payload, StartGamePayload.class);

        var lobby = gameCoordinator.getLobby(newPayload.getLobbyID());
        var playersToNotify = lobby.getPlayers();

        newPayload.setPlayerNames(lobby.getPlayerNames());

        var message = new Message();
        message.setType(START_GAME);

        newPayload.setNumberOfPlayers(playersToNotify.size());
        int i = 0;
        for (var c : playersToNotify) {
            try {
                newPayload.setClientPlayerNumber(i++);
                message.setPayload(objectMapper.writeValueAsString(newPayload));
                var textMessage = new TextMessage(objectMapper.writeValueAsString(message));
                lobby.setStarted(true);
                lobby.getSessions().get(c.getId()).sendMessage(textMessage);
            } catch (IOException e) {
                log.error("Unable to start game");
            }
        }
        lobby.dealCards();
    }
    private void handleMoveWormholes(String payload) throws JsonProcessingException {

        var newPayload = objectMapper.readValue(payload, WormholeSwitchPayload.class);

        var lobby = gameCoordinator.getLobby(newPayload.getLobbyID());
        var playersToNotify = lobby.getPlayers();

        var message = new Message();
        message.setType(WORMHOLE_MOVE);

        for (var c : playersToNotify) {
            try {
                message.setPayload(objectMapper.writeValueAsString(newPayload));
                var textMessage = new TextMessage(objectMapper.writeValueAsString(message));

                lobby.getSessions().get(c.getId()).sendMessage(textMessage);
            } catch (IOException e) {
                log.error(UNABLE_TO_NOTIFY_ERROR, c.getId());
            }
        }
    }

    private void handleUpdateBoard(String payload)throws JsonProcessingException {
        var newPayload = objectMapper.readValue(payload, UpdateBoardPayload.class);

        var lobby = gameCoordinator.getLobby(newPayload.getLobbyID());
        var playersToNotify = lobby.getPlayers();

        var message = new Message();
        message.setType(UPDATE_BOARD);

        for (var c : playersToNotify) {
            try {
                message.setPayload(objectMapper.writeValueAsString(newPayload));
                var textMessage = new TextMessage(objectMapper.writeValueAsString(message));

                lobby.getSessions().get(c.getId()).sendMessage(textMessage);
            } catch (IOException e) {
                log.error("Unable to update board");
            }
        }
        lobby.getPlayerByID(newPayload.getPlayerID()).reduceCardsLeft();
        lobby.dealCards();
    }

    private void handlePunishment(String payload) throws JsonProcessingException{
        var newPayload = objectMapper.readValue(payload, PunishPayload.class);

        var lobby = gameCoordinator.getLobby(newPayload.getLobbyID());
        var playersToNotify = lobby.getPlayers();

        var message = new Message();
        message.setType(PUNISHMENT_MESSAGE);

        for (var c : playersToNotify) {
            try {
                message.setPayload(objectMapper.writeValueAsString(newPayload));
                var textMessage = new TextMessage(objectMapper.writeValueAsString(message));

                lobby.getSessions().get(c.getId()).sendMessage(textMessage);
            } catch (IOException e) {
                log.error("Unable to notify about punishment");
            }
        }

    }
}
