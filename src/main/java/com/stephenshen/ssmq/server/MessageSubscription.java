package com.stephenshen.ssmq.server;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * message subscription.
 * @author stephenshen
 * @date 2024/7/14 16:45:57
 */
@Data
@AllArgsConstructor
public class MessageSubscription {

    private String topic;
    private String consumerId;
    private int offset = -1;
}
