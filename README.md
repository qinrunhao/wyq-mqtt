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
## mqtt客户端名称
默认mqtt客户端名称为ip:port，如果要个性化命名，可重新创建bean，如下：
```java
import com.hope.common.mqtt.dto.MqttV3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttInitConfig {

    @Bean
    public MqttV3Client mqttV3Client() {
        //TODO 在此处生成客户端名称
        String clientId = "";
        return new MqttV3Client(clientId);
    }
}
```

