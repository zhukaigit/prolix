package com.zk;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;

@Slf4j
public class TimeAndSizeTest {

    @Test
    public void test() {
        int max = 1000;
//        int max = 10;
        for (int i = 0; i < max; i++) {
            log.info("time is " + LocalDateTime.now() + ", i = " + i);

        }
    }
}
