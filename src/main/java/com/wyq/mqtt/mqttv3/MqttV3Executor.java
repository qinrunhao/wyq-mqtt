package com.wyq.mqtt.mqttv3;

import com.wyq.mqtt.callback.ConnectCallback;
import com.wyq.mqtt.config.EmqXConfig;
import com.wyq.mqtt.dto.MqttV3Client;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @program: dev
 * @description:
 * @author: heyede
 * @create: 2019-08-02
 **/
@Slf4j
@Component
public class MqttV3Executor implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EmqXConfig config;
    @Autowired
    private MqttV3Client mqttV3Client;
    private IMqttAsyncClient mqttAsyncClient;
    private IMqttToken connectToken;

    private IMqttToken connect() throws MqttException {
        if(connectToken != null){
            return connectToken;
        }
        mqttAsyncClient = new MqttAsyncClient(config.getHost(), mqttV3Client.getClientId());
        mqttAsyncClient.setCallback(new ConnectCallback(config.getReconnectionSeconds(), mqttAsyncClient));
        connectToken = mqttAsyncClient.connect(config.getConnectOptions());
        return connectToken;
    }

    public IMqttAsyncClient getMqttAsyncClient() throws MqttException {
        return connect().getClient();
    }

    public IMqttAsyncClient getMqttClient() throws MqttException {
        IMqttToken mqttToken = connect();
        mqttToken.waitForCompletion();
        return mqttToken.getClient();
    }

    public void disconnect() {
        try {
            if (mqttAsyncClient != null) {
                mqttAsyncClient.disconnect();
            }
        } catch (MqttException e) {
           log.error("MqttV3Executor disconnect MqttException error",e);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            connect();
        } catch (MqttException e) {
            log.error("MQTT连接异常", e);
        }
    }
}
