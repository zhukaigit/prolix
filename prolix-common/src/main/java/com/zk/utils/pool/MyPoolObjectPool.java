package com.zk.utils.pool;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


public class MyPoolObjectPool {
    private GenericObjectPool<MyPoolObject> pool;

    public MyPoolObjectPool (PooledObjectFactory factory) {
        GenericObjectPoolConfig config = new MyPoolConfig();
        pool = new GenericObjectPool<>(factory, config);
    }

    public MyPoolObject borrowObject () {
        try {
            System.out.println();
            System.out.println(StringUtils.center("执行MyPoolObjectPool.borrowObject - 开始", 50, "=="));
            MyPoolObject myPoolObject = pool.borrowObject();
            System.out.println("对象池的对象object={}" + myPoolObject);
            System.out.println(StringUtils.center("执行MyPoolObjectPool.borrowObject - 结束", 50, "=="));
            System.out.println();
            return myPoolObject;
        } catch (Exception e) {
            throw new RuntimeException("MyPoolObjectPool.borrowObject异常", e);
        }
    }


    public void returnObject(MyPoolObject object) {
        System.out.println();
        System.out.println(StringUtils.center("执行MyPoolObjectPool.returnObject - 开始", 50, "=="));
        if (object != null) {
            pool.returnObject(object);
        }
        System.out.println(StringUtils.center("执行MyPoolObjectPool.returnObject - 结束", 50, "=="));
        System.out.println();
    }
}
