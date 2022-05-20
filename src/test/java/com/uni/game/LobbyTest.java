package com.uni.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.socket.WebSocketSession;

class LobbyTest {

    Lobby lobby;
    Player player1;
    Player player2;

    @BeforeEach
    void setup(){
        player1 = new Player("1");
        player2 = new Player("2");
        lobby = new Lobby();
        lobby.addPlayer(player1,webSocketSession);
        lobby.addPlayer(player2,webSocketSession);
    }

    @AfterEach
    void cleanup(){
        lobby = null;
        player1 = null;
        player2 = null;
    }

    @Mock
    WebSocketSession webSocketSession;

    @Test
    void getPlayerByIDTest(){
        Assertions.assertEquals(player1, lobby.getPlayerByID(player1.getId()));
    }
}
