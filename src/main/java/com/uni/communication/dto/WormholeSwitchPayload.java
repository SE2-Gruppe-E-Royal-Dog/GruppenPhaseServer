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

    int newWormholeFieldPosition_1;
    int newWormholeFieldPosition_2;
    int newWormholeFieldPosition_3;
    int newWormholeFieldPosition_4;

    String lobbyID;
}
