package com.uni.communication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartGamePayload {

    String lobbyID;
    int numberOfPlayers;
    int clientPlayerNumber;
    LinkedList<String> playerNames;
}
