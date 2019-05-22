package com.zk;

import com.zk.utils.ValidatorUtil;
import org.junit.After;

public class ValidateBaseTest {

    protected ValidatorUtil.ValidatorResult result = null;

    @After
    public void after () {
        System.out.println("校验结果：" + result);
    }
}
