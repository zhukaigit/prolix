package com.zk.springbootswagger2.mongo.service;

import com.mongodb.client.result.DeleteResult;
import com.zk.springbootswagger2.mongo.entity.PersonInfo;
import com.zk.springbootswagger2.mongo.repository.PersonInfoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonInfoService {

  @Autowired
  private PersonInfoRepository personInfoRepository;
  @Autowired
  private MongoTemplate mongoTemplate;

  @Transactional
  public void hasTransaction(PersonInfo insertData, String deleteName) {
    testTransaction(insertData, deleteName);

  }

  public void noTransaction(PersonInfo insertData, String deleteName) {
    testTransaction(insertData, deleteName);
  }

  private void testTransaction(PersonInfo insertData, String deleteName) {

    // 插入
    PersonInfo insert = personInfoRepository.insert(insertData);
    System.err.println("新增了一条数据");

    // 查询
    Optional<PersonInfo> optional = personInfoRepository.findOne(Example.of(
        PersonInfo.builder().name(deleteName).build()
    ));
    System.err.println("查询结果：" + optional.get());
    if (!optional.isPresent()) {
      System.err.println("name = " + deleteName + "的记录不存在，无需删除");
    }

    try {
      Thread.sleep(6000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // 删除
    DeleteResult deleteResult = mongoTemplate.remove(optional.get());
    System.err.println("删除结果" + deleteResult.getDeletedCount());
  }

}
