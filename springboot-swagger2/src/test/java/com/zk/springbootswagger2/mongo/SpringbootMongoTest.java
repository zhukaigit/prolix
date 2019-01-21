package com.zk.springbootswagger2.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootMongoTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  // 插入测试数据
  @Test
  public void testInsertTestData() {
    generateTestData();
  }

  private void generateTestData() {
    // 删除数据库
    mongoTemplate.dropCollection("order");

    // 数据插入
    OrderInfo o1 = OrderInfo.builder()
        .id(1).item("abc").price(10).quantity(2).date(new Date()).build();
    o1 = mongoTemplate.insert(o1, "order");

    OrderInfo o2 = OrderInfo.builder()
        .id(2).item("jkl").price(20).quantity(1).date(new Date()).build();
    o2 = mongoTemplate.insert(o2, "order");

    OrderInfo o3 = OrderInfo.builder()
        .id(3).item("xyz").price(5).quantity(10).date(new Date()).build();
    o3 = mongoTemplate.insert(o3, "order");

    OrderInfo o4 = OrderInfo.builder()
        .id(4).item("abc").price(10).quantity(20).date(new Date()).build();
    o4 = mongoTemplate.insert(o4, "order");

    OrderInfo o5 = OrderInfo.builder()
        .id(5).item("abc").price(15).quantity(10).date(new Date()).build();
    o5 = mongoTemplate.insert(o5, "order");

    OrderInfo o6 = OrderInfo.builder()
        .id(6).item("jkl").price(5).quantity(3).date(new Date()).build();
    o6 = mongoTemplate.insert(o6, "order");
  }

  // eq
  @Test
  public void testQuery1() {
    Criteria criteria = Criteria.where("item").is("abc");
    List<OrderInfo> orderInfoList = mongoTemplate
        .find(Query.query(criteria), OrderInfo.class, "order");
    print(orderInfoList);
  }

  // lt, gt, lte, gte
  @Test
  public void testQuery2() {
    Criteria criteria = Criteria.where("price").gte(10);
    List<OrderInfo> orderInfoList = mongoTemplate
        .find(Query.query(criteria), OrderInfo.class, "order");
    print(orderInfoList);
  }

  // and连接多个条件
  @Test
  public void testQuery3() {
    Criteria criteria = Criteria
        .where("item").is("jkl")
        .and("price").gte(10);
    List<OrderInfo> orderInfoList = mongoTemplate
        .find(Query.query(criteria), OrderInfo.class, "order");
    print(orderInfoList);
  }

  // or连接多个条件
  @Test
  public void testQuery4() {
    Criteria criteria = new Criteria()
        .orOperator(Criteria.where("item").is("xyz"), Criteria.where("item").is("jkl"));
    List<OrderInfo> orderInfoList = mongoTemplate
        .find(Query.query(criteria), OrderInfo.class, "order");
    print(orderInfoList);
  }

  // and or连接多个条件
  @Test
  public void testQuery5() {
    Criteria criteria = Criteria.where("price").is(5)
        .orOperator(Criteria.where("item").is("xyz"), Criteria.where("item").is("jkl"));
    List<OrderInfo> orderInfoList = mongoTemplate
        .find(Query.query(criteria), OrderInfo.class, "order");
    print(orderInfoList);
  }

  // between... and
  @Test
  public void testQuery6() {
    Criteria criteria = Criteria
        .where("price").gt(5).lt(20);
    List<OrderInfo> orderInfoList = mongoTemplate
        .find(Query.query(criteria), OrderInfo.class, "col");
    print(orderInfoList);
  }

  @Test
  public void testSort() {
    Query query = Query.query(new Criteria());
    query.with(Sort.by(
        new Order(Direction.ASC, "price"),
        new Order(Direction.DESC, "quantity")
    ));
    List<OrderInfo> orderList = mongoTemplate.find(query, OrderInfo.class, "order");
    print(orderList);
  }

  // 更新所有记录
  @Test
  public void testUpdate() {
    generateTestData();

    UpdateResult updateResult = mongoTemplate.updateMulti(
        Query.query(Criteria.where("price").is(5)),
        Update.update("price", 6),
        OrderInfo.class, "order");
    System.out.println("updateCount = " + updateResult.getModifiedCount());

    // 恢复
    mongoTemplate.updateMulti(Query.query(Criteria.where("price").is(6)), Update.update("price", 5),
        OrderInfo.class, "order");
  }

  // 更新该记录的document结构
  @Test
  public void testUpdate2() {
    generateTestData();

    Document doc = new Document().append("newFiled", "xxxxxx");
    Update update = Update.fromDocument(doc);

    Query query = Query.query(Criteria.where("price").is(5));
    // 注意：即使使用updateMulti()也没有效果，只能更新一条
    UpdateResult updateResult = mongoTemplate.updateFirst(query, update,"order");
    System.out.println("updateCount = " + updateResult.getModifiedCount());

    printCollection("order");

  }

  // 更新该记录的document结构
  @Test
  public void testUpdate3() throws InterruptedException {
    generateTestData();

    // 更新该记录的document结构
    HashMap<String, Object> map = new HashMap<>();
    map.put("_id", 1);
    map.put("newField", "xxxxxxx");
    mongoTemplate.save(map, "order");

    printCollection("order");
  }

  // 更新子文档
  @Test
  public void testUpdate4() throws InterruptedException {
    mongoTemplate.dropCollection("order");

    // 数据插入
    OrderInfo o1 = OrderInfo.builder()
        .id(1).item("abc").price(10).quantity(2).date(new Date())
        .owner(new User("zhukai", 29))
        .build();
    o1 = mongoTemplate.insert(o1, "order");

    System.out.println("原始数据如下：");
    printCollection("order");

    // 更新
    UpdateResult updateResult = mongoTemplate.updateMulti(
        Query.query(Criteria.where("_id").is(1)),
        Update.update("owner.name", "zk"),
        OrderInfo.class, "order");

    System.out.println("更新后数据如下：");
    printCollection("order");
  }

  @Test
  public void testAggregate() {
    // 生成数据
    MongoCollection<Document> hobby = mongoTemplate.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));

    // 聚合
    AggregationResults<HashMap> aggregate = mongoTemplate.aggregate(
        Aggregation.newAggregation(
            Aggregation.project("name", "hobby"),
            Aggregation.unwind("hobby"),
            Aggregation.group("hobby").count().as("countNum")
        ),
        "student",
        HashMap.class
    );
    List<HashMap> mappedResults = aggregate.getMappedResults();
    System.out.println("聚合结果：" + mappedResults);

  }

  @Test
  public void testAggregate2() {
    // 生成数据
    MongoCollection<Document> hobby = mongoTemplate.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));

    // 聚合
    AggregationResults<HashMap> aggregate = mongoTemplate.aggregate(
        Aggregation.newAggregation(
            Aggregation.project("name", "hobby").andExclude("_id"),
            Aggregation.unwind("hobby")
        ),
        "student",
        HashMap.class
    );
    List<HashMap> mappedResults = aggregate.getMappedResults();
    System.out.println("聚合结果：" + mappedResults);

  }

  @Test
  public void testAggregate3() {
    // 生成数据
    MongoCollection<Document> hobby = mongoTemplate.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));

    // 聚合
    AggregationResults<HashMap> aggregate = mongoTemplate.aggregate(
        Aggregation.newAggregation(
            Aggregation.project("name", "hobby").andExclude("_id"),
            Aggregation.skip(2),
            Aggregation.limit(2)
        ),
        "student",
        HashMap.class
    );
    List<HashMap> mappedResults = aggregate.getMappedResults();
    System.out.println("聚合结果：" + mappedResults);

  }

  // 需要借助javaScript脚本
  @Test
  public void testMapReduce() {
    // 生成数据
    MongoCollection<Document> hobby = mongoTemplate.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));

    // mapReduce()
  }

  // 打印数据库的数据
  private void printCollection(String collectionName) {
    FindIterable<Document> documents = mongoTemplate.getCollection(collectionName).find();
    System.out.println("\n================== print start ==================\n");
    MongoCursor<Document> iterator = documents.iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next());
    }
    System.out.println("\n================== print end ==================\n");
  }

  private void print(List<?> dataList) {
    System.out.println("\n================== print start ==================\n");
    if (dataList != null) {
      for (Object o : dataList) {
        System.out.println(o);
      }
    } else {
      System.out.println("无数据");
    }
    System.out.println("\n================== print end ==================\n");
  }

  @Data
  @Builder
  @ToString
//  @Document("order")// 若加上该注解，则不需要增、删、改、查时不需要指定collectionName
  private static class OrderInfo {
    @Field("_id") // 属性与mongodb中字段的关系映射
    private Integer id;
    private String item;
    private Integer price;
    private Integer quantity;
    private Date date;
    private User owner;
  }

  @Data
  @ToString
  @Builder
  private static class User {
    private String name;
    private Integer age;
  }
}
