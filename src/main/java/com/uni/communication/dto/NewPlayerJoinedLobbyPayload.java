package com.uni.communication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPlayerJoinedLobbyPayload {
    String playerName;
}
