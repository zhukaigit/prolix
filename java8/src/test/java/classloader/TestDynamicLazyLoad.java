package classloader;

public class TestDynamicLazyLoad {
    static {
        System.out.println("class TestDynamicLazyLoad 的静态模块");
    }

    public TestDynamicLazyLoad(){
        System.out.println("class TestDynamicLazyLoad 的构造方法已启动");
    }

    public static void main(String[] args) {
        new A();
        B b = null;
    }
}

class A {
    static {
        System.out.println("class A 的静态模块");
    }

    public A(){
        System.out.println("class A的构造方法已启动");
    }
}

class B {
    static {
        System.out.println("class B 的静态模块");
    }

    public B(){
        System.out.println("class B的构造方法已启动");
    }
}


