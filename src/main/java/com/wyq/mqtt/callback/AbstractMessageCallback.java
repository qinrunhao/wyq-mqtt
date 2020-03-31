package com.wyq.mqtt.callback;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public abstract class AbstractMessageCallback implements IMqttMessageListener {

    @Override
    public final void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("---------------------------------MQTT客户端接收到消息---------------------------------");
        log.info("消息内容：{}", new String(mqttMessage.getPayload()));
        log.info("消息质量qos：{}", mqttMessage.getQos());
        log.info("-------------------------------------------------------------------------------------");
        msgArrived(s, mqttMessage);
    }

    protected abstract void msgArrived(String topic, MqttMessage mqttMessage);
}
