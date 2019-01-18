package com.zk.mongo;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

public class MongoTest {

  private static final String databaseName = "zk";
  private static final String collectionName = "col";
  private static final MongoDatabase db = getDatabase("localhost", 12345, databaseName);
  private static final MongoCollection<Document> col = db.getCollection(collectionName);

  // 创建集合
  @Test
  public void testCreateCollection() {
    MongoDatabase db = getDatabase("localhost", 12345, databaseName);
    db.createCollection(collectionName);
  }

  // 测试数据
  @Test
  public void testInsertDocument() {
    // 删除所有数据
    col.deleteMany(new Document());
    // 插入
    Document d1 = new Document()
        .append("_id", 1).append("item", "abc").append("price", 10).append("quantity", 2)
        .append("date", new Date());
    Document d2 = new Document()
        .append("_id", 2).append("item", "jkl").append("price", 20).append("quantity", 1)
        .append("date", new Date());
    Document d3 = new Document()
        .append("_id", 3).append("item", "xyz").append("price", 5).append("quantity", 10)
        .append("date", new Date());
    Document d4 = new Document()
        .append("_id", 4).append("item", "abc").append("price", 10).append("quantity", 20)
        .append("date", new Date());
    Document d5 = new Document()
        .append("_id", 5).append("item", "abc").append("price", 15).append("quantity", 10)
        .append("date", new Date());
    Document d6 = new Document()
        .append("_id", 6).append("item", "jkl").append("price", 5).append("quantity", 3)
        .append("date", new Date());
    ArrayList<Document> list = new ArrayList<>();
    list.add(d1);
    list.add(d2);
    list.add(d3);
    list.add(d4);
    list.add(d5);
    list.add(d6);
    col.insertMany(list);
  }

  // ========================  查询 ========================

  // =========  通过Document查询条件 =========
  @Test
  public void testQuery1() {
    Document document = new Document("item", "abc");
    FindIterable<Document> documents = col.find(document);
    print(documents);
  }

  @Test
  public void testQuery1_1() {
    FindIterable<Document> documents = col.find(Filters.eq("item", "abc"));
    print(documents);
  }

  // 使用运算符“$lt”,"$gt","$lte","$gte"
  @Test
  public void testQuery2() {
    Document queryCondition = new Document("price", new Document("$gte", 10));
    FindIterable<Document> documents = col.find(queryCondition);
    print(documents);
  }

  @Test
  public void testQuery2_1() {
    FindIterable<Document> documents = col.find(Filters.gte("price", 10));
    print(documents);
  }

  // and连接多个条件
  @Test
  public void testQuery3() {
    Document query = new Document("item", "jkl").append("price", new Document("$gt", 10));
    print(col.find(query));
  }

  @Test
  public void testQuery3_1() {
    Bson query = Filters.and(Filters.eq("item", "jkl"), Filters.gt("price", 10));
    print(col.find(query));
  }

  // or连接多个条件
  @Test
  public void testQuery4() {
    Document query = new Document("$or",
        Lists.newArrayList(new Document("item", "jkl"), new Document("item", "xyz")));
    print(col.find(query));
  }

  @Test
  public void testQuery4_1() {
    Bson query = Filters.or(Filters.eq("item", "jkl"), Filters.eq("item", "xyz"));
    print(col.find(query));
  }

  @Test
  public void testQuery4_2() {
    Bson query = Filters.in("item", Lists.newArrayList("jkl", "xyz"));
    print(col.find(query));
  }

  // or and 联合连接多个条件
  @Test
  public void testQuery5() {
    Document query = new Document("price", 5)
        .append("$or",
            Lists.newArrayList(new Document("item", "jkl"), new Document("item", "xyz")));// and
    print(col.find(query));
  }

  @Test
  public void testQuery5_1() {
    Bson query = Filters.and(
        Filters.eq("price", 5),
        Filters.or(Filters.eq("item", "jkl"), Filters.eq("item", "xyz"))
    );
    print(col.find(query));
  }

  // between ... and 条件
  @Test
  public void testQuery6() {
    // 查询price > 5 且 price < 20的记录
    Document query = new Document("price", new Document("$gt", 5).append("$lt", 20));
    // 下面为错误的写法，同样的key，后面的会将前面的覆盖
//    Document query = new Document("price", new Document("$gt", 5)).append("price", new Document("$lt", 20));
    print(col.find(query));
  }

  @Test
  public void testQuery6_1() {
    // 查询price > 5 且 price < 20的记录
    Bson query = Filters.and(Filters.gt("price", 5), Filters.lt("price", 20));
    print(col.find(query));
  }

  // 计数
  @Test
  public void testCount() {
    long countDocuments = col.countDocuments();
    System.out.println(countDocuments);
  }

  // 升、降序
  @Test
  public void testSort1() {
    System.out.println("============== 升序 ==============");
    print(col.find().sort(Sorts.ascending("price")));
    System.out.println("============== 降序 ==============");
    print(col.find().sort(Sorts.descending("price")));
  }

  // 多字段排序
  @Test
  public void testSort2() {
    // price按照升序，quantity按照降序
    print(col.find().sort(Sorts.orderBy(Sorts.ascending("price"), Sorts.descending("quantity"))));
  }

  @Test
  public void testSkipAndLimit() {
    print(col.find().skip(2).limit(2));
  }

  @Test
  public void testDistinct() {
    DistinctIterable<Integer> distinct = col
        .distinct("price", Filters.eq("item", "abc"), Integer.class);
    print(distinct);
  }

