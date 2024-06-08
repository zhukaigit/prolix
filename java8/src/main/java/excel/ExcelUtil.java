package excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    /**
     * 读取Excel文件内容
     * @return
     */
    public static List<Map<String, Object>> readExcel() {
        /* Excel文件地址 */
        String filePath = "D:\\3-temp\\02-test\\offer_rule_current_20220822093300.xlsx";
        String strField = "no,proxy_name_2,increase_day,increase_month,increase_year,total_num,no_trade_num";
        String[] fieldArr = strField.split(",");

        List<Map<String, Object>> retList = new ArrayList<>();
        try {
            /**
             * HSSFworkbook,XSSFworkbook,SXSSFworkbook区别总结
             *
             * HSSFWorkbook:是操作Excel2003以前（包括2003）的版本，扩展名是.xls；     【导出的行数至多为65535行，超出65536条后系统就会报错】
             * XSSFWorkbook:是操作Excel2007后的版本，扩展名是.xlsx；                 【其对应的是excel2007(1048576行，16384列)扩展名为“.xlsx”，最多可以导出104万行】
             * SXSSFWorkbook:是操作Excel2007后的版本，扩展名是.xlsx；                【不会内存溢出】
             */
            /* 创建Excel工作本的引用 */
            Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
//            Workbook workbook = new SXSSFWorkbook(new FileInputStream(filePath));

            /* 读取第一张工作表 Sheet1 */
            Sheet sheet = workbook.getSheet("Sheet1");

            System.out.println(sheet.getLastRowNum());

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {

                /* 获取每一行 */
                Row row = sheet.getRow(i);
                Map<String, Object> itemMap = new HashMap<>();

                for (int cellNum = 0; cellNum < fieldArr.length; cellNum++) {
                    String key = fieldArr[cellNum];
                    String item = row.getCell(cellNum).toString();
                    itemMap.put(key, item);
                }
                retList.add(itemMap);
            }

            System.out.println(retList);
        } catch (Exception e) {
            System.out.println(e);
        }

        return retList;
    }


    /**
     * 写出数据到Excel工作簿
     *
     * @param workbook    工作簿
     * @param headerCount 表头高度
     * @param offsetRow   偏移行
     * @param dataList    数据
     * @param mapper      映射关系
     * @return 下次开始行
     *
     * <p>我该如何使用?</p>
     * @see ExportUtils#textExport
     */
    public static int writeExcel(Workbook workbook, int headerCount, int offsetRow, List<Map<String, Object>> dataList, Map<Integer, String> mapper) {
        for (int i = 0; i < mapper.size(); i++) {
            String value = mapper.get(i);
            if (value == null) {
                throw new RuntimeException("映射关系有错误");
            }
        }

        Sheet sheet = workbook.getSheet("Sheet1");
        for (Map<String, Object> data : dataList) {
            Row row = sheet.createRow(headerCount + offsetRow++);

            for (int i = 0; i < mapper.size(); i++) {
                String columnName = mapper.get(i);
                if (columnName.isEmpty()) {
                    continue;
                }

                Cell cell = row.createCell(i);

                if ("$AUTO".equals(columnName)) {
                    cell.setCellValue(offsetRow);
                    continue;
                }

                Object value = data.get(columnName);
                if (value == null) {
                    cell.setCellValue("");
                    continue;
                }
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        }

        return offsetRow;
    }

    /**
     * 测试写入
     */
    public static void textExport() {
        try {
            String inputFilePath = "F:\\inputExcel.xlsx";
            String outputFilePath = "F:\\outputExcel.xlsx";

            /* 关系映射表 */
            Map<Integer, String> mapper = new HashMap<>();
            mapper.put(0, "$AUTO"); // 序号定义
            mapper.put(1, "proxy_name_2");
            mapper.put(2, "increase_day");
            mapper.put(3, "increase_month");
            mapper.put(4, "increase_year");
            mapper.put(5, "total_num");
            mapper.put(6, "no_trade_num");

            /* 查询数据 */
            List<Map<String, Object>> dataList = new ArrayList<>();

            FileInputStream inputStream = new FileInputStream(inputFilePath);
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            Workbook workbook = WorkbookFactory.create(inputStream);

            int headerCount = 2;   // 设置表头高度
            int offsetRow = 0;     // 初始偏移量0
            // 写行数据
            writeExcel(workbook, headerCount, offsetRow, dataList, mapper);

            workbook.write(outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        textExport();
        readExcel();
    }
}
