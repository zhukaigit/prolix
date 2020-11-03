package com.zk.utils.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class MyPoolObjectFactory2 extends BasePooledObjectFactory<MyPoolObject> {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private AtomicInteger id = new AtomicInteger(1);

    @Override
    public MyPoolObject create () {
        MyPoolObject object = MyPoolObject.builder().remainBattery(100).id(id.getAndAdd(1))
                .build();
        System.out.println("【创建】的池对象=================》》》》》" + object + "，当前时间时间：" + getNow());

        return object;
    }

    @Override
    public PooledObject<MyPoolObject> wrap (MyPoolObject obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public boolean validateObject (PooledObject<MyPoolObject> p) {
        boolean b = super.validateObject(p) && p.getObject().getRemainBattery() > 0;
        if (!b) {
            System.out.println("对象不可用了，object={}" + p.getObject());
        }
        return b;
    }

    @Override
    public void activateObject (PooledObject<MyPoolObject> p) throws Exception {
        System.out.println("activateObject");
        super.activateObject(p);
    }

    @Override
    public void destroyObject (PooledObject<MyPoolObject> p) throws Exception {
        MyPoolObject object = p.getObject();
        System.out.println("【销毁】的池对象=================》》》》》" + object + "，当前时间时间：" + getNow());
    }

    @Override
    public void passivateObject (PooledObject<MyPoolObject> p) throws Exception {
        System.out.println("passivateObject");
        super.passivateObject(p);
    }

    private String getNow () {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern
                (YYYY_MM_DD_HH_MM_SS));
    }

}
