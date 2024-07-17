package com.stephenshen.ssmq.server;

import com.stephenshen.ssmq.model.SSMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * queues.
 *
 * @author stephenshen
 * @date 2024/7/14 16:39:12
 */
public class MessageQueue {

    private static final Map<String, MessageQueue> queues = new HashMap<>();
    private static final String TEST_TOPIC = "com.stephenshen.test";

    static {
        queues.put(TEST_TOPIC, new MessageQueue(TEST_TOPIC));
    }

    private Map<String, MessageSubscription> subscriptions = new HashMap<>();
    private String topic;
    private SSMessage<?>[] queue = new SSMessage[1024 * 10];
    private int index = 0;

    public MessageQueue(String topic) {
        this.topic = topic;
    }

    public int send(SSMessage<?> message) {
        if (index >= queue.length) return -1;
        queue[index++] = message;
        return index;
    }

    public SSMessage<?> recv(int ind) {
        if (ind > index) return null;
        return queue[ind];
    }

    public void subscribe(String consumerId) {
        subscriptions.putIfAbsent(consumerId, new MessageSubscription(topic, consumerId, -1));
    }

    public void unsubscribe(String consumerId) {
        subscriptions.remove(consumerId);
    }

    public static void sub(String topic, String consumerId) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) throw new RuntimeException("topic not found");
        messageQueue.subscribe(consumerId);
    }

    public static void unsub(String topic, String consumerId) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) return;
        messageQueue.unsubscribe(consumerId);
    }

    public static int send(String topic, String consumerId, SSMessage<String> message) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) throw new RuntimeException("topic not found");
        return messageQueue.send(message);
    }

    // 使用此方法，需要手工调用ack，更新订阅关系里的offset
    public static SSMessage<?> recv(String topic, String consumerId) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) {
            throw new RuntimeException("topic not found");
        }
        if (!messageQueue.subscriptions.containsKey(consumerId)) {
            throw new RuntimeException("subscriptions not found for topic/consumerId = " + topic + "/" + consumerId);
        }
        int idx = messageQueue.subscriptions.get(consumerId).getOffset();
        return messageQueue.recv(idx);
    }

    public static int ack(String topic, String consumerId, int offset) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) {
            throw new RuntimeException("topic not found");
        }
        if (!messageQueue.subscriptions.containsKey(consumerId)) {
            throw new RuntimeException("subscriptions not found for topic/consumerId = " + topic + "/" + consumerId);
        }
        MessageSubscription subscription = messageQueue.subscriptions.get(consumerId);
        if (offset <= subscription.getOffset() || offset > messageQueue.index) {
            return -1;
        }
        subscription.setOffset(offset);
        return offset;
    }
}
