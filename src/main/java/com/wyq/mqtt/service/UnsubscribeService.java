package com.wyq.mqtt.service;

import com.wyq.mqtt.callback.AbstractUnSubscribeCallback;
import com.wyq.mqtt.dto.MqttResult;

/**
 * @program: dev
 * @description: 退订消息
 * @author: heyede
 * @create: 2019-08-06
 **/
public interface UnsubscribeService {

    /***
     * 取消订阅-同步
     * @param topicFilter 主题
     */
    MqttResult unsubscribe(String topicFilter);

    /***
     * 批量取消订阅-同步
     * @param topicFilters
     */
    MqttResult unsubscribeBatch(String[] topicFilters);

    /**
     * 取消订阅-异步
     * @param topicFilter
     */
    void unsubscribeAsync(String topicFilter);

    /**
     * 取消订阅-异步
     * @param topicFilter
     * @param callback
     */
    void unsubscribeAsync(String topicFilter, AbstractUnSubscribeCallback callback);

    /**
     * 取消订阅-异步
     * @param topicFilter
     * @param userContext
     * @param callback
     */
    void unsubscribeAsync(String topicFilter, Object userContext, AbstractUnSubscribeCallback callback);

    /**
     * 批量取消订阅-异步
     * @param topicFilters
     */
    void unsubscribeAsyncBatch(String[] topicFilters);

    /**
     * 批量取消订阅-异步
     * @param topicFilters
     * @param callback
     */
    void unsubscribeAsyncBatch(String[] topicFilters, AbstractUnSubscribeCallback callback);

    /**
     * 批量取消订阅-异步
     * @param topicFilters
     * @param userContext
     * @param callback
     */
    void unsubscribeAsyncBatch(String[] topicFilters, Object userContext, AbstractUnSubscribeCallback callback);
}
