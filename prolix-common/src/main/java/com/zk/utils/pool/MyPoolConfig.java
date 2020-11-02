package com.zk.utils.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 对象池 GenericObjectPool 配置参数详解：https://blog.csdn.net/u012855229/article/details/100866550
 */
public class MyPoolConfig extends GenericObjectPoolConfig {
    /**
     * 最大连接数，默认值 com.zk.utils.pool.MyPoolConfig#maxTotal = 8
     */
    private int maxTotal = 5;
    /**
     * 最大空闲连接数， 默认值 com.zk.utils.pool.MyPoolConfig#maxIdle = 8
     */
    private int maxIdle = 3;
    /**
     * 最小空闲连接数， 默认值 com.zk.utils.pool.MyPoolConfig#minIdle = 0
     */
    private int minIdle = 1;

    /**
     * 当连接池资源用尽后，调用者获取连接时的最大等待时间（单位 ：毫秒）；
     * 默认值 DEFAULT_MAX_WAIT_MILLIS = -1L， 永不超时。
     */
    private long maxWaitMillis = 200;

    /**
     * 当对象池没有空闲对象时，新的获取对象的请求是否阻塞（true 阻塞，maxWaitMillis 才生效； false 连接池没有资源立马抛异常）
     * 默认值 DEFAULT_BLOCK_WHEN_EXHAUSTED = true
     */
    private boolean blockWhenExhausted = true;

    /**
     * 在创建对象时检测对象是否有效(true : 是) , 配置true会降低性能；
     * 默认值 DEFAULT_TEST_ON_CREATE = false。
     */
    private boolean testOnCreate = false;

    /**
     * 在从对象池获取对象时是否检测对象有效(true : 是) , 配置true会降低性能；
     * 默认值 DEFAULT_TEST_ON_BORROW = false
     */
    private boolean testOnBorrow = true;

    /**
     * 在向对象池中归还对象时是否检测对象有效(true : 是) , 配置true会降低性能；
     * 默认值 DEFAULT_TEST_ON_RETURN = false
     */
    private boolean testOnReturn = false;

    /**
     * 在检测空闲对象线程检测到对象不需要移除时，是否检测对象的有效性。建议配置为true，不影响性能，并且保证安全性；
     */
    private boolean testWhileIdle = true;

    /**
     * 连接的最小空闲时间，达到此值后该空闲连接可能会被移除（还需看是否已达最大空闲连接数）；
     * 默认值 DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1000L * 60L * 30L
     */
    private long minEvictableIdleTimeMillis = 1000L * 5;

    /**
     * 连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留minIdle个空闲连接数；
     * 默认值 DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = -1
     */
    private long softMinEvictableIdleTimeMillis = 1000L * 5;

    /**
     * 空闲连接检测的周期（单位毫秒）；如果为负值，表示不运行检测线程；
     * 默认值 DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L
     */
    private long timeBetweenEvictionRunsMillis = 1000L;

    public MyPoolConfig () {
        super();
    }

    @Override
    public int getMaxTotal () {
        return maxTotal;
    }

    @Override
    public void setMaxTotal (int maxTotal) {
        this.maxTotal = maxTotal;
    }

    @Override
    public int getMaxIdle () {
        return maxIdle;
    }

    @Override
    public void setMaxIdle (int maxIdle) {
        this.maxIdle = maxIdle;
    }

    @Override
    public int getMinIdle () {
        return minIdle;
    }

    @Override
    public void setMinIdle (int minIdle) {
        this.minIdle = minIdle;
    }

    @Override
    public long getMaxWaitMillis () {
        return maxWaitMillis;
    }

    @Override
    public void setMaxWaitMillis (long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    @Override
    public long getMinEvictableIdleTimeMillis () {
        return minEvictableIdleTimeMillis;
    }

    @Override
    public void setMinEvictableIdleTimeMillis (long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    @Override
    public long getSoftMinEvictableIdleTimeMillis () {
        return softMinEvictableIdleTimeMillis;
    }

    @Override
    public void setSoftMinEvictableIdleTimeMillis (long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    @Override
    public boolean getTestOnCreate () {
        return testOnCreate;
    }

    public void setTestOnCreate (boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    @Override
    public boolean getTestOnBorrow () {
        return testOnBorrow;
    }

    public void setTestOnBorrow (boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    @Override
    public boolean getTestOnReturn () {
        return testOnReturn;
    }

    public void setTestOnReturn (boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    @Override
    public boolean getTestWhileIdle () {
        return testWhileIdle;
    }

    public void setTestWhileIdle (boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    @Override
    public long getTimeBetweenEvictionRunsMillis () {
        return timeBetweenEvictionRunsMillis;
    }

    @Override
    public void setTimeBetweenEvictionRunsMillis (long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    @Override
    public boolean getBlockWhenExhausted () {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted (boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }
}
