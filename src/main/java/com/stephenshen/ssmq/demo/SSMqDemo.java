package com.stephenshen.ssmq.demo;

import com.stephenshen.ssmq.core.SSBroker;
import com.stephenshen.ssmq.core.SSConsumer;
import com.stephenshen.ssmq.core.SSMessage;
import com.stephenshen.ssmq.core.SSProducer;
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

        String topic = "ss.order";
        SSBroker broker = new SSBroker();
        broker.createTopic(topic);

        SSProducer producer = broker.createProducer();
        SSConsumer<?> consumer = broker.createPConsumer(topic);

        consumer.listen(message -> {
            System.out.println(" onMessage => " + message);
        });

        for (int i = 0; i < 10; i++) {
            Order order = new Order(ids, "item" + ids, 100 * ids);
            producer.send(topic, new SSMessage<>(ids++, order, null));
        }

        for (int i = 0; i < 10; i++) {
            SSMessage<Order> message = (SSMessage<Order>) consumer.poll(1000);
            System.out.println(message);
        }

        while (true) {
            char c = (char) System.in.read();
            if (c == 'q' || c == 'e') {
                break;
            }
            if (c == 'p') {
                Order order = new Order(ids, "item" + ids, 100 * ids);
                producer.send(topic, new SSMessage<>(ids++, order, null));
                System.out.println("send ok => " + order);
            }
            if (c == 'c') {
                SSMessage<Order> message = (SSMessage<Order>) consumer.poll(1000);
                System.out.println("poll ok => " + message);
            }
            if (c == 'a') {
                for (int i = 0; i < 10; i++) {
                    Order order = new Order(ids, "item" + ids, 100 * ids);
                    producer.send(topic, new SSMessage<>(ids++, order, null));
                }
                System.out.println("send 10 orders...");
            }
        }
    }
}
