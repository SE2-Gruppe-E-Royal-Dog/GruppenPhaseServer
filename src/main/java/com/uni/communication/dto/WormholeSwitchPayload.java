package com.uni.communication.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WormholeSwitchPayload {

    int newWormholeFieldPosition1;
    int newWormholeFieldPosition2;
    int newWormholeFieldPosition3;
    int newWormholeFieldPosition4;

    String lobbyID;
}
