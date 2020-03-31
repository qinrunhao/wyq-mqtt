package com.wyq.mqtt.dto;

import lombok.Getter;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * 推送结果
 */
public class MqttResult {

    private Boolean isComplete;
    @Getter
    private MqttException exception;

    public MqttResult (IMqttToken token) {
        this.isComplete = token.getMessageId() > 0;
    }

    public MqttResult(IMqttDeliveryToken token) {
        this.isComplete = token.getMessageId() > 0;
    }

    public MqttResult(MqttException mqttException) {
        this.isComplete = false;
        this.exception = mqttException;
    }

    public Boolean isComplete() {
        return isComplete;
    }
}
