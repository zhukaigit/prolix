import org.apache.lucene.util.RamUsageEstimator;
import org.openjdk.jol.info.ClassLayout;

/**
 * 计算对象大小
 * 查看一个对象实例占用内存的大小，会展示“对象头（mark word和klass pointer）、对象中各个属性、对象填充”各个部分占用多少字节。
 * 注意：不包含该对象所引用对象的大小，如想要查看可参考{@link MemoryOccupyTest}类。仅仅是该对象实例在内存中的大小。
 */
public class JOLSample {

    public static void main(String[] args) {
        System.out.println(" ------------------ new Object -----------------");
        ClassLayout layout = ClassLayout.parseInstance(new Object());
        System.out.println(layout.toPrintable());
        System.out.println("大小（byte）："+RamUsageEstimator.shallowSizeOf(new Object()));

        System.out.println();
        System.out.println(" ------------------ new int[] -----------------");
        ClassLayout layout1 = ClassLayout.parseInstance(new int[]{});
        System.out.println(layout1.toPrintable());
        System.out.println("大小（byte）："+RamUsageEstimator.shallowSizeOf(new int[]{}));

        System.out.println();
        System.out.println(" ------------------ new int[20] -----------------");
        ClassLayout layout1_2 = ClassLayout.parseInstance(new int[20]);
        System.out.println(layout1_2.toPrintable());
        System.out.println("大小（byte）："+RamUsageEstimator.shallowSizeOf(new int[20]));

        System.out.println();
        System.out.println(" ------------------ new byte[13] -----------------");
        ClassLayout layout1_4 = ClassLayout.parseInstance(new byte[13]);
        System.out.println(layout1_4.toPrintable());
        System.out.println("大小（byte）："+RamUsageEstimator.shallowSizeOf(new byte[13]));

        System.out.println();
        System.out.println(" ------------------ new String[20] -----------------");
        ClassLayout layout1_3 = ClassLayout.parseInstance(new String[20]);
        System.out.println(layout1_3.toPrintable());
        System.out.println("大小（byte）："+RamUsageEstimator.shallowSizeOf(new String[20]));

        System.out.println();
        A a = new A();
        System.out.println(" ------------------ new A() -----------------");
        ClassLayout layout2 = ClassLayout.parseInstance(a);
        System.out.println(layout2.toPrintable());
        System.out.println("大小（byte）："+RamUsageEstimator.shallowSizeOf(a));
    }

    // ‐XX:+UseCompressedOops 默认开启的压缩所有指针
    // ‐XX:+UseCompressedClassPointers 默认开启的压缩对象头里的类型指针Klass Pointer
    // Oops : Ordinary Object Pointers
    public static class A {
        //8B mark word
        //4B Klass Pointer 如果关闭压缩‐XX:‐UseCompressedClassPointers或‐XX:‐UseCompressedOops，则占用8B
        int id; //4B
        String name; //4B 如果关闭压缩‐XX:‐UseCompressedOops，则占用8B
        byte b; //1B
        Object o; //4B 如果关闭压缩‐XX:‐UseCompressedOops，则占用8B
    }
}
