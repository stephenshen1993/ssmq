package com.stephenshen.ssmq.server;

import com.stephenshen.ssmq.model.Result;
import com.stephenshen.ssmq.model.SSMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MQ server.
 * @author stephenshen
 * @date 2024/7/14 16:35:50
 */
@Controller
@RequestMapping("/ssmq")
public class MQServer {

    // send
    @RequestMapping("/send")
    public Result<String> send(@RequestParam("t") String topic, @RequestParam("cid") String consumerId,
                               @RequestBody SSMessage<String> message) {
        return Result.ok("" + MessageQueue.send(topic, consumerId, message));
    }

    // recv
    @RequestMapping("/send")
    public Result<SSMessage<?>> recv(@RequestParam("t") String topic, @RequestParam("cid") String consumerId) {
        return Result.msg(MessageQueue.recv(topic, consumerId));
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
    public Result<String> subscribe(@RequestParam("t") String topic, @RequestParam("cid") String consumerId) {
        MessageQueue.sub(topic, consumerId);
        return Result.ok();
    }

    // unsubscribe
    @RequestMapping("/unsub")
    public Result<String> unsubscribe(@RequestParam("t") String topic, @RequestParam("cid") String consumerId) {
        MessageQueue.unsub(topic, consumerId);
        return Result.ok();
    }
}
