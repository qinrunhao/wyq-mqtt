package com.wyq.mqtt.callback;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

/**
 * 自定义连接回调，不使用异步客户端MqttAsyncClient自带的连接回调
 * @description:
 * @author: heyede
 * @create: 2019-08-05
 **/
@Slf4j
public class ConnectCallback implements MqttCallbackExtended {
    private int reconnectionSeconds;
    private IMqttAsyncClient mqttClient;
    private String serverURI;
    private static final Object LOCK = new Object();

    public ConnectCallback(int reconnectionSeconds, IMqttAsyncClient mqttClient) {
        this.reconnectionSeconds = reconnectionSeconds;
        this.mqttClient = mqttClient;
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI){
        this.serverURI = serverURI;
        log.info("==============================================================");
        log.info("MQTT连接成功，客户端ID：{}，服务器URI：{}",mqttClient.getClientId(), serverURI);
        log.info("==============================================================");
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.error("MQTT连接断开，开始重连", cause);
        int number = 0;
        synchronized (LOCK) {
            while (true) {
                try {
                    Thread.sleep(reconnectionSeconds * 1000);
                    if(mqttClient != null && mqttClient.isConnected()){
                        number = 0;
                        break;
                    }
                    if (mqttClient == null) {
                        log.error("MQTT客户端为null");
                        break;
                    }
                    if (mqttClient != null && !mqttClient.isConnected()) {
                        log.info("MQTT开始重连，客户端ID：{}，每隔{}秒重连一次，当前重连次数：{}",mqttClient.getClientId(), reconnectionSeconds,++number);
                        mqttClient.reconnect();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MqttException e) {
                    //已连接
                    if (e.getReasonCode() == 32100) {
                        log.info("MQTT已连接，客户端ID：{}，不需要再重新连接，退出连接循环", mqttClient.getClientId());
                        break;
                    }
                    //正在连接
                    if (e.getReasonCode() == 32110) {
                        log.info("MQTT正在连接，客户端ID：{}，不需要再重新连接，退出连接循环", mqttClient.getClientId());
                    }
                    //正在断开连接
                    if (e.getReasonCode() == 32102) {
                        log.info("MQTT正在断开连接，客户端ID：{}，不需要再重新连接，退出连接循环", mqttClient.getClientId());
                    }
                    //连接已关闭
                    if (e.getReasonCode() == 32111) {
                        log.info("MQTT连接已关闭，客户端ID：{}，不需要再重新连接，退出连接循环", mqttClient.getClientId());
                    }
                    log.error("MQTT重连异常，客户端ID：{}，",mqttClient.getClientId(), e);
                }
            }
        }
    }

    /**
     * 消息到达-异步的订阅和发布才会调用。当没有IMqttMessageListener实现类使用时才会被调用，否则会被IMqttMessageListener实现类的messageArrived覆盖
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("MQTT消息已到达。客户端ID：{}，消息ID：{}，消息内容：{}，topic：{}",mqttClient.getClientId(), message.getId(), new String(message.getPayload()), topic);
    }

    /**
     * 当消息的传递已完成，且所有确认已收到时调用。
     * 对于QoS 0消息，一旦消息被提交到网络进行交付，就会调用它。
     * 对于QoS 1，在接收PUBACK时调用它，
     * 对于QoS 2，在接收PUBCOMP时调用它。令牌将与消息发布时返回的令牌相同。
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            log.info("MQTT消息传递完成。" +
                            "消息内容（可能为空，QOS为0时非NULL，为1、2时为NULL）：{}，" +
                            "topics：{}，" +
                            "上下文对象：{}，" +
                            "回调类：{}",
                    token.getMessage() != null ? new String(token.getMessage().getPayload()) : "NULL",
                    new Gson().toJson(token.getTopics()),
                    token.getUserContext() != null ? token.getUserContext().toString() : "NULL",
                    token.getActionCallback() != null ? token.getActionCallback().getClass().getName() : "NULL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
