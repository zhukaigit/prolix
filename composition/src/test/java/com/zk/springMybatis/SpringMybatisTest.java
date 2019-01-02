package com.zk.springMybatis;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zk.springMybatis.mapper.UserMapper;
import com.zk.springMybatis.model.UserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springMybatis/application.xml"})
public class SpringMybatisTest {

    @Autowired
    private UserMapper userMapper;

    @Before
    public void testBefore() {
        System.out.println("\r\n================================= before =================================\n");
    }

    @After
    public void testAfter() {
        System.out.println("\n================================= after =================================\n");
    }

    @Test
    public void test() {
        UserInfo userInfo = userMapper.findById(1);
        System.out.println("查询结果：" + userInfo);
    }

    @Test
    public void testInsert() {
        int count = userMapper.insertUserInfo("测试");
        System.out.println("插入条数：" + count);
    }

    @Test
    public void testCommon() {
        UserInfo u = new UserInfo();
        u.setAge(17);
        u.setName("测试3");
        int insert = userMapper.insert(u);
        System.out.println("插入条数：" + insert);
    }

    @Test
    public void testUpdate() {
        int update = userMapper.updateById(1L, "朱凯1");
        System.out.println("更新结果：" + update);
    }

    @Test
    public void testUpdateUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setName("朱凯3");
        userInfo.setVersion(1);
        int update = userMapper.updateUser(userInfo);
        System.out.println("更新结果：" + update);
    }

    @Test
    public void testQueryByExample() {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("name", "朱凯");//查询条件
        List<UserInfo> userInfoList = userMapper.selectByExample(example);
        for (int i = 0; i < userInfoList.size(); i++) {
            System.out.println(String.format("userInfo[%s] = %s", i, userInfoList.get(i)));
        }
    }

    //通过使用tk.mybatis.mapper.entity.Example类来更新，类似于乐观锁
    @Test
    public void testUpdateByExample() {
        Example example = new Example(UserInfo.class);
        Criteria criteria = example.createCriteria().andEqualTo("id", 1);
        UserInfo userInfo = userMapper.selectOneByExample(example);
        criteria.andEqualTo("version", userInfo.getVersion());

        userInfo.setUserId(UUID.randomUUID().toString());
        userInfo.setVersion(userInfo.getVersion() + 1);
        int update = userMapper.updateByExampleSelective(userInfo, example);
        System.out.println("更新结果：" + update);
    }

    @Test
    public void testBatchInsert() {
        UserInfo u = null;
        for (int i = 1; i <= 100; i++) {
            u = new UserInfo();
            u.setName("name-" + i);
            u.setAge(i);
            userMapper.insertSelective(u);
        }
        System.out.println("insert finish");

    }

    /**
     * PageHelper参数设置可参考下面的博客地址
     *
     * @link : https://blog.csdn.net/u012562943/article/details/51838759
     */
    @Test
    public void testPageHelper() {
        PageHelper.startPage(1, 10);
        UserInfo u = new UserInfo();
        u.setName("name-1");
//    List<UserInfo> userInfoList = userMapper.select(u);
        List<UserInfo> userInfoList = userMapper.selectAll();
        PageInfo<UserInfo> page = new PageInfo<>(userInfoList);
        for (int i = 0; i < userInfoList.size(); i++) {
            System.out.println(String.format("userInfo[%s] = %s", i, userInfoList.get(i)));
        }
        System.out.println("=============== page信息 ===============");
        //测试PageInfo全部属性,PageInfo的强大之处，它携带以下信息
        System.out.println("当前页 - PageNum: " + page.getPageNum()); //2  第几页
        System.out.println("每页的数量 - PageSize: " + page.getPageSize()); //3  每页包含的条数
        System.out.println("当前页数量 - size: " + page.getSize()); //  当前页数量
        System.out.println("当前页面第一个元素在数据库中的行号 - StartRow: " + page.getStartRow()); //4  显示的页面的第一条的
        System.out.println("当前页面最后一个元素在数据库中的行号 - EndRow: " + page.getEndRow());  //6   显示的页面的最后一条的
        System.out.println("总记录数 - Total: " + page.getTotal());  //8   总记录数
        System.out.println("总页数 - Pages: " + page.getPages());  //3   总页数
        System.out.println("第一页 - FirstPage: " + page.getFirstPage());  //1  第一页
        System.out.println("前一页 - PrePage: " + page.getPrePage());  //  前一页
        System.out.println("后一页 - LastPage: " + page.getNextPage());   //3  后一页
        System.out.println("最后一页 - LastPage: " + page.getLastPage());   //3  最后一页
        System.out.println("isHasPreviousPage: " + page.isHasPreviousPage());
        System.out.println("isHasNextPage: " + page.isHasNextPage());

    }


}
