package com.zk.jdbc;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class JDBCUtils {

    //将读取属性文件放在静态代码块中
    //保证文件只被读取一次，节省资源
    static Properties prop = null;

    static {
        try {
            //读取配置文件jdbc.properties
            prop = new Properties();
            String pathname = "jdbc/jdbc.properties";
            InputStream resourceAsStream = JDBCUtils.class.getClassLoader().getResourceAsStream(pathname);
            prop.load(resourceAsStream);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //1.私有化构造函数，
    // 防止外界直接new对象
    private JDBCUtils() {
    }

    //2.提供getConnection，
    // 用来对外界提供获取数据连接
    public static Connection getConnection() {
        try {
            //1.注册驱动
            Class.forName(
                    prop.getProperty("jdbc.driverClassName"));
            //2.获取数据库连接
            Connection conn = DriverManager.getConnection(
                    prop.getProperty("jdbc.url"),
                    prop.getProperty("jdbc.user"),
                    prop.getProperty("jdbc.pass"));
            return conn;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    // 3.提供close方法，用来关闭资源
    @SuppressWarnings("all")
    public static void close(Connection conn, Statement st, ResultSet rs) {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                //保证资源一定会被释放
                conn = null;
            }
        }

        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                //保证资源一定会被释放
                st = null;
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                //保证资源一定会被释放
                rs = null;
            }
        }
    }

    public static List<Map> convertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        while (rs.next()) {
            Map rowData = new HashMap();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }
}
