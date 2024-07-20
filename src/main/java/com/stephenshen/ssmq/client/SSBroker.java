package com.stephenshen.ssmq.client;

import cn.kimmking.utils.HttpUtils;
import cn.kimmking.utils.ThreadUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.stephenshen.ssmq.model.Message;
import com.stephenshen.ssmq.model.Result;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * broker for topics.
 * @author stephenshen
 * @date 2024/7/13 21:40:47
 */
public class SSBroker {

    @Getter
    public static SSBroker Default = new SSBroker();

    public static String brokerUrl = "http://localhost:8765/ssmq";

    static {
        init();
    }

    private static void init() {
        ThreadUtils.getDefault().init(1);
        ThreadUtils.getDefault().schedule(() -> {
            SSBroker broker = getDefault();
            broker.getConsumers().forEach((topic, consumers) -> {
                consumers.forEach(consumer -> {
                    Message<?> recv = consumer.recv(topic);
                    if (recv == null) return;
                    try {
                        consumer.getListener().onMessage(recv);
                        consumer.ack(topic, recv);
                    } catch (Exception ex) {
                        // TODO
                    }
                });
            });
        }, 100, 100);
    }

    public SSProducer createProducer() {
        return new SSProducer(this);
    }

    public SSConsumer<?> createPConsumer(String topic) {
        SSConsumer<?> consumer = new SSConsumer<>(this);
        consumer.sub(topic);
        return consumer;
    }

    public boolean send(String topic, Message message) {
        System.out.println(" ===> send topic/message: " + topic + "/" + message);
        System.out.println(JSON.toJSONString(message));
        Result<String> result = HttpUtils.httpPost(JSON.toJSONString(message),
                brokerUrl + "/send?t=" + topic, new TypeReference<>() {});
        System.out.println(" ===> send result: " + result);
        return result.getCode() == 1;
    }

    public void sub(String topic, String consumerId) {
        System.out.println(" ===> sub topic/consumerId: " + topic + "/" + consumerId);
        Result<String> result = HttpUtils.httpGet(brokerUrl + "/sub?t=" + topic + "&cid=" + consumerId,
                new TypeReference<>() {});
        System.out.println(" ===> sub result: " + result);
    }

    public void unsub(String topic, String consumerId) {
        System.out.println(" ===> unsub topic/consumerId: " + topic + "/" + consumerId);
        Result<String> result = HttpUtils.httpGet(brokerUrl + "/unsub?t=" + topic + "&cid=" + consumerId,
                new TypeReference<>() {});
        System.out.println(" ===> unsub result: " + result);
    }

    public <T> Message<T> recv(String topic, String consumerId) {
        System.out.println(" ===> recv topic/consumerId: " + topic + "/" + consumerId);
        Result<Message<String>> result = HttpUtils.httpGet(brokerUrl + "/recv?t=" + topic + "&cid=" + consumerId,
                new TypeReference<>() {});
        System.out.println(" ===> recv result: " + result);
        return (Message<T>)result.getData();
    }

    public boolean ack(String topic, String consumerId, int offset) {
        System.out.println(" ===> ack topic/consumerId/offset: " + topic + "/" + consumerId + "/" + offset);
        Result<String> result = HttpUtils.httpGet(brokerUrl + "/ack?t=" + topic + "&cid=" + consumerId + "&offset=" + offset,
                new TypeReference<>() {});
        System.out.println(" ===> ack result: " + result);
        return result.getCode() == 1;
    }

    @Getter
    private MultiValueMap<String, SSConsumer<?>> consumers = new LinkedMultiValueMap();
    public void addConsumer(String topic, SSConsumer<?> consumer) {
        consumers.add(topic, consumer);
    }
}
