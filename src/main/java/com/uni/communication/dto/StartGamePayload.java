package com.uni.communication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartGamePayload {

    String lobbyID;
    int numberOfPlayers;
    int clientPlayerNumber;
    List<String> playerNames;
}
