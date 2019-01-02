package com.zk;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void test() {
        String[] src = {"a", "b", "c", "d", "e"};
        String join = StringUtils.join(src, ",", 1, 3);
        System.out.println(join);
    }
}
