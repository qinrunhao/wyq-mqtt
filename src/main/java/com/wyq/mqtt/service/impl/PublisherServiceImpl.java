package com.wyq.mqtt.service.impl;

import com.google.gson.Gson;
import com.wyq.mqtt.callback.AbstractPublishCallback;
import com.wyq.mqtt.config.EmqXConfig;
import com.wyq.mqtt.dto.MqttResult;
import com.wyq.mqtt.dto.PublishDto;
import com.wyq.mqtt.mqttv3.MqttV3Executor;
import com.wyq.mqtt.service.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
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
@Slf4j
@Service
public class PublisherServiceImpl implements PublisherService {
    @Autowired
    private MqttV3Executor mqttV3Executor;
    @Autowired
    private EmqXConfig emqXConfig;

    @Override
    public MqttResult publish(PublishDto publishDto) {
        check(publishDto);
        MqttMessage mqttMessage = new MqttMessage(new Gson().toJson(publishDto.getMessage()).getBytes());
        mqttMessage.setQos(publishDto.getQos().getValue());
        mqttMessage.setRetained(publishDto.getRetain());
        try {
            return new MqttResult(mqttV3Executor.getMqttClient().publish(publishDto.getTopic(),mqttMessage, null, new AbstractPublishCallback() {
                @Override
                protected void success(IMqttToken iMqttToken) {
                }
                @Override
                protected void fail(IMqttToken iMqttToken, Throwable throwable) {
                }
            }));
        } catch (MqttException e) {
            return new MqttResult(e);
        }
    }

    @Override
    public int publishBatch(List<PublishDto> publishDtoList) {
        int successNum = 0;
        for (PublishDto publishDto:publishDtoList){
            if (publish(publishDto).isComplete()) {
                successNum ++;
            }
        }
        return successNum;
    }

    @Override
    public void publishAsync(PublishDto publishDto) {
        check(publishDto);
        MqttMessage mqttMessage = new MqttMessage(new Gson().toJson(publishDto.getMessage()).getBytes());
        mqttMessage.setQos(publishDto.getQos().getValue());
        mqttMessage.setRetained(publishDto.getRetain());
        if (publishDto.getCallbackAsync() == null) {
            publishDto.setCallbackAsync(new AbstractPublishCallback() {
                @Override
                protected void success(IMqttToken iMqttToken) {
                }
                @Override
                protected void fail(IMqttToken iMqttToken, Throwable throwable) {
                }
            });
        }
        try {
            IMqttAsyncClient mqttAsyncClient = mqttV3Executor.getMqttAsyncClient();
            boolean flag = true;
            synchronized (this) {
                while(flag) {
                    if (emqXConfig.getConnectOptions().getMaxInflight() > mqttAsyncClient.getPendingDeliveryTokens().length) {
                        flag = false;
                        mqttV3Executor.getMqttAsyncClient()
                                .publish(publishDto.getTopic(), mqttMessage, publishDto.getUserContextAsync(), publishDto.getCallbackAsync());
                    }
                }
            }
        } catch (MqttException e) {
            log.error("MQTT异步发布消息异常：", e);
        }
    }


    @Override
    public void publishBatchAsync(List<PublishDto> publishDtoList) {
        for (PublishDto publishDto:publishDtoList){
            publishAsync(publishDto);
        }
    }

    private void check(PublishDto publishDto){
        if (StringUtils.isEmpty(publishDto.getTopic()) || StringUtils.isEmpty(publishDto.getMessage())) {
            throw new RuntimeException("MQTT发布消息缺少必要参数");
        }
    }

}
