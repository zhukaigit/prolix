package com.zk.utils.pool;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 对象池 GenericObjectPool 配置参数详解：https://blog.csdn.net/u012855229/article/details/100866550
 * <p>
 * 测试后总结点：
 * 【一】 returnObject时，若此时池中的空闲对象大于maxIdle，那么该返回的池对象会被直接销毁 <p/>
 * 【二】 若timeBetweenEvictionRunsMillis大于0，且minEvictableIdleTimeMillis、softMinEvictableIdleTimeMillis至少有一个不为负数 <p/>
 * ，那么空余空余对象最终会降到minIdle个。 <p/>
 * 【三】 若minEvictableIdleTimeMillis、softMinEvictableIdleTimeMillis都为负数，或者timeBetweenEvictionRunsMillis为负数，那么当 <p/>
 * 池中连接数量大于maxIdle时，最终池中空闲保持maxIdle个 <p/>
 * 【四】 timeBetweenEvictionRunsMillis和minEvictableIdleTimeMillis都大于0，若池对象空闲时间达到minEvictableIdleTimeMillis时， <p/>
 * 可能会被销毁（不一定是时间一达到就立即销毁，因为要检测线程执行检察时，判断满足条件才会去销毁），不管当前池对象的空闲对象是否小于 <p/>
 * minIdle，所以这个设置可以保证空闲的池对象存活时间一般不会超过minEvictableIdleTimeMillis <p/>
 * 【五】softMinEvictableIdleTimeMillis和minEvictableIdleTimeMillis比较（前提是生效，即timeBetweenEvictionRunsMillis大于0）： <p/>
 * 相同点：都会让空闲池对象数量降低到minIdle <p/>
 * 不同点：minEvictableIdleTimeMillis：只要时间达到minEvictableIdleTimeMillis，池对象就会销毁（若销毁后空闲池对象数量小于minIdle，会在create一个）； <p/>
 * softMinEvictableIdleTimeMillis：池对象空闲时间达到softMinEvictableIdleTimeMillis，且空闲池对象数量大于minIdle，才会销毁该池对象 <p/>
 */
@Data
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
     * 在空闲线程检测时，若池中对象的空闲时间大于minEvictableIdleTimeMillis，该池对象会被销毁，即使当前池中对象数量是否小于minIdle <p />
     * 注意：若此时池中的对象数量小于minIdle，会创建新的池对象到池中，直到数量达到minIdle <p />
     * 默认值 DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1000L * 60L * 30L
     */
    private long minEvictableIdleTimeMillis = 1000L * 5;

    /**
     * 在空闲线程检测时，若池对象空闲时间大于softMinEvictableIdleTimeMillis且池中空闲对象数量大于minIdle，该池对象销毁
     * 默认值 DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = -1
     */
    private long softMinEvictableIdleTimeMillis = 1000L * 5;

    /**
     * 空闲连接检测的周期（单位毫秒）；如果为负值，表示不运行检测线程；
     * 默认值 DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L
     * <p>
     * minEvictableIdleTimeMillis与softMinEvictableIdleTimeMillis区别：<p />
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

    public static MyPoolConfigBuilder builder () {
        return new MyPoolConfigBuilder();
    }

    public static class MyPoolConfigBuilder {

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


        public int getMaxTotal () {
            return maxTotal;
        }


        public MyPoolConfigBuilder maxTotal (int maxTotal) {
            this.maxTotal = maxTotal;
            return this;
        }


        public int getMaxIdle () {
            return maxIdle;
        }


        public MyPoolConfigBuilder maxIdle (int maxIdle) {
            this.maxIdle = maxIdle;
            return this;
        }


        public int getMinIdle () {
            return minIdle;
        }


        public MyPoolConfigBuilder minIdle (int minIdle) {
            this.minIdle = minIdle;
            return this;
        }


        public long getMaxWaitMillis () {
            return maxWaitMillis;
        }


        public MyPoolConfigBuilder maxWaitMillis (long maxWaitMillis) {
            this.maxWaitMillis = maxWaitMillis;
            return this;
        }


        public long getMinEvictableIdleTimeMillis () {
            return minEvictableIdleTimeMillis;
        }


        public MyPoolConfigBuilder minEvictableIdleTimeMillis (long minEvictableIdleTimeMillis) {
            this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
            return this;
        }


        public long getSoftMinEvictableIdleTimeMillis () {
            return softMinEvictableIdleTimeMillis;
        }


        public MyPoolConfigBuilder softMinEvictableIdleTimeMillis (long softMinEvictableIdleTimeMillis) {
            this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
            return this;
        }


        public boolean getTestOnCreate () {
            return testOnCreate;
        }

        public MyPoolConfigBuilder testOnCreate (boolean testOnCreate) {
            this.testOnCreate = testOnCreate;
            return this;
        }


        public boolean getTestOnBorrow () {
            return testOnBorrow;
        }

        public MyPoolConfigBuilder testOnBorrow (boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
            return this;
        }


        public boolean getTestOnReturn () {
            return testOnReturn;
        }

        public MyPoolConfigBuilder testOnReturn (boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
            return this;
        }


        public boolean getTestWhileIdle () {
            return testWhileIdle;
        }

        public MyPoolConfigBuilder testWhileIdle (boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
            return this;
        }


        public long getTimeBetweenEvictionRunsMillis () {
            return timeBetweenEvictionRunsMillis;
        }


        public MyPoolConfigBuilder timeBetweenEvictionRunsMillis (long timeBetweenEvictionRunsMillis) {
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
            return this;
        }


        public boolean getBlockWhenExhausted () {
            return blockWhenExhausted;
        }

        public MyPoolConfigBuilder blockWhenExhausted (boolean blockWhenExhausted) {
            this.blockWhenExhausted = blockWhenExhausted;
            return this;
        }

        public MyPoolConfig build () {
            MyPoolConfig config = new MyPoolConfig();
            config.setMaxTotal(maxTotal);
            config.setMaxIdle(maxIdle);
            config.setMinIdle(minIdle);
            config.setMaxWaitMillis(maxWaitMillis);
            config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            config.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
            config.setTestOnCreate(testOnCreate);
            config.setTestOnBorrow(testOnBorrow);
            config.setTestOnReturn(testOnReturn);
            config.setTestWhileIdle(testWhileIdle);
            config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            config.setBlockWhenExhausted(blockWhenExhausted);
            return config;
        }

    }


}
