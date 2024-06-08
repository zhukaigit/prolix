import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import excel.XlsUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.util.RamUsageEstimator;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TempTest {

    Cache<byte[], byte[]> spaceCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(200, TimeUnit.MILLISECONDS).build();
    private int _1MB = 1024 * 1024;

    /**
     * ================== 缓存前 ==================
     * 堆空间大小：240MB
     * sizeOf = 0MB
     * shallowSizeOf = 16KB
     * humanSizeOf = 1.3 KB
     * ================== 缓存后 ==================
     * 堆空间大小：166MB
     * sizeOf = 70MB
     * shallowSizeOf = 16KB
     * humanSizeOf = 70 MB
     * 缓存结果：52428800
     * ================== 过期后 ==================
     * 堆空间大小：170MB
     * sizeOf = 70MB
     * shallowSizeOf = 16KB
     * humanSizeOf = 70 MB
     * 缓存结果：null
     * ================== 过期后, GC后, waitding 1min ==================
     * 堆空间大小：220MB
     * sizeOf = 0MB
     * shallowSizeOf = 16KB
     * humanSizeOf = 1.3 KB
     * ================== 删除后, GC前 ==================
     * 堆空间大小：220MB
     * sizeOf = 0MB
     * shallowSizeOf = 16KB
     * humanSizeOf = 1.3 KB
     * ================== 删除后, GC后 ==================
     * 堆空间大小：240MB
     * sizeOf = 0MB
     * shallowSizeOf = 16KB
     * humanSizeOf = 1.3 KB
     *
     * Process finished with exit code 0
     * @throws InterruptedException
     */
    @Test
    public void test3() throws InterruptedException {
        System.gc();
        System.out.println("================== 缓存前 ==================");
        printSize(spaceCache);

        System.out.println("================== 缓存后 ==================");
        byte[] key = new byte[_1MB * 20];
        spaceCache.put(key, new byte[_1MB * 50]);
        printSize(spaceCache);
        System.out.println("缓存结果：" + (spaceCache.getIfPresent(key) != null ? spaceCache.getIfPresent(key).length : "null"));

        Thread.sleep(2000);
        System.out.println("================== 过期后 ==================");
        System.gc();
        printSize(spaceCache);
        System.out.println("缓存结果：" + (spaceCache.getIfPresent(key) != null ? spaceCache.getIfPresent(key).length : "null"));

        System.out.println("================== 过期后, GC后, waitding 1min ==================");
        Thread.sleep(65*1000);
        System.gc();
        printSize(spaceCache);

        System.out.println("================== 删除后, GC前 ==================");
        spaceCache.invalidate(key);
        key = null;
        printSize(spaceCache);

        System.out.println("================== 删除后, GC后 ==================");
        System.gc();
        printSize(spaceCache);
    }

    /**
     * 对象头：12
     * 数组对象头：16
     */
    private void printSize(Object target) {
        System.out.println("堆空间大小：" + Runtime.getRuntime().freeMemory() / _1MB + "MB");
        //计算指定对象及其引用树上的所有对象的综合大小，单位字节
        System.out.println("sizeOf = " + RamUsageEstimator.sizeOf(target) / _1MB + "MB");
        //计算指定对象本身在堆空间的大小，单位字节
        System.out.println("shallowSizeOf = " + RamUsageEstimator.shallowSizeOf(target) + "KB");
        //计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果，如：2KB
        System.out.println("humanSizeOf = " + RamUsageEstimator.humanSizeOf(target));
    }

    @Test
    public void test() throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(new File("D:\\whiteList.xls"));
            List<Object[]> list = XlsUtil.readExcel(in);
            for (int i = 1; i < list.size(); i++) {
                Object[] objects = list.get(i);
                Object quote_id = objects[0];
                String property_value = objects[5].toString();
                List<String> rules = findRules(property_value);
                StringBuilder sb = new StringBuilder(quote_id.toString());
                for (String rule : rules) {
                    sb.append("\t\t\t").append(StringUtils.rightPad(rule, 20, ""));
                }
                System.out.println(sb);
            }
        } finally {
            in.close();
        }
    }

    @Test
    public void testout() throws IOException {
        OutputStream out = null;
        try {
            String[] title = {"quote_id", "规则1", "规则2"};
            out = new FileOutputStream(new File("D:\\Out_whiteList2.xls"));
            List<String[]> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int index = RandomUtils.nextInt(3, 5);
                String[] objects = new String[index];
                for (int j = 0; j < index; j++) {
                    objects[j] = "abcdjfldskjfl124dsfjsdk".substring(0, RandomUtils.nextInt(5, 15));
                }
                list.add(objects);
            }
            XlsUtil.exportExcelFile(title, list, out);
        } finally {
            out.flush();
            out.close();

        }

    }

    public static final String temp = "{\"errorCode\":\"0\",\"errorMsg\":\"\",\"factors\":{\"relationshipOfRegisterDateAndFirstSellYear\":\"0\"},\"respDTO\":{\"accommodationResults\":[],\"channelFlagDim\":\"TOC民线\",\"commonResps\":[{\"hitRuleVersion\":\"uw_accommodation_current_226_2_20201214102840\",\"ruleCode\":\"uw_accommodation_current_226_2\",\"ruleEffectiveTime\":\"2020-12-14 10:29:29\"},{\"hitRuleVersion\":\"uw_accommodation_current_20220524163836\",\"ruleCode\":\"uw_accommodation_current\",\"ruleEffectiveTime\":\"2022-05-24 16:38:29\"}],\"listLabel\":{\"label\":\"28\",\"reason\":\"首年续优质名单\"},\"needAccommodation\":false,\"nonLocalVehicleFlag\":true,\"pass\":true,\"ruleTrace\":\"渠道标识:TOC民线,本地车校验：属于异地车,高风险车型组：托底分组,险别信息：通过,险别信息：通过,险别信息：通过,监管核保规则：通过,通融规则：触碰规则：V12839,,通融规则：通过,通融规则：触碰规则：VTYD003,,监管通融规则：通过\",\"vehicleRiskGroup\":\"托底分组\",\"whitelists\":[{\"id\":\"11aab597-3062-410b-bdef-ac993416027f\",\"whitelistType\":\"BIZ\"}]},\"success\":true}";

    @Test
    public void test2() {

        List<String> rules = findRules(temp);
        System.out.println(rules);

    }

    private List<String> findRules(String temp) {
        if (!temp.contains("触碰规则")) {
            return Collections.EMPTY_LIST;
        }
        ArrayList<String> list = new ArrayList<>();
//        Pattern pattern = Pattern.compile("(触碰规则：\\w+),");
        Pattern pattern = Pattern.compile(",([\\u4E00-\\u9FA5]+：触碰规则：\\w+?),");
        Matcher matcher = pattern.matcher(temp);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }


    static class A {
        private CharSequence name = getName();

        protected CharSequence getName() {
            return "a";
        }

        public void say() {
            System.out.println("name = " + name);
        }
    }

    static class B extends A {
        @Override
        protected String getName() {
            return "b";
        }
    }

    static class C extends B {
        public static void main(String[] args) {
            new C().say();
        }
    }
}
