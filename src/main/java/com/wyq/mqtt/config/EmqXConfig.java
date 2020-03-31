package com.wyq.mqtt.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @program:
 * @description:
 * @author: heyede
 **/
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "mqtt.config")
public class EmqXConfig implements Serializable {

    /***
     * emtqq 服务地址
     **/
    @Setter
    @Getter
    private String host = "";

    /**
     * 重连间隔，单位秒
     */
    @Getter
    @Setter
    private int reconnectionSeconds = 1;

    @Setter
    private ConnectOptions options = new ConnectOptions();


    public MqttConnectOptions getConnectOptions(){
        return options.getConnectOptions();
    }

}