  @Test
  public void testUpdate() {
    System.out.println("============= 原始数据 =============");
    print(col.find());

    System.out.println("============= 修改后数据 =============");
    col.updateMany(
        Filters.eq("price", 5),
        new Document("$set", new Document("price", 6))
    );
    print(col.find());

    System.out.println("============= 回复后数据 =============");
    col.updateMany(
        Filters.eq("price", 6),
        new Document("$set", new Document("price", 5))
    );
    print(col.find());
  }

  @Test
  public void testAggregateProject() {
    MongoClient mongo = new MongoClient("localhost", 12345);
    MongoDatabase db = mongo.getDatabase("test");
    MongoCollection<Document> hobby = db.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));
    AggregateIterable<Document> aggregate = hobby.aggregate(
        Arrays.asList(
            // 只有_id默认输出的，不想输出该字段，需要设置为0，需要输出的其他字段需要手动设置为1.
            // $project : 对流入的每个document进行投影操作，类似于select field1, field2, ...
            new Document("$project", new Document("name", 1).append("_id", 0).append("hobby", 1))
        )
    );
    print(aggregate);
  }

  /**
   * $match
   *
   * 类似于where字句
   */
  @Test
  public void testAggregateProjectMatch() {
    MongoClient mongo = new MongoClient("localhost", 12345);
    MongoDatabase db = mongo.getDatabase("test");
    MongoCollection<Document> hobby = db.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));
    AggregateIterable<Document> aggregate = hobby.aggregate(
        Arrays.asList(
            // 只有_id默认输出的，不想输出该字段，需要设置为0，需要输出的其他字段需要手动设置为1.
            // $project : 对流入的每个document进行投影操作，类似于select field1, field2, ...
            new Document("$project", new Document("name", 1).append("_id", 0).append("hobby", 1)),
            new Document("$match", new Document("name", "csc"))
        )
    );
    print(aggregate);
  }

  @Test
  public void testAggregateProjectLimitSkip() {
    MongoClient mongo = new MongoClient("localhost", 12345);
    MongoDatabase db = mongo.getDatabase("test");
    MongoCollection<Document> hobby = db.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));
    AggregateIterable<Document> aggregate = hobby.aggregate(
        Arrays.asList(
            // 只有_id默认输出的，不想输出该字段，需要设置为0，需要输出的其他字段需要手动设置为1.
            // $project : 对流入的每个document进行投影操作，类似于select field1, field2, ...
            new Document("$project", new Document("name", 1).append("_id", 0).append("hobby", 1)),
            new Document("$skip", 2),
            new Document("$limit", 2)
        )
    );
    print(aggregate);
  }

  /**
   * $unwind
   *
   * 把数组中的元素拆分为多个document。
   */
  @Test
  public void testAggregateProjectUnwind() {
    MongoClient mongo = new MongoClient("localhost", 12345);
    MongoDatabase db = mongo.getDatabase("test");
    MongoCollection<Document> hobby = db.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));
    AggregateIterable<Document> aggregate = hobby.aggregate(
        Arrays.asList(
            // 只有_id默认输出的，不想输出该字段，需要设置为0，需要输出的其他字段需要手动设置为1.
            // $project : 对流入的每个document进行投影操作，类似于select field1, field2, ...
            new Document("$project", new Document("name", 1).append("_id", 0).append("hobby", 1)),
            new Document("$unwind", "$hobby")
        )
    );
    print(aggregate);
  }

  /**
   * $group
   *
   * 类似于group by，可以使用各种聚合操作符，常用的有：
   * $sum, $avg, $max, $min, $first, $last
   */
  @Test
  public void testAggregateProjectUnwindGroup() {
    MongoClient mongo = new MongoClient("localhost", 12345);
    MongoDatabase db = mongo.getDatabase("test");
    MongoCollection<Document> hobby = db.getCollection("student");
    hobby.deleteMany(new Document());
    hobby.insertOne(new Document("name", "csc").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "nicky").append("hobby", Arrays.asList("game")));
    hobby.insertOne(new Document("name", "jack").append("hobby", Arrays.asList("movie")));
    hobby.insertOne(new Document("name", "tom").append("hobby", Arrays.asList("reading", "coding")));
    hobby.insertOne(new Document("name", "lucy").append("hobby", Arrays.asList("reading", "football")));
    hobby.insertOne(new Document("name", "lion").append("hobby", Arrays.asList("basketball", "football")));
    // 需求：计算每个hobby的人数
    AggregateIterable<Document> aggregate = hobby.aggregate(
        Arrays.asList(
            // 只有_id默认输出的，不想输出该字段，需要设置为0，需要输出的其他字段需要手动设置为1.
            // $project : 对流入的每个document进行投影操作，类似于select field1, field2, ...
            new Document("$project", new Document("name", 1).append("_id", 0).append("hobby", 1)),
            new Document("$unwind", "$hobby"),
            new Document("$group",
                new Document("_id", "$hobby").append("count", new Document("$sum", 1)))
        )
    );
    print(aggregate);
  }

  @Test
  public void testMapReduce() {

  }

  private <T> void print(MongoIterable<T> iterable) {
    if (iterable == null) {
      return;
    }
    MongoCursor<T> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      T t = iterator.next();
      if (t instanceof Document) {
        System.out.println(((Document) t).toJson());
      } else {
        System.out.println(t);
      }
    }
  }

  private static MongoDatabase getDatabase(String host, int port, String databaseName) {
    MongoClient mongoClient = new MongoClient(host, port);
    return mongoClient.getDatabase(databaseName);
  }


}
