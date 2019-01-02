package com.zk;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 命名规则按照：资源名+_语言_国别.properties，
 */
public class ResourceBundleTest {
    /**
     * 每个资源文件中定义了本地化的信息，那么系统如何取到对应的资源文件呢？
     * ResourceBundle bundle = ResourceBundle.getBundle("res", new Locale("zh", "CN"));
     * 其中new Locale("zh", "CN");这个对象就告诉了程序你的本地化信息，
     * 就拿这个来说吧：程序查找步骤由前到后依次为：
     * 1、首先按照language和country来找，即res_zh_CN.properties
     * 2、若第1步没找到，则按照language来寻找，即res_zh.properties
     * 3、若第2步还没有找到，则根据当前系统的默认语言环境，假如是中文，则找res_zh_CN.properties
     * 4、若第3步没找到，则在res.properties中寻找
     * 5、若以上都没找到，那么就该抛异常了：MissingResourceException
     * 我们可以来写个测试程序验证一下：
     *
     */
    @Test
    public void shouldAnswerWithTrue() {
        final String baseName = "i18n/res";
        final String key = "fileName";

        print(new Locale("zh", "CN"), baseName, key);
        print(Locale.US, baseName, key);
        print(Locale.getDefault(), baseName, key);
        /**
         * 特别注意：
         * 没有对应该语言的资源文件，所以根据语言环境，找到了res_zh_CN.properties。
         * 如果把res_zh_CN.properties中的这个key注释掉，那么会找到res_zh.properties
         * 如果再把res_zh.properties中的key也注释掉，就会找到res.properties
         * 如果res.properties中的也注释掉，就会报错了
         */
        print(Locale.GERMAN, baseName, key);

    }

    // 测试不存在对应的Locale properties文件，而key在默认的【res.properties】文件中
    @Test
    public void test1() {
        final String baseName = "i18n/res";
        final String key = "this.is.no.locale.file.but.key.is.in.res.properties";

        print(new Locale("de", ""), baseName, key);
    }

    // key不在任何的properties文件中
    @Test
    public void test2() {
        final String baseName = "i18n/res";
        final String key = "key.is.not.exist";

        boolean hasException = false;
        try {
            print(new Locale("de", ""), baseName, key);
        } catch (Exception e) {
            System.err.println("exception name is " + e.getClass().getSimpleName());
            hasException = true;
        }

        Assert.assertTrue(hasException);
    }

    private void print(Locale locale, String baseName, String key) {
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
        String value = bundle.getString(key);
        System.out.printf("\n===分割符===\n" +
                        "locale = {language : %s, country : %s}\n" +
                        "key = %s \n" +
                        "value = %s" +
                        "\n===分割符===\n",
                locale.getLanguage(), locale.getCountry(), key, value);

    }
}
