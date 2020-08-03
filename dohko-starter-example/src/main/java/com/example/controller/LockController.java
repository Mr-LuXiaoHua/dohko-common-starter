package com.example.controller;

import com.example.dto.OrderDTO;
import com.example.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-28 15:03
 */
@RestController
@Slf4j
public class LockController {

    @Autowired
    private LockService lockService;


    @GetMapping("/lock")
    public String test(String orderNo) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderNo(orderNo);
        dto.setUserId("chuliuxiang");
        lockService.testLock1(dto);
        return "hello";

    }

    @GetMapping("/hello")
    public String hello(String orderNo) {


        lockService.testLock2(orderNo);
        return "hello";

    }
}
