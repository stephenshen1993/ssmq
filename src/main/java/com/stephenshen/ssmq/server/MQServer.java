package com.stephenshen.ssmq.server;

import com.stephenshen.ssmq.model.Result;
import com.stephenshen.ssmq.model.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * MQ server.
 * @author stephenshen
 * @date 2024/7/14 16:35:50
 */
@RestController
@RequestMapping("/ssmq")
public class MQServer {

    // send
    @RequestMapping("/send")
    public Result<String> send(@RequestParam("t") String topic,
                               @RequestBody Message<String> message) {
        return Result.ok("" + MessageQueue.send(topic, message));
    }

    // recv
    @RequestMapping("/recv")
    public Result<Message<?>> recv(@RequestParam("t") String topic, @RequestParam("cid") String consumerId) {
        return Result.msg(MessageQueue.recv(topic, consumerId));
    }

    @RequestMapping("/batch")
    public Result<List<Message<?>>> batch(@RequestParam("t") String topic,
                                         @RequestParam("cid") String consumerId,
                                         @RequestParam(name = "size", required = false, defaultValue = "1000") int size) {
        return Result.msg(MessageQueue.batch(topic, consumerId, size));
    }

    // ack
    @RequestMapping("/ack")
    public Result<String> ack(@RequestParam("t") String topic,
                              @RequestParam("cid") String consumerId,
                              @RequestParam("offset") Integer offset) {
        return Result.ok("" + MessageQueue.ack(topic, consumerId, offset));
    }

    // 1.subscribe
    @RequestMapping("/sub")
    public Result<String> sub(@RequestParam("t") String topic, @RequestParam("cid") String consumerId) {
        MessageQueue.sub(topic, consumerId);
        return Result.ok();
    }

    // unsubscribe
    @RequestMapping("/unsub")
    public Result<String> unsub(@RequestParam("t") String topic, @RequestParam("cid") String consumerId) {
        MessageQueue.unsub(topic, consumerId);
        return Result.ok();
    }
}
