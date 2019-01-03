package com.zk;

import com.zk.inter.impl.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.tools.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Unit test for simple App.
 */
 @RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(locations = "classpath:spring-config.xml")
public class AspectjTest {

    @Autowired
    Man man;

    @Autowired
    Woman woman;

    @Autowired
    Tiger tiger;

    @Autowired
    Target target;

    /*public void t() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        StandardLocation classOutput = StandardLocation.CLASS_OUTPUT;
        fileManager.setLocation();

        Iterable<? extends JavaFileObject> compilationUnits1 =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files1));
        compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();

        Iterable<? extends JavaFileObject> compilationUnits2 =
                fileManager.getJavaFileObjects(files2); // use alternative method
// reuse the same file manager to allow caching of jar files
        compiler.getTask(null, fileManager, null, null, null, compilationUnits2).call();

//        fileManager.close();

    }*/

    @Test
    public void test_within_1() {
        target.go();
    }

    @Test
    public void test_before11() {
        ArrayList<RequestVo> list = new ArrayList<RequestVo>();
        tiger.generic(list);
    }

    @Test
    public void test_before10() {
        tiger.oneParamHasAnnotation(new RequestVo());
    }

    @Test
    public void test_before9() {
        tiger.twoParamHasAnnotation("1","2");
    }

    @Test
    public void test_before8() {
        tiger.returnValue();
    }

    @Test
    public void test_before7() {
        tiger.twoAnnotation();
        tiger.addLogAnnotationMethod("a");
        tiger.ipWhiterAnnotationMethod();
    }

    @Test
    public void test_before6() {
        tiger.twoAnnotation();
    }

    @Test
    public void test_before5() {
        tiger.addLogAnnotationMethod("a");
    }

    @Test
    public void test_before4() {
        //这个会被拦截
        tiger.any(new Date());
        //这个不会
        tiger.any(3);
    }

    @Test
    public void test_beforePointcut3() {
        tiger.day(new Date());
    }

    @Test
    public void test_beforePointcut2() {
        System.out.println("className = " + tiger.getClass().getName());
        tiger.say("大王");
    }

    @Test
    public void test_beforePointcut1() {
//        System.out.println("className = " + man.getClass().getName());
        man.say("朱凯1");
//        System.out.println("className = " + woman.getClass().getName());
//        woman.say("岳如意");
    }



}
