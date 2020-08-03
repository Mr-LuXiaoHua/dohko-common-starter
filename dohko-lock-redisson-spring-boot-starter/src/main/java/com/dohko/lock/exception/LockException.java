package com.dohko.lock.exception;

/**
 * @description: 锁异常
 * @author: luxiaohua
 * @date: 2020-06-28 15:46
 */
public class LockException extends RuntimeException {

    public LockException(String message) {
        super(message);
    }
}
