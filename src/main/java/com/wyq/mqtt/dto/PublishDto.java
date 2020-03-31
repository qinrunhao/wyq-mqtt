package com.wyq.mqtt.dto;

import com.wyq.mqtt.callback.AbstractPublishCallback;
import lombok.Data;

/**
 * @program: dev
 * @description: 推送
 * @author: heyede
 * @create: 2019-08-01
 **/
@Data
public class PublishDto {

    /***
     * 推送的消息报文
     */
    private Object message;

    /***
     * 是否保留消息
     * 若为true，则消息会保留在服务器上，即使qos=2，客户端也已经消费，
     * 但当主题被重新订阅时，
     * 该消息还是会被再次消费，被消费的消息为最后一次发送的且retain=true的消息
     * 默认为false，必要情况下再设置为true，请谨慎使用
     */
    private Boolean retain = false;

    /***
     * Qos 1/2
     */
    private QosType qos = QosType.QOS_JUST;

    /***
     * 推送主题
     */
    private String  topic;

    /**
     * 额外携带的信息，异步执行结束后会传至callback
     */
    private Object userContextAsync;

    /**
     * 异步推送操作完成时，将通知此接口的实现者
     */
    private AbstractPublishCallback callbackAsync;
}
