package com.stephenshen.ssmq.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author stephenshen
 * @date 2024/7/13 21:54:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private long id;
    private String item;
    private double price;
}
