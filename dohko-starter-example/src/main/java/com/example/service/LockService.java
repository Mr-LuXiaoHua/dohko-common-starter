package com.example.service;

import com.example.dto.OrderDTO;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 17:47
 */
public interface LockService {


     void testLock1(OrderDTO dto);



     void testLock2(String orderNo);
}
