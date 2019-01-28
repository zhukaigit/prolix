package com.zk.springbootswagger2.mongo;

import com.mongodb.client.result.UpdateResult;
import com.zk.springbootswagger2.utils.JsonUtils;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Update.Position;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoUpdateTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  /**
   * 生成测试数据
   */
  @Before
  public void before() {
    mongoTemplate.dropCollection(TestUpdateModel.class);
    TestUpdateModel model = TestUpdateModel.builder()
        .loves(new String[]{"swimming", "sing", "boxing", "boxing"})
        .updateDate(Date.from(LocalDateTime.of(2000, 1, 1, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant()))
        .maxField(100d)
        .minField(10d)
        .name("zhukai")
        .build();
    mongoTemplate.save(model);
  }

  /**
   * update api 解说
   */
  public void updateApi() {
    Class<?> entityClass = null;
    mongoTemplate.updateMulti(
        Query.query(new Criteria()),
        new Update()
            .addToSet("key", "vale") // 给键值类型为数组的key添加一个元素，如果元素已存在，则不再添加
            .addToSet("").each()  //给键值类型为数组的key添加一批量的元素，如果元素已存在，则不再添加
            .currentDate("key")  // 将键值类型为日期类型的key的值更新为当前值
            .max("key", "newValue") // 若newValue > 当前值，则更新，否则不变
            .min("key", "newValue")  // 若newValue < 当前值，则更新，否则不变
            .multiply("key", 7)  // 该key的值乘以7倍
            .pop("key", Position.FIRST)  // $pop修饰符删除数组中的第一个或者最后一个元素，给$pop传递-1会删除第一个元素传递1会删除最后一个元素
            .pull("key", "value")  // $pull修饰符会删除掉数组中符合条件的元素，只可指定一个数值
            .pullAll("key", new Object[]{}) // $pull修饰符会删除掉数组中符合条件的元素，可指定多个数值
            .push("key", "value")  // 操作符添加指定的值到数组中，不去重
            .push("key").each(new Object[]{})  // 操作符添加指定的值到数组中，不去重
            .rename("oldName", "newName") // 重命名字段名称
            .set("key", "newValue")  // $set，存在该字段，则更新其值；否则新增一个key的字段
            .setOnInsert("key", "newValue")  // TODO - ZhuKai: 2019/1/28 待确定
            .unset("key")  // 删除指定字段
        ,
        entityClass);
  }

  @Test
  public void testUnset() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .unset("loves");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testSetOnInsert1() {
    Query query = Query.query(Criteria.where("name").is("zk"));
    Update update = new Update()
            .setOnInsert("name", "zk");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testSet2() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .set("newField", "newFieldValue");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testSet1() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .set("name", "zk");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testRename() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .rename("loves", "favour");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testPushEach() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .push("loves").each(new String[]{"newValue1", "newValue2"});  // 已废弃：Use {@link #push(String) $push $each} instead.
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testPush() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .push("loves", "newValue");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testPullAll() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .pullAll("loves", new String[]{"boxing", "sing"});// 删除loves数组中值为boxing、sing的数据
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testPull() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .pull("loves", "boxing");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testPop() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .pop("loves", Position.FIRST);
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testMultiply() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .multiply("min_field", 10);  // 大于实际值，不会更新
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testMin() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .min("max_field", 99) // 小于实际值，会更新
            .min("min_field", 11);  // 大于实际值，不会更新
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testMax() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update()
            .max("max_field", 101) // 大于实际值，会更新
            .max("min_field", 9);  // 小于实际值，不会更新
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }


  @Test
  public void testCurrentDate() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update().currentDate("update_date");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testAddToSetNotExisted() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update().addToSet("loves", "reading");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testAddToSetExisted() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update().addToSet("loves", "sing");
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));
  }

  @Test
  public void testAddToSetEach() {
    Query query = Query.query(Criteria.where("name").is("zhukai"));
    Update update = new Update().addToSet("loves").each(new String[]{"reading", "basketBall"});
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, TestUpdateModel.class);
    System.err.println("更新结果：" + JsonUtils.toJsonHasNullKey(updateResult));

  }

  /**
   * 拥有类似事务特性的更新与查询操作
   *
   * db.COLLECTION_NAME.findAndModify({
   *    query:{}, update:{}, remove:true|false, new:true|false, sort:{}, fields:{}, upsert:true|false
   * });
   *
   * query是查询选择器，与findOne的查询选择器相同
   * update是要更新的值，不能与remove同时出现
   * remove表示删除符合query条件的文档，不能与update同时出现
   * new为true：返回个性后的文档，false：返回个性前的，默认是false
   * sort：排序条件，与sort函数的参数一致。
   * fields:投影操作，与find*的第二个参数一致。
   * upsert:与update的upsert参数一样。
   */
  public void findAndModifyTest() {
//    mongoTemplate.findAndModify();
  }

  @Document("test_update_model")
  @Data
  @ToString
  @Builder
  public static class TestUpdateModel {
    private String id;
    private String[] loves; // 爱好
    @Field("update_date")
    private Date updateDate;
    @Field("max_field")
    private Double maxField;
    @Field("min_field")
    private Double minField;
    @Indexed(unique = true)
    private String name;
  }


}
