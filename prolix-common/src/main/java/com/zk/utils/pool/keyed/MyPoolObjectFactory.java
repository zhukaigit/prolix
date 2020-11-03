package com.zk.utils.pool.keyed;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.atomic.AtomicInteger;

public class MyPoolObjectFactory extends BaseKeyedPooledObjectFactory<String, MyKeyedPoolObject> {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private AtomicInteger id = new AtomicInteger(1);

    @Override
    public MyKeyedPoolObject create (String key) throws Exception {
        return MyKeyedPoolObject.builder().id(id.getAndAdd(1)).key(key).build();
    }

    @Override
    public PooledObject<MyKeyedPoolObject> wrap (MyKeyedPoolObject value) {
        return new DefaultPooledObject<>(value);
    }
}
