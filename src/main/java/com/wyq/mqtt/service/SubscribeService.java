package com.wyq.mqtt.service;

import com.wyq.mqtt.callback.AbstractSubscribeCallback;
import com.wyq.mqtt.dto.MqttResult;
import com.wyq.mqtt.dto.SubscribeDto;

import java.util.List;

/**
 * @program: dev
 * @description: 订阅消息
 * @author: heyede
 * @create: 2019-08-05
 **/
public interface SubscribeService {

    /**
     * 订阅消息-同步
     * @param subscribeDto
     */
    MqttResult subscribe(SubscribeDto subscribeDto);

    /***
     * 批量订阅-同步
     * @param subscribeDtoList
     */
    MqttResult subscribeBatch(List<SubscribeDto> subscribeDtoList);

    /**
     * 订阅消息-异步
     */
    void subscribeAsync(SubscribeDto subscribeDto);

    /**
     * 订阅消息-异步
     * @param subscribeDto
     * @param callback 异步回调
     */
    void subscribeAsync(SubscribeDto subscribeDto, AbstractSubscribeCallback callback);

    /**
     * 批量订阅-异步
     * @param subscribeDtoList
     */
    void subscribeBatchAsync(List<SubscribeDto> subscribeDtoList);

    /**
     * 批量订阅-异步
     * @param subscribeDtoList
     * @param callback 批量异步回调
     */
    void subscribeBatchAsync(List<SubscribeDto> subscribeDtoList, AbstractSubscribeCallback callback);

    /**
     * 批量订阅-异步
     * @param subscribeDtoList
     * @param userContext 额外携带的信息，异步执行结束后会传至callback
     * @param callback 批量异步回调
     */
    void subscribeBatchAsync(List<SubscribeDto> subscribeDtoList, Object userContext, AbstractSubscribeCallback callback);

}
