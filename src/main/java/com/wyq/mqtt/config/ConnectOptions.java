package com.wyq.mqtt.config;

import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

@Setter
@ConfigurationProperties(prefix = "mqtt.config.options")
public class ConnectOptions implements Serializable {
    /***
     * emtqq 用户名
     **/
    private String userName;
    /***
     * emtqq 密码
     **/
    private String password;
    /***
     * 连接超时时间(单位秒)
     **/
    private int connectionTimeout = 30;
    /***
     * 是否清除session会话信息，当客户端disconnect 时 cleanSession 为true会清除之前的所有订阅信息。
     */
    private Boolean cleanSession = false;
    /***
     * 终止executor服务时等待的时间(以秒为单位)。
     */
    private int executorServiceTimeout = 1;
    /***
     *   每隔多长时间（秒）发送一个心跳包（默认60秒）
     **/
    private int keepaliveInterval = 60;
    /***
     * 消息最大堵塞数 默认10
     */
    private int maxInflight = 10;
    /***
     *  重连接间隔毫秒数，默认为128000ms
     */
    private int maxReconnectDelay = 128000;

    @Setter
    private SSL ssl;

    @Setter
    private Will will;

    /**
     * 客户端可以连接的MQTT服务器，用英文逗号间隔
     */
    @Setter
    private String serverURIs;

    @Setter
    @ConfigurationProperties(prefix = "mqtt.config.ssl")
    protected class SSL {

        private Boolean enable = false;
        /***
         * 客户端证书地址
         */
        private String clientKeyStore = "";

        /***
         * 客户端秘钥
         */
        private String clientKeyStorePassword = "";

        protected SocketFactory getSocketFactory() {
            return enable ? getSSLSocketFactory() : SocketFactory.getDefault();
        }

        private SSLSocketFactory getSSLSocketFactory(){
            SSLContext sslContext = null;
            try {
                InputStream keyStoreStream = new FileInputStream(new URL(ssl.clientKeyStore).getPath());
                sslContext = SSLContext.getInstance("TLS");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory
                        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(keyStoreStream, ssl.clientKeyStorePassword.toCharArray());
                trustManagerFactory.init(keyStore);
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return sslContext != null ? sslContext.getSocketFactory() : null;
        }
    }

    @Setter
    @ConfigurationProperties(prefix = "mqtt.config.will")
    protected class Will {
        /***
         * 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
         */
        private String topic = "";

        /***
         * 设置“遗嘱”消息的内容，默认是长度为零的消息。
         */
        private String message = "";

        /***
         * 设置“遗嘱”消息的QoS，默认为2
         */
        private int qos = 2;
        /***
         * 若想要在发布“遗嘱”消息时拥有retain选项，则为true。
         */
        private Boolean retained = false;
    }

    public MqttConnectOptions getConnectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        if (!StringUtils.isEmpty(serverURIs)) {
            options.setServerURIs(serverURIs.split(","));
        }
        if (!StringUtils.isEmpty(this.userName)) {
            options.setUserName(this.userName);
        }
        if (!StringUtils.isEmpty(this.password)) {
            options.setPassword(this.password.toCharArray());
        }
        options.setCleanSession(this.cleanSession);
        options.setConnectionTimeout(this.connectionTimeout);
        options.setExecutorServiceTimeout(this.executorServiceTimeout);
        options.setKeepAliveInterval(this.keepaliveInterval);
        options.setMaxInflight(this.maxInflight);
        options.setMaxReconnectDelay(this.maxReconnectDelay);
        if (this.ssl != null) {
            options.setSocketFactory(this.ssl.getSocketFactory());
        }
        if (this.will != null) {
            options.setWill(this.will.topic, this.will.message.getBytes(), this.will.qos, this.will.retained);
        }
        return options;
    }

}
