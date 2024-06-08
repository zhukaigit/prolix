package com.test.classloader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    static {
        System.out.println("user class version one");
    }

    public static void say() {
        System.out.println("我是主版本");
    }
}
