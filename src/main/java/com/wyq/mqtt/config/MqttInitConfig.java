package com.wyq.mqtt.config;

import com.wyq.mqtt.dto.MqttV3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class MqttInitConfig {

    @Value("${server.port}")
    private String port;

    @Bean
    @ConditionalOnMissingBean(value = {MqttV3Client.class})
    public MqttV3Client mqttV3Client() {
        try {
            InetAddress localHost = Inet4Address.getLocalHost();
            String ip = localHost.getHostAddress();
            String clientId = ip + ":" + port;
            return new MqttV3Client(clientId);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new MqttV3Client("unknown");
        }
    }
}
