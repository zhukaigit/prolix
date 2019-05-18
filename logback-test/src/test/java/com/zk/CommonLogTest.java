package com.zk;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CommonLogTest {

    @Test
    public void testLogLevel () {
        log.trace("test log filter trance");
        log.debug("test log filter debug");
        log.info("test log filter log");
        log.warn("test log filter warn");
        log.error("test log filter error");
    }
}
