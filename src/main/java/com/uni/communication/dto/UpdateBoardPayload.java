package com.uni.communication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBoardPayload {
    int figure1ID;
    int figure2ID;
    int newField1ID;
    int newField2ID;
    int cardType;
    int cheatModifier;
    String lobbyID;
    String playerID;
}
