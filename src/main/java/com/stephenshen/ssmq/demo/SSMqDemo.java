package com.stephenshen.ssmq.demo;

import com.alibaba.fastjson.JSON;
import com.stephenshen.ssmq.client.SSBroker;
import com.stephenshen.ssmq.client.SSConsumer;
import com.stephenshen.ssmq.model.Message;
import com.stephenshen.ssmq.client.SSProducer;
import lombok.SneakyThrows;

/**
 * mq demo for order.
 * @author stephenshen
 * @date 2024/7/13 21:54:05
 */
public class SSMqDemo {
    @SneakyThrows
    public static void main(String[] args) {

        long ids = 0;

        String topic = "com.stephenshen.test";
        SSBroker broker = SSBroker.getDefault();

        SSProducer producer = broker.createProducer();
        SSConsumer<?> consumer = broker.createPConsumer(topic);
        consumer.listen(topic, message -> {
            System.out.println(" onMessage => " + message);
        });

        // SSConsumer<?> consumer1 = broker.createPConsumer(topic);

        for (int i = 0; i < 10; i++) {
            Order order = new Order(ids, "item" + ids, 100 * ids);
            producer.send(topic, new Message<>(ids++, JSON.toJSONString(order), null));
        }

//        for (int i = 0; i < 10; i++) {
//            Message<String> message = (Message<String>) consumer1.recv(topic);
//            System.out.println(message);
//            consumer1.ack(topic, message);
//        }

        while (true) {
            char c = (char) System.in.read();
            if (c == 'q' || c == 'e') {
                // consumer1.unsub(topic);
                break;
            }
            if (c == 'p') {
                Order order = new Order(ids, "item" + ids, 100 * ids);
                producer.send(topic, new Message<>(ids++, JSON.toJSONString(order), null));
                System.out.println("produce ok => " + order);
            }
            if (c == 'c') {
//                Message<String> message = (Message<String>) consumer1.recv(topic);
//                System.out.println("consume ok => " + message);
//                consumer1.ack(topic, message);
            }
            if (c == 'a') {
                for (int i = 0; i < 10; i++) {
                    Order order = new Order(ids, "item" + ids, 100 * ids);
                    producer.send(topic, new Message<>(ids++, JSON.toJSONString(order), null));
                }
                System.out.println("produce 10 orders...");
            }
        }
    }
}
