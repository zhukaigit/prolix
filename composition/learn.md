# 目录

| 包名                      | 功能                                                     |
| ------------------------- | -------------------------------------------------------- |
| com.zk.regex              | 主要测试正则表达式的匹配问题，包括Pattern与Matcher的使用 |
| com.zk.feign              | 主要测试原生feign的使用                                  |
| com.zk.jdbc               | 原生的数据库连接操作                                     |
| com.zk.mybatis            | 原生mybatis的使用                                        |
| com.zk.springMybatis      | mybatis与spring的整合使用                                |
| com.zk.modifier           | 访问修饰符的测试                                         |
| com.zk.other.TestForCycle | 几种不同for循环方式的区别测试                            |



# 正则表达式

## 常用分组语法

| 代码／语法        | 说明                                               |
| :---------------- | -------------------------------------------------- |
| (pattern)         | 匹配 pattern，并捕获文本到自动命名的组里           |
| (?<name> pattern) | 匹配 pattern，并捕获文本到名称为name的组里         |
| (?:pattern)       | 匹配 pattern，不捕获匹配的文本，也不给此组分配组号 |
| (?=pattern)       | 匹配pattern表达式之前的位置                        |
| (?<=pattern)      | 匹配pattern表达式之后的位置                        |
| (?!pattern)       | 匹配后面不是pattern的位置                          |
| (?!<pattern)      | 匹配前面不是pattern的位置                          |

```java
package com.zk.regex;


import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    /**
     *  语法】：(?<name>exp)
     * 【新知识】构造Pattern时，给group取一个名称，即在小阔内的正则表达式前使用"?<groupName>",如下的"?<num>"和"?<word>"
     *
     * 结果：
     * ================ 第1轮 ====================
     * matcher.group() = 12abc
     * matcher.group(1) = 12
     * matcher.group("num") = 12
     * matcher.group(2) = abc
     * matcher.group("word") = abc
     * ================ 第2轮 ====================
     * matcher.group() = 3bcd
     * matcher.group(1) = 3
     * matcher.group("num") = 3
     * matcher.group(2) = bcd
     * matcher.group("word") = bcd
     * ================ 第3轮 ====================
     * matcher.group() = 4ff
     * matcher.group(1) = 4
     * matcher.group("num") = 4
     * matcher.group(2) = ff
     * matcher.group("word") = ff
     * ================ 第4轮 ====================
     * matcher.group() = 56f7
     * matcher.group(1) = 56
     * matcher.group("num") = 56
     * matcher.group(2) = f7
     * matcher.group("word") = f7
     *
     * Process finished with exit code 0
     */
    @org.junit.Test
    public void testGroupName() {
        String src = "12abc你好3bcd路径4ff将56f7";
        Pattern pattern = Pattern.compile("(?<num>\\d+)(?<word>\\w+)");
        Matcher matcher = pattern.matcher(src);
        boolean b = matcher.find();
        int i = 0;
        while (b) {
            i++;
            System.out.println("================ 第" + i + "轮 ====================");
            System.out.println("matcher.group() = " + matcher.group());
            System.out.println("matcher.group(1) = " + matcher.group(1));
            System.out.println("matcher.group(\"num\") = " + matcher.group("num"));
            System.out.println("matcher.group(2) = " + matcher.group(2));
            System.out.println("matcher.group(\"word\") = " + matcher.group("word"));
            b = matcher.find();
        }
    }

    /**
     * 【语法】：(?<name>exp)
     *
     * 类似Zuul自定义路由关系映射【PatternServiceRouteMapper】，需要将如 user-service-v1/**的请求映射称 /v1/user-service/**的请求
     * 即入参为：user-service-v1/** ，得到 /v1/user-service/**
     * 即入参为：helloService-v2/** ，得到 /v2/helloService/**
     *
     * 打印如下：
     * v1/user-service/user/login
     */
    @Test
    public void testGroupNameReplace() {
        System.out.println(getRequestMapping("user-service-v1/user/login"));
    }

    private String getRequestMapping(String url) {
        Pattern urlPattern = Pattern.compile("(?<serviceName>.+)-(?<version>v\\d+)/(?<resourceUrl>.*)");
        Matcher matcher = urlPattern.matcher(url);
        /**
         * 【新知识】${version}相当于matcher.group("version")的值
         */
        String result = matcher.replaceFirst("${version}/${serviceName}/${resourceUrl}");
        return result;

    }
    
    //==================== 以下是断言测试 ====================

    /**
     * 【语法】: (?=pattern)
     *  用途: 匹配pattern表达式之前的位置
     *
     * 例如，要匹配 cooking ，singing ，doing中除了ing之外的内容，
     * 只取cook, sing, do的内容，这时候的增则表达式可以用 [a-z]*(?=ing) 来匹配
     *
     * 注意：先行断言的执行步骤是这样的先从要匹配的字符串中的最右端找到第一个 ing
     * (也就是先行断言中的表达式)然后 再匹配其前面的表达式，若无法匹配则继续查找第二个
     * ing 再匹配第二个 ing 前面的字符串，若能匹配则匹配，符合正则的贪婪性。
     *
     * 打印如下：
     * cook
     *
     * think
     */
    @Test
    public void test1() {
        String src = "cooking thinking";
        Pattern pattern = Pattern.compile("[a-z]*(?=ing)");
        print(pattern, src);
    }

    /**
     * 【语法】: (?<=pattern)
     *  用途: 匹配pattern表达式之后的位置
     *
     * 示例：匹配某个标签之内的内容
     *
     * 打印如下：
     *  this is paragraph content
     *  this is div content
     */
    @Test
    public void test2() {
        String src =
                "<p> this is paragraph content </p> " +
                "</br> " +
                "<div> this is div content </div>";
        Pattern pattern = Pattern.compile("(?<=<\\w{1,10}>).*?(?=</\\w+>)");
        print(pattern, src);
    }

    /**
     * 【语法】： (?<!pattern)
     *  用途： 匹配前面不是pattern的位置
     *
     * 例如“(?<!95|98|NT|2000)Windows”能匹配“3.1Windows”中的“Windows”，但不能匹配“2000Windows”中的“Windows”。
     *
     * 打印如下：
     * Windows
     */
    @Test
    public void test4() {
        String src = "3.1Windows 你好呀！";
        Pattern pattern = Pattern.compile("(?<!95|98|NT|2000)[a-zA-Z]+");
        print(pattern, src);

    }

    /**
     * 【语法】： (?!pattern)
     *  用途： 匹配后面不是pattern的位置
     *
     * 打印如下：
     * Windows
     */
    @Test
    public void test5() {
        String src = "我很喜欢Windows3.1";
        Pattern pattern = Pattern.compile("[a-zA-Z]+(?!95|98|NT|2000)");
        print(pattern, src);

    }

    private void print(Pattern pattern, String src) {
        Matcher matcher = pattern.matcher(src);
        boolean find = matcher.find();
        while (find) {
            System.out.println(matcher.group());
            find = matcher.find();
        }
    }
}

```