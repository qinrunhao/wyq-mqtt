package com.wyq.mqtt.dto;

import lombok.Getter;

@Getter
public class MqttV3Client {
    private String clientId;

    public MqttV3Client(String clientId) {
        this.clientId = clientId;
    }
}
