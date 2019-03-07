package com.zk.springMybatis.transactionTest;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionService {

  void invokeDirectly();

  void invokeWithProxy();

  void transactionMethod(String name) throws Exception;

  @Transactional(propagation = Propagation.NESTED)
  void insertUser2(String name);

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  void insertUser3(String name);

  @Transactional
  void testNested(String name);

  @Transactional
  void testNestedRollback(String name);

  @Transactional
  void testRequestNew(String name);
}
