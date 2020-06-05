package com.base.service;

import lombok.Data;

/**
 * @ClassName Demo1
 * @Author zc
 * @Date 2020/5/26 18:21
 * @Version 1.0
 **/
@Data
public class Demo1 {
    private String name;
    private Integer age;
    private Integer cm;

    public Demo1(String name, Integer age , Integer cm) {
        this.name = name;
        this.age = age;
        this.cm = cm;
    }

    public Demo1() {
    }
}
