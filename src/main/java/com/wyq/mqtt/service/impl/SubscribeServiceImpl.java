package com.wyq.mqtt.service.impl;

import com.wyq.mqtt.callback.AbstractSubscribeCallback;
import com.wyq.mqtt.dto.MqttResult;
import com.wyq.mqtt.dto.SubscribeDto;
import com.wyq.mqtt.mqttv3.MqttV3Executor;
import com.wyq.mqtt.service.SubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @program: dev
 * @description:
 * @author: heyede
 * @create: 2019-08-05
 **/
@Service
@Slf4j
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private MqttV3Executor mqttV3Executor;

    @Override
    public MqttResult subscribe(SubscribeDto subscribeDto) {
        check(subscribeDto);
        try {
            return new MqttResult(mqttV3Executor.getMqttClient().subscribe(subscribeDto.getTopic(), subscribeDto.getQos().getValue(), null, defaultCallback(), subscribeDto.getMessageListener()));
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public MqttResult subscribeBatch(List<SubscribeDto> subscribeDtoList) {
        String[] topics = new String[subscribeDtoList.size()];
        int[] qos = new int[subscribeDtoList.size()];
        IMqttMessageListener[] iMqttMessageListeners = new IMqttMessageListener[subscribeDtoList.size()];
        buildSubscribeBatch(subscribeDtoList, topics, qos, iMqttMessageListeners);
        try {
            return new MqttResult(mqttV3Executor.getMqttClient().subscribe(topics, qos, null, defaultCallback(), iMqttMessageListeners));
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public void subscribeAsync(SubscribeDto subscribeDto) {
        subscribeAsync(subscribeDto, defaultCallback());
    }

    @Override
    public void subscribeAsync(SubscribeDto subscribeDto, AbstractSubscribeCallback callback) {
        check(subscribeDto);
        try {
            mqttV3Executor.getMqttAsyncClient().subscribe(subscribeDto.getTopic(), subscribeDto.getQos().getValue(), subscribeDto.getUserContext(), callback, subscribeDto.getMessageListener());
        } catch (MqttException e) {
            log.error("MQTT异步订阅消息异常：", e);
        }
    }

    @Override
    public void subscribeBatchAsync(List<SubscribeDto> subscribeDtoList) {
        subscribeBatchAsync(subscribeDtoList, null, defaultCallback());
    }

    @Override
    public void subscribeBatchAsync(List<SubscribeDto> subscribeDtoList, AbstractSubscribeCallback callback) {
        subscribeBatchAsync(subscribeDtoList, null, callback);
    }

    @Override
    public void subscribeBatchAsync(List<SubscribeDto> subscribeDtoList, Object userContext, AbstractSubscribeCallback callback) {
        String[] topics = new String[subscribeDtoList.size()];
        int[] qos = new int[subscribeDtoList.size()];
        IMqttMessageListener[] iMqttMessageListeners = new IMqttMessageListener[subscribeDtoList.size()];
        buildSubscribeBatch(subscribeDtoList, topics, qos, iMqttMessageListeners);
        try {
            mqttV3Executor.getMqttAsyncClient().subscribe(topics, qos, userContext, callback, iMqttMessageListeners);
        } catch (MqttException e) {
            log.error("MQTT异步批量订阅消息异常：", e);
        }
    }

    private void buildSubscribeBatch(List<SubscribeDto> subscribeDtoList, String[] topics, int[] qos, IMqttMessageListener[] iMqttMessageListeners) {
        for (int i =0; i < subscribeDtoList.size(); i++){
            check(subscribeDtoList.get(i));
            topics[i] = subscribeDtoList.get(i).getTopic();
            qos[i] = subscribeDtoList.get(i).getQos().getValue();
            iMqttMessageListeners[i] = subscribeDtoList.get(i).getMessageListener();
        }
    }

    private void check(SubscribeDto subscribeDto) {
        if (StringUtils.isEmpty(subscribeDto.getTopic()) || StringUtils.isEmpty(subscribeDto.getQos()) || subscribeDto.getMessageListener() == null) {
            throw new RuntimeException("MQTT订阅消息缺少必要参数");
        }
    }

    private AbstractSubscribeCallback defaultCallback() {
        return new AbstractSubscribeCallback() {
            @Override
            protected void success(IMqttToken iMqttToken) {
            }
            @Override
            protected void fail(IMqttToken iMqttToken, Throwable throwable) {
            }
        };
    }
}
