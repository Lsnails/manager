package com.base.service;

import java.math.BigDecimal;

/**
 * @ClassName Test001
 * @Author zc
 * @Date 2020/5/26 11:03
 * @Version 1.0
 **/
public class Test001 {
    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(3000).divide(new BigDecimal(103),2,BigDecimal.ROUND_HALF_UP); //需要发配的奖金
        System.out.println(bigDecimal.doubleValue());
    }
}
