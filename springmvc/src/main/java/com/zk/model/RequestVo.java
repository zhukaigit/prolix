package com.zk.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RequestVo<T> {
    private String channel;
    private T data;
}
