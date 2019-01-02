package com.zk.jdbc;

import org.junit.Test;

import javax.xml.bind.ValidationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//connection.setAutoCommit(false);相当于start transaction
public class JdbcTest {

    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        connection.setAutoCommit(false);
        int transactionIsolation = connection.getTransactionIsolation();

        /**
         * @see  java.sql.Connection.TRANSACTION_REPEATABLE_READ mysql默认隔离级别
         */
        System.out.println("默认的隔离级别：" + transactionIsolation);
        System.out.println("开始事务");
        PreparedStatement preparedStatement = connection
                .prepareStatement("insert into user_info (age, name, userId) values (14, '朱凯', ?)");
        preparedStatement.setString(1, UUID.randomUUID().toString());
        int i = preparedStatement.executeUpdate();
        System.out.println("插入结条数：" + i);
        Thread.sleep(5000);
        connection.commit();
        System.out.println("提交事务");
        JDBCUtils.close(connection, preparedStatement, null);
    }

    //一级封锁协议（对应read uncommited）
    //读取其他事务未提交的数据
    @SuppressWarnings("all")
    @Test
    public void testReadUncommitted() throws Exception {
        //唯一标示
        final String userId = UUID.randomUUID().toString();

        //事务一插入数据
        new Thread(() -> {
            try {
                Connection connection = JDBCUtils.getConnection();
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                connection.setAutoCommit(false);
                PreparedStatement statement = connection
                        .prepareStatement("insert into user_info (age, name, userId) values (12, '朱凯', ?)");
                statement.setString(1, userId);
                int i = statement.executeUpdate();
                System.out.println(String
                        .format("【线程%s】，执行插入userId=%s，结果为：%s", Thread.currentThread().getName(), userId, i));
                //睡眠10s，在次期间，其他线程查询该数据
                Thread.sleep(70000L);
                //回滚操作，发生胀读
                connection.rollback();
                connection.commit();
                JDBCUtils.close(connection, statement, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //睡眠一会，保证线程1已经插入
        Thread.sleep(1500);

        //线程二去查询
        new Thread(() -> {
            try {
                Connection connection = JDBCUtils.getConnection();
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                connection.setAutoCommit(false);
                PreparedStatement ps = connection
                        .prepareStatement("select * from user_info where userId = ?");
                ps.setString(1, userId);
                ResultSet resultSet = ps.executeQuery();
                List list = JDBCUtils.convertList(resultSet);
                System.out.println(String
                        .format("线程%s，查询条件userId=%s，查询结果为：%s", Thread.currentThread().getName(), userId, list));
                connection.commit();
                JDBCUtils.close(connection, ps, resultSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //等待所有线程执行完毕
        Thread.sleep(10000);

    }

    //二级封锁协议（对应read commited）
    //读取已提交的数据
    //测试防止胀读的情况
    @SuppressWarnings("all")
    @Test
    public void testReadCommitted() throws Exception {
        //唯一标示
        final String userId = UUID.randomUUID().toString();

        //事务一插入数据
        new Thread(() -> {
            try {
                Connection connection = JDBCUtils.getConnection();
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                PreparedStatement statement = connection
                        .prepareStatement("insert into user_info (age, name, userId) values (14, '朱凯', ?)");
                statement.setString(1, userId);
                int i = statement.executeUpdate();
                System.out.println(String
                        .format("【线程%s】，执行插入userId=%s，结果为：%s", Thread.currentThread().getName(), userId, i));
                //睡眠10s，在次期间，其他线程查询该数据
                Thread.sleep(7000L);
                //回滚操作，发生胀读
//        connection.rollback();
                System.out.println(String.format("【线程%s】提交事务", Thread.currentThread().getName()));
                connection.commit();
                JDBCUtils.close(connection, statement, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //睡眠一会，保证线程1已经插入
        Thread.sleep(1500);

        //线程二去查询
        new Thread(() -> {
            try {
                Connection connection = JDBCUtils.getConnection();
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                connection.setAutoCommit(false);
                PreparedStatement ps = connection
                        .prepareStatement("select * from user_info where userId = ?");
                ps.setString(1, userId);
                ResultSet resultSet = ps.executeQuery();
                List list = JDBCUtils.convertList(resultSet);
                System.out.println(String
                        .format("线程%s，查询条件userId=%s，查询结果为：%s", Thread.currentThread().getName(), userId, list));
                System.out.println(String.format("【线程%s】提交事务", Thread.currentThread().getName()));
                connection.commit();
                JDBCUtils.close(connection, ps, resultSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //等待所有线程执行完毕
        Thread.sleep(10000);

    }

    @Test
    @SuppressWarnings("all")
    public void testUpdate() throws Exception {
        new Thread(() -> {
            try {
                Connection connection = JDBCUtils.getConnection();
                connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                connection.setAutoCommit(false);
                //先查询
                PreparedStatement ps = connection
                        .prepareStatement("select * from user_info where id = 1");
                ResultSet resultSet = ps.executeQuery();
                List<Map> list = JDBCUtils.convertList(resultSet);
                System.out.println(String
                        .format("线程%s，查询条件id=%s，第一次查询结果为：%s", Thread.currentThread().getName(), 1, list));

                Thread.sleep(4000L);


                ps = connection
                        .prepareStatement("update user_info set userId = ? where id = 1 and userId = ?");
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, list.get(0).get("userId").toString());
                int i = ps.executeUpdate();
                if (!(i > 0)) {
                    throw new ValidationException("更新失败");
                }
                ps = connection
                        .prepareStatement("select * from user_info where id = 1");
                resultSet = ps.executeQuery();
                list = JDBCUtils.convertList(resultSet);
                System.out.println(String
                        .format("线程%s，查询条件id=%s，更新后，第二次查询结果为：%s", Thread.currentThread().getName(), 1, list));

                System.out.println(String.format("【线程%s】提交事务", Thread.currentThread().getName()));
                connection.commit();
                JDBCUtils.close(connection, ps, resultSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(1000L);

        new Thread(() -> {
            try {
                Connection connection = JDBCUtils.getConnection();
                connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                connection.setAutoCommit(false);
                //先查询
                PreparedStatement ps = connection
                        .prepareStatement("update user_info set userId = ? where id = 1");
                ps.setString(1, UUID.randomUUID().toString());
                ps.executeUpdate();
                ps = connection
                        .prepareStatement("select * from user_info where id = 1");
                ResultSet resultSet = ps.executeQuery();
                List list = JDBCUtils.convertList(resultSet);
                System.out.println(String
                        .format("线程%s，查询条件id=%s，更新后，查询结果为：%s", Thread.currentThread().getName(), 1, list));

                System.out.println(String.format("【线程%s】提交事务", Thread.currentThread().getName()));
                connection.commit();
                JDBCUtils.close(connection, ps, resultSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(7000);


    }

    //二级封锁协议（对应read commited）
    //读取已提交的数据
    //测试不可重复的情况
    @Test
    @SuppressWarnings("all")
    public void testReadCommitted2() throws Exception {
        testRepeatableRead(Connection.TRANSACTION_READ_COMMITTED);
    }

    //二级封锁协议（对应repeatable read）
    //读取已提交的数据
    //测试可重复情况
    @Test
    @SuppressWarnings("all")
    public void testRepeatableRead() throws Exception {
        testRepeatableRead(Connection.TRANSACTION_REPEATABLE_READ);

    }

    //二级封锁协议（对应serialization）
    //读取已提交的数据
    //测试可重复情况
    @Test
    @SuppressWarnings("all")
    public void testSerialization() throws Exception {
        testRepeatableRead(Connection.TRANSACTION_SERIALIZABLE);

    }


    /**
     * 测试是否可重复
     *
     * @param level 数据库隔离级别
     */
    private void testRepeatableRead(int level) throws Exception {
        System.out.println("数据库隔离级别：" + level);
        Long id = 1L;
        //事务一插入数据
        new Thread(() -> {
            try {
                Connection connection = JDBCUtils.getConnection();
                connection.setTransactionIsolation(level);
                connection.setAutoCommit(false);

                String sql = "select * from user_info where id = 1";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                System.out.println(String
                        .format("【线程%s】，第一次查询结果：%s", Thread.currentThread().getName(), JDBCUtils.convertList(resultSet)));
                //睡眠10s，在此期间，其他线程更新id=1的这条数据
                Thread.sleep(3000L);
                resultSet = statement.executeQuery();
                System.out.println(String
                        .format("【线程%s】，第二次查询结果：%s", Thread.currentThread().getName(), JDBCUtils.convertList(resultSet)));
                //事务提交
                connection.commit();
                JDBCUtils.close(connection, statement, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //保证线程1先于线程二执行
        Thread.sleep(500L);

        //事务二更新id=1的数据
        new Thread(() -> {
            try {
                Connection connection = JDBCUtils.getConnection();
                connection.setTransactionIsolation(level);
                connection.setAutoCommit(false);

                String sql = "UPDATE user_info set update_time = NOW() where id = 1;";
                PreparedStatement statement = connection.prepareStatement(sql);
                int i = statement.executeUpdate();
                System.out.println(String
                        .format("【线程%s】，更新结果：%s", Thread.currentThread().getName(), i));

                String querySql = "select * from user_info where id = 1";
                statement = connection.prepareStatement(querySql);
                ResultSet resultSet = statement.executeQuery();
                System.out.println(String
                        .format("【线程%s】，查询结果：%s", Thread.currentThread().getName(), JDBCUtils.convertList(resultSet)));

                connection.commit();
                JDBCUtils.close(connection, statement, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //等待所有线程执行完毕
        Thread.sleep(5000);

    }

}
