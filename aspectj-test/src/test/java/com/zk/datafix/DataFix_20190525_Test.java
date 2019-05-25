package com.zk.datafix;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DataFix_20190525_Test {

    @Test
    public void test() throws Exception {
        String data_path = "classpath:datafix/20190525/data.txt";
        String db_path = "classpath:datafix/20190525/db.txt";
        String id_path = "classpath:datafix/20190525/id.txt";
        ArrayList<String> data_list = Tools.fileToArr(data_path);
        ArrayList<String> db_list = Tools.fileToArr(db_path);
        ArrayList<String> id_list = Tools.fileToArr(id_path);

        // 校验数量
        Assert.assertTrue(data_list.size() == id_list.size());
        Assert.assertTrue(db_list.size() == id_list.size());

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < id_list.size(); i++) {
            String ele = String.format("\r\nUPDATE %s\n" +
                    "SET settlement_amount = '%s',\n" +
                    "  modifier            = 'zhukai001',\n" +
                    "  gmt_modified        = curdate()\n" +
                    "WHERE id = %s;\r\n", db_list.get(i), data_list.get(i), id_list.get(i));
            result.append(ele);
        }

        System.out.println(result);
        System.out.println("总数量：" + data_list.size());

    }

    @Test
    public void test2() {

        System.out.println();

    }

}
