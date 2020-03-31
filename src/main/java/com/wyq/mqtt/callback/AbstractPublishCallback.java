package com.wyq.mqtt.callback;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

@Slf4j
public abstract class AbstractPublishCallback implements IMqttActionListener {

    @Override
    public final void onSuccess(IMqttToken iMqttToken) {
        log.info("---------------------------------MQTT异步发布消息成功---------------------------------");
        log.info("客户端ID：{}", iMqttToken.getClient().getClientId());
        log.info("主题：{}", new Gson().toJson(iMqttToken.getTopics()));
        log.info("上下文对象：{}", iMqttToken.getUserContext() != null ? iMqttToken.getUserContext().toString() : "NULL");
        log.info("callback类：{}", iMqttToken.getActionCallback() != null ? iMqttToken.getActionCallback().getClass().getName() : "NULL");
        log.info("-------------------------------------------------------------------------------------");
        success(iMqttToken);
    }

    @Override
    public final void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        log.info("--------------------------------MQTT异步发布消息失败----------------------------------");
        log.info("客户端ID：{}", iMqttToken.getClient().getClientId());
        log.info("主题：{}", new Gson().toJson(iMqttToken.getTopics()));
        log.info("上下文对象：{}", iMqttToken.getUserContext().toString());
        log.info("callback类：{}", iMqttToken.getActionCallback() != null ? iMqttToken.getActionCallback().getClass().getName() : "NULL");
        log.error("失败原因", throwable);
        log.info("-------------------------------------------------------------------------------------");
        fail(iMqttToken, throwable);
    }

    protected abstract void success(IMqttToken iMqttToken);

    protected abstract void fail(IMqttToken iMqttToken, Throwable throwable);
}
