package com.zk.springbootswagger2.mongo.repository;

import com.zk.springbootswagger2.mongo.entity.PersonInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.util.CollectionUtils;

public interface PersonInfoRepository extends MongoRepository<PersonInfo, String> {

  List<PersonInfo> findByName(String name);

  default Optional<PersonInfo> findByNameFirst(String name) {
    List<PersonInfo> list = findByName(name);
    return Optional.ofNullable(CollectionUtils.isEmpty(list) ? null : list.get(0));
  }

}
