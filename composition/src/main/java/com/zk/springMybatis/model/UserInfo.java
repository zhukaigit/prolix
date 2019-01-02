package com.zk.springMybatis.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "user_info")
public class UserInfo {
    private Long id;
    private String name;
    private Integer age;
    @Column(name = "userId")
    private String userId;
    @Column(name = "create_time")
    private String createTime;
    private Integer version;
}
