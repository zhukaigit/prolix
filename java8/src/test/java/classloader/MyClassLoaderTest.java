package classloader;

import com.test.classloader.User;

import java.io.FileInputStream;
import java.lang.reflect.Method;

public class MyClassLoaderTest {

    public static void main(String[] args) throws Exception {
        MyClassLoader myClassLoader = new MyClassLoader("D:\\3-temp\\testc_class_loader");
        Class<?> user = myClassLoader.loadClass("com.test.classloader.User");
        System.out.println(user.getName());
        System.out.println(user.getClassLoader());

        System.out.println("================ first ================");
        new User().say();
        System.out.println("================ second ================");


        Object o = user.newInstance();
        Method say = o.getClass().getDeclaredMethod("say");
        say.invoke(o);

    }

    static class MyClassLoader extends ClassLoader {
        private String classPath;

        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }

        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name
                    + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }

        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                //defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组。
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            synchronized (getClassLoadingLock(name)) {
                if (name.startsWith("com.test.classloader")) {
                    Class<?> c = findLoadedClass(name);
                    return c != null ? c : findClass(name);
                } else {
                    return super.loadClass(name, resolve);
                }
            }
        }
    }
}
