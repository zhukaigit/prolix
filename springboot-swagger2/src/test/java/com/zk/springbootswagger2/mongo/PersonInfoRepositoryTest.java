package com.zk.springbootswagger2.mongo;

import com.google.common.collect.Lists;
import com.mongodb.client.result.UpdateResult;
import com.zk.springbootswagger2.mongo.entity.PersonInfo;
import com.zk.springbootswagger2.mongo.entity.PersonInfo.Toy;
import com.zk.springbootswagger2.mongo.repository.PersonInfoRepository;
import com.zk.springbootswagger2.utils.JsonUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonInfoRepositoryTest {

  @Autowired
  private PersonInfoRepository personInfoRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Before
  public void before() {
    personInfoRepository.deleteAll();
    savePerson("zhukai");
  }

  private PersonInfo savePerson(String name) {
    PersonInfo p1 = PersonInfo.builder()
        .name(name)
        .channel("CSCI")
        .birthday(new Date())
        .favourites(new String[]{"read", "swimming"})
        .toys(Lists.newArrayList(
            Toy.builder().name("小狗").price(12.8).build(),
            Toy.builder().name("小猪").price(5.8).build()
        ))
        .salary(new BigDecimal(99.18))
        .build();

    PersonInfo save = personInfoRepository.save(p1);
    return save;
  }

  @Test
  public void testUniqueIndex() {
    PersonInfo p1 = PersonInfo.builder()
        .name("zhukai")
        .build();
    personInfoRepository.save(p1);
  }

  // 更改了原文件结构
  @Test
  public void testUpdate1() {
    List<PersonInfo> list = personInfoRepository.findByName("zhukai");
    if (list != null && !list.isEmpty()) {
      PersonInfo personInfoDB = list.get(0);
      PersonInfo p = PersonInfo.builder().id(personInfoDB.getId()).channel("NEW_CHANNEL").build();
      PersonInfo save = personInfoRepository.save(p);
      System.out.println("保存后对象：" + save);
    } else {
      System.out.println("没有查询到数据");
    }
  }

  // 更新：新增key
  @Test
  public void testUpdate2() {
    List<PersonInfo> list = personInfoRepository.findByName("zhukai");
    if (list != null && !list.isEmpty()) {
      PersonInfo personInfoDB = list.get(0);
      personInfoDB.setNewField("新增的key");
      PersonInfo save = personInfoRepository.save(personInfoDB);
      System.out.println("保存后对象：" + save);
    } else {
      System.out.println("没有查询到数据");
    }
  }

  // 更新：新增key
  @Test
  public void testUpdate3() {
    UpdateResult updateResult = mongoTemplate.updateMulti(
        Query.query(new Criteria("user_name").is("zhukai")),
        Update.update("newField", "新增的key"), PersonInfo.class);
    System.out.println("保存后结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testExist() {
    personInfoRepository.findByNameFirst("zhukaiff").ifPresent(personInfo -> {
      System.out.println("id = " + personInfo.getId() + "是否存在：" + personInfoRepository
          .existsById(personInfo.getId()));
    });
  }

  @Test
  public void testFindByName() {
    List<PersonInfo> infoList = personInfoRepository.findByName("zhukai");
    System.out.println("查询到的结果："+JsonUtils.toJsonHasNullKey(infoList));
  }

  @Test
  public void testFindOne() {
    Optional<PersonInfo> personInfoOptional = personInfoRepository
        .findOne(Example.of(PersonInfo.builder().name("zhukai").build()));
    System.out.println("查询结果：" + personInfoOptional.get());
  }

  /**
   * 测试：使用@Query注解查询
   * @org.springframework.data.mongodb.repository.Query
   */
  @Test
  public void testWithQueryAnnotation() {
    PersonInfo p1 = PersonInfo.builder()
        .name("zk")
        .channel("jsy")
        .birthday(new Date())
        .favourites(new String[]{"read", "swimming"})
        .toys(Lists.newArrayList(
            Toy.builder().name("小狗").price(12.8).build(),
            Toy.builder().name("小猪").price(5.8).build()
        ))
        .build();
    PersonInfo save = personInfoRepository.save(p1);

    HashMap<String, Object> param = new HashMap<>();
    param.put("name", "zk");
    param.put("channel", "CSCI");
    List<HashMap> list = personInfoRepository
        .findByNameOrChannelWithAnnotation(param);
    System.err.println("查询结果：" + JsonUtils.toJsonHasNullKey(list));

  }

  @Test
  public void testConverter() {
    PersonInfo p1 = PersonInfo.builder()
        .name("zk")
        .channel("jsy")
        .birthday(new Date())
        .favourites(new String[]{"read", "swimming"})
        .toys(Lists.newArrayList(
            Toy.builder().name("小狗").price(12.8).build(),
            Toy.builder().name("小猪").price(5.8).build()
        ))
        .salary(new BigDecimal(200.15).setScale(2, RoundingMode.HALF_UP))
        .build();
    PersonInfo save = personInfoRepository.save(p1);

    List<PersonInfo> list = mongoTemplate
        .find(Query.query(Criteria.where("salary").gt(new BigDecimal(100))), PersonInfo.class);
    System.err.println("查询结果：" + JsonUtils.toJsonHasNullKey(list));

  }

}
