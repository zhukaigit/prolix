package com.zk.utils.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


public class MyPoolObjectPool {
    private GenericObjectPool<MyPoolObject> pool;

    public MyPoolObjectPool (PooledObjectFactory factory, GenericObjectPoolConfig config) {
        pool = new GenericObjectPool<>(factory, config);
    }

    /**
     * 1 从LinkedBlockingDeque中pollFirst
     * 2 若为空，检查对象池对象是否达到上限，根据等待策略执行（如在指定时间段获取不到则报错；或者直接报错；或者一直阻塞等待），
     *   若否，则调用PooledObjectFactory的makeObject去创建一个对象
     * 3 得到对象之后，对对象进行初始化和一些配置的计数处理（即activateObject），同时将对象加入到ConcurrentHashMap。
     *
     * @return
     */
    public MyPoolObject borrowObject () {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("MyPoolObjectPool.borrowObject异常", e);
        }
    }

    /**
     * 1 根据obj从ConcurrentHashMap拿到其对应的PooledObject p
     * 2 判空；将p状态置为RETURN
     * 3 若getTestOnReturn参数为true，进行validateObject，若验证失败，销毁
     * 4 对p进行passivateObject，与初始化相反
     * 5 更新p状态为IDLE
     * 6 归还Pool：Pool的idle实例达到上限或者Pool已经关闭，销毁之，否则将p加入到LinkedBlockingDeque中。
     *
     * @param object
     */
    public void returnObject (MyPoolObject object) {
        if (object != null) {
            pool.returnObject(object);
        }
    }

    public int getNumActive () {
        return pool.getNumActive();
    }

    public int getNumIdle () {
        return pool.getNumIdle();
    }
}
