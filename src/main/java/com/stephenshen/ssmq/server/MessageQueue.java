package com.stephenshen.ssmq.server;

import com.stephenshen.ssmq.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Message<?>[] queue = new Message[1024 * 10];
    private int index = 0;

    public MessageQueue(String topic) {
        this.topic = topic;
    }

    public static List<Message<?>> batch(String topic, String consumerId, int size) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) {
            throw new RuntimeException("topic not found");
        }
        if (!messageQueue.subscriptions.containsKey(consumerId)) {
            throw new RuntimeException("subscriptions not found for topic/consumerId = " + topic + "/" + consumerId);
        }
        int ind = messageQueue.subscriptions.get(consumerId).getOffset();
        int offset = ind + 1;
        List<Message<?>> result = new ArrayList<>();
        Message<?> recv = null;
        while (result.size() < size) {
            recv = messageQueue.recv(offset++);
            if (recv == null) break;
            result.add(recv);
        }

        System.out.println(" ===>> recv: topic/cid/size = " + topic + "/" + consumerId + "/" + result.size());
        System.out.println(" ===>> last message: " + recv);
        return result;
    }

    public int send(Message<?> message) {
        if (index >= queue.length) return -1;
        message.getHeaders().put("X-offset", String.valueOf(index));
        queue[index++] = message;
        return index;
    }

    public Message<?> recv(int ind) {
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
        System.out.println(" ===>> sub: topic/consumerId " + topic + "/" + consumerId);
        if (messageQueue == null) throw new RuntimeException("topic not found");
        messageQueue.subscribe(consumerId);
    }

    public static void unsub(String topic, String consumerId) {
        MessageQueue messageQueue = queues.get(topic);
        System.out.println(" ===>> unsub: topic/consumerId " + topic + "/" + consumerId);
        if (messageQueue == null) return;
        messageQueue.unsubscribe(consumerId);
    }

    public static int send(String topic, Message<String> message) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) throw new RuntimeException("topic not found");
        System.out.println(" ===>> send: topic/message " + topic + "/" + message);
        return messageQueue.send(message);
    }

    public static Message<?> recv(String topic, String consumerId, int ind) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) {
            throw new RuntimeException("topic not found");
        }
        if (!messageQueue.subscriptions.containsKey(consumerId)) {
            throw new RuntimeException("subscriptions not found for topic/consumerId = " + topic + "/" + consumerId);
        }
        return messageQueue.recv(ind);
    }

    // 使用此方法，需要手工调用ack，更新订阅关系里的offset
    public static Message<?> recv(String topic, String consumerId) {
        MessageQueue messageQueue = queues.get(topic);
        if (messageQueue == null) {
            throw new RuntimeException("topic not found");
        }
        if (!messageQueue.subscriptions.containsKey(consumerId)) {
            throw new RuntimeException("subscriptions not found for topic/consumerId = " + topic + "/" + consumerId);
        }
        int ind = messageQueue.subscriptions.get(consumerId).getOffset();
        Message<?> recv = messageQueue.recv(ind + 1);
        System.out.println(" ===>> recv: topic/cid/ind = " + topic + "/" + consumerId + "/" + ind);
        System.out.println(" ===>> message: " + recv);
        return recv;
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
        System.out.println(" ===>> ack: topic/cid/offset = " + topic + "/" + consumerId + "/" + offset);
        subscription.setOffset(offset);
        return offset;
    }
}
