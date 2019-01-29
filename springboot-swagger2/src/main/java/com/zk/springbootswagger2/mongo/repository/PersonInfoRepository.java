package com.zk.springbootswagger2.mongo.repository;

import com.zk.springbootswagger2.mongo.entity.PersonInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.util.CollectionUtils;

public interface PersonInfoRepository extends MongoRepository<PersonInfo, String> {

  List<PersonInfo> findByName(String name);

  default Optional<PersonInfo> findByNameFirst(String name) {
    List<PersonInfo> list = findByName(name);
    return Optional.ofNullable(CollectionUtils.isEmpty(list) ? null : list.get(0));
  }

  /**
   * ?0为第一个参数, ?1为第二个参数。
   * 注意：参数支持SpEL表达式。使用SpEL表达式时，第n个参数的写法为：?#{[n].fieldName}
   * @param map ：要求含有name、channel两个key
   * @return
   */
  @Query(value = "{$or : [ { 'name' : ?#{[0][name]} }, { 'channel' : ?#{[0][channel]} } ]}",
      fields = "{'name' : 1, 'channel' : 1, 'favourites' : 1}")
  List<HashMap> findByNameOrChannelWithAnnotation(HashMap<String, Object> map);

}
