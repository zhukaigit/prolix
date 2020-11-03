package com.zk.utils.pool.keyed;

import com.zk.utils.AssertUtil;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

public class MyKeyedObjectPool {

    private GenericKeyedObjectPool<String, MyKeyedPoolObject> pool;

    public MyKeyedObjectPool (KeyedPooledObjectFactory factory, GenericKeyedObjectPoolConfig config) {
        this.pool = new GenericKeyedObjectPool(factory, config);
    }

    public MyKeyedPoolObject borrowObject (String key) {
        try {
            return pool.borrowObject(key);
        } catch (Exception e) {
            throw new RuntimeException("pool.borrowObject异常，key=" + key, e);
        }
    }

    public void returnObject (String key, MyKeyedPoolObject obj) {
        pool.returnObject(key, obj);
    }


    public static void main (String[] args) {
        MyPoolObjectFactory factory = new MyPoolObjectFactory();
        GenericKeyedObjectPoolConfig config = new MyKeyedPoolConfig();
        MyKeyedObjectPool pool = new MyKeyedObjectPool(factory, config);

        MyKeyedPoolObject key1Obj1 = pool.borrowObject("key1");
        System.out.println("获取到的池对象key1Obj1：" + key1Obj1);
        pool.returnObject("key1", key1Obj1);
        MyKeyedPoolObject key1Obj2 = pool.borrowObject("key1");
        System.out.println("获取到的池对象key1Obj2：" + key1Obj2);
        AssertUtil.assertTrue(key1Obj1 == key1Obj2, "应该要是一样的");

        MyKeyedPoolObject key1Obj3 = pool.borrowObject("key2");
        System.out.println("获取到的池对象key1Obj3：" + key1Obj3);
        AssertUtil.assertTrue(key1Obj1 != key1Obj3, "应该要是不一样的");
    }
}
