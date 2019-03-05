package com.zk.springEvent;

import com.zk.event.MyEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringEventTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void test() {
        context.publishEvent(new MyEvent("source", "朱凯", 28));
    }
}

