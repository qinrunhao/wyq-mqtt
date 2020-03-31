package com.wyq.mqtt.service.impl;

import com.wyq.mqtt.callback.AbstractUnSubscribeCallback;
import com.wyq.mqtt.dto.MqttResult;
import com.wyq.mqtt.mqttv3.MqttV3Executor;
import com.wyq.mqtt.service.UnsubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: dev
 * @description:
 * @author: heyede
 * @create: 2019-08-06
 **/
@Service
@Slf4j
public class UnsubscribeServiceImpl implements UnsubscribeService {

    @Autowired
    private MqttV3Executor mqttV3Executor;

    @Override
    public MqttResult unsubscribe(String topicFilter) {
        try {
            return new MqttResult(mqttV3Executor.getMqttClient().unsubscribe(topicFilter, null,  defaultCallback()));
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public MqttResult unsubscribeBatch(String[] topicFilters) {
        try {
            return new MqttResult(mqttV3Executor.getMqttClient().unsubscribe(topicFilters, null ,  defaultCallback()));
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public void unsubscribeAsync(String topicFilter) {
        unsubscribeAsync(topicFilter, null,  defaultCallback());
    }

    @Override
    public void unsubscribeAsync(String topicFilter, AbstractUnSubscribeCallback callback) {
        unsubscribeAsync(topicFilter, null, callback);
    }

    @Override
    public void unsubscribeAsync(String topicFilter, Object userContext, AbstractUnSubscribeCallback callback) {
        try {
            mqttV3Executor.getMqttAsyncClient().unsubscribe(topicFilter, userContext, callback);
        } catch (MqttException e) {
            log.error("MQTT异步取消订阅异常：", e);
        }
    }

    @Override
    public void unsubscribeAsyncBatch(String[] topicFilters) {
        unsubscribeAsyncBatch(topicFilters, null, defaultCallback());
    }

    @Override
    public void unsubscribeAsyncBatch(String[] topicFilters, AbstractUnSubscribeCallback callback) {
        unsubscribeAsyncBatch(topicFilters, null, callback);
    }

    @Override
    public void unsubscribeAsyncBatch(String[] topicFilters, Object userContext, AbstractUnSubscribeCallback callback) {
        try {
            mqttV3Executor.getMqttAsyncClient().unsubscribe(topicFilters, userContext, callback);
        } catch (MqttException e) {
            log.error("MQTT异步批量取消订阅异常：", e);
        }
    }

    private AbstractUnSubscribeCallback defaultCallback() {
        return new AbstractUnSubscribeCallback() {
            @Override
            protected void success(IMqttToken iMqttToken) {
            }

            @Override
            protected void fail(IMqttToken iMqttToken, Throwable throwable) {
            }
        };
    }
}
