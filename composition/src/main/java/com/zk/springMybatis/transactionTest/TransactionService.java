package com.zk.springMybatis.transactionTest;

public interface TransactionService {

  void invokeDirectly();

  void invokeWithProxy();

  void transactionMethod(String name) throws Exception;
}
