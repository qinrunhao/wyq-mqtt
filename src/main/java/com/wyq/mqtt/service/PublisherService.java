package com.wyq.mqtt.service;

import com.wyq.mqtt.dto.MqttResult;
import com.wyq.mqtt.dto.PublishDto;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

/**
 * @program: dev
 * @description: 推送消息
 * @author: heyede
 * @create: 2019-08-05
 **/
public interface PublisherService {

    /***
     * 推送消息-同步
     * @param publishDto
     */
    MqttResult publish(PublishDto publishDto);

    /***
     * 批量推送-同步
     * 返回成功条数
     * @param publishDtoList
     */
    int publishBatch(List<PublishDto> publishDtoList);

    /***
     * 推送消息-异步
     * @param publishDto
     * @throws MqttException
     */
    void publishAsync(PublishDto publishDto);

    /***
     * 批量推送-异步
     * @param publishDtoList
     */
    void publishBatchAsync(List<PublishDto> publishDtoList);
}
