# mqtt
mqtt封装
## 快速上手
* 发布消息，直接注入接口PublisherService，调用相关成员方法。
* 订阅消息，直接注入接口SubscribeService，调用相关成员方法。
* 取消订阅，直接注入接口UnsubscribeService，调用相关成员方法。
## application.yml配置
```
mqtt:
  config:
    host: tcp://172.16.30.56:1883
    option:
          user-name: username
          password: password
          max-inflight: 1000
```
