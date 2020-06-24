package com.dohko.log.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @description:
 * @author: luxiaohua
 * @date: 2020-06-24 15:24
 */
public class RandomUtils {


    private static final int ORIGIN = 10000000;
    private static final int BOUND = 99999999;

    /**
     * 随机整数
     * @return
     */
    public static int rand() {
        return ThreadLocalRandom.current().nextInt(ORIGIN, BOUND);
    }


    public static String randStr() {
        return String.valueOf(rand());
    }


}
