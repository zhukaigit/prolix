package com.zk.springbootswagger2.mongo;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoTypeMappingTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Test
  public void testBefore() {
    mongoTemplate.dropCollection(Sample.class);
  }

  @Test
  public void testSave() {
    Sample s1 = Sample.builder()
        .person(Man.builder().describe("我是男的").build())
        .name("sample1").build();
    Sample s2 = Sample.builder()
        .person(Women.builder().describe("我是女的").build())
        .name("sample2").build();
    mongoTemplate.insertAll(Lists.newArrayList(s1, s2));
  }


  @Test
  public void testFindAll() {
    List<Sample> all = mongoTemplate.findAll(Sample.class);
    System.out.println();
  }

  @Data
  @ToString
  @Builder
  @Document("sample")
  private static class Sample {
    private String name;
    Person person;
  }

  private static abstract class Person {}

  @Data
  @ToString
  @Builder
//  @TypeAlias("man") // _class默认对应类的全限定名，使用该注解可指定其值
  private static class Man extends Person {
    private String describe;
  }

  @Data
  @ToString
  @Builder
  private static class Women extends Person {
    private String describe;
  }

}
