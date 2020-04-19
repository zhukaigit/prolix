package com.zk.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

/**
 * Created by zhukai on 2020/4/19
 */
public class RandomUtilTest {

    @Test
    public void test_getRandomScope() {
        int min = 10;
        int max = 30;
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < 40000; i++) {
            int random = RandomUtil.getRandomScope(min, max);
            Assert.assertTrue(random >= min);
            Assert.assertTrue(random < max);
            set.add(random);
        }
        System.out.println(set);
    }
}
