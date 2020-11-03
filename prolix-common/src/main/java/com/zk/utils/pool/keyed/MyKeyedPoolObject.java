package com.zk.utils.pool.keyed;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 需要被对象池管理的对象,
 * 模拟一个充电宝被复用的情况
 */
@Data
@Builder
@ToString
public class MyKeyedPoolObject {

    private String key;
    private int id;

}
