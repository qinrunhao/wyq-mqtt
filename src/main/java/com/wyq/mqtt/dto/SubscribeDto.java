package com.wyq.mqtt.dto;

import com.wyq.mqtt.callback.AbstractMessageCallback;
import lombok.Data;

/**
 * @program: dev
 * @description: 订阅
 * @author: heyede
 * @create: 2019-08-01
 **/
@Data
public class SubscribeDto {

    /***
     * Qos 1/2
     */
    private QosType qos = QosType.QOS_JUST;

    /***
     * 推送主题
     */
    private String  topic;

    /**
     * 消息监听器，在此处编写消息处理逻辑
     */
    private AbstractMessageCallback messageListener;

    /**
     * 消息监听器中收到的上下文对象
     */
    private Object userContext;
}
