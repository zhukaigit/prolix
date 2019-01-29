package com.zk.springbootswagger2.mongo;

import com.zk.springbootswagger2.mongo.entity.PersonInfo;
import com.zk.springbootswagger2.mongo.repository.PersonInfoRepository;
import com.zk.springbootswagger2.mongo.service.PersonInfoService;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonInfoServiceTest {

  @Autowired
  private PersonInfoService personInfoService;
  @Autowired
  private PersonInfoRepository personInfoRepository;

  @Before
  public void before() {
    personInfoRepository.deleteAll();

    PersonInfo p1 = PersonInfo.builder()
        .name("zhukai")
        .salary(new BigDecimal(99.18))
        .build();
    personInfoRepository.save(p1);
    System.err.println("=============== 已初始化完成 ==============");
  }

  @Test
  public void testHasTransaction() {
    PersonInfo p1 = PersonInfo.builder()
        .name("i'm new data")
        .salary(new BigDecimal(99.18))
        .build();
    personInfoService.hasTransaction(p1, "zhukai");
  }

  @Test
  public void testNoTransaction() {
    PersonInfo p1 = PersonInfo.builder()
        .name("zk")
        .salary(new BigDecimal(99.18))
        .build();
    personInfoService.noTransaction(p1, "zhukai");
  }

}
