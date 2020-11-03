package com.zk.utils.pool;

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
public class MyPoolObject {

    private int remainBattery;// 剩余电量
    private int id;

}
