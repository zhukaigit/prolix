package com.zk.springbootswagger2.mongo;

import com.google.common.collect.Lists;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 *
 /**
 * $nin，即【not in】或者【not exist】
 * 1、the field value is not in the specified array or
 * 2、the field does not exist.
 *
 * $not，即【逻辑非】或者【not exist】
 * 例子：db.inventory.find( { price: { $not: { $gt: 1.99 } } } )
 * This query will return all documents that:
 * 1、the price field value is less than or equal to 1.99
 * 2、the price field does not exist
 *
 * $nor，即【$not && $not ...】
 * 例子：db.inventory.find( { $nor: [ { price: 1.99 }, { sale: true } ]  } )
 * This query will return all documents that:
 * 1、contain the price field whose value is not equal to 1.99 and contain the sale field whose value is not equal to true or
 * 2、contain the price field whose value is not equal to 1.99 but do not contain the sale field
 * 3、do not contain the price field but contain the sale field whose value is not equal to true
 * 4、do not contain the price field and do not contain the sale field
 *
 * $ne：即【not equal】或则【not exist】：
 * 1、selects the documents where the value of the field is not equal to the specified value.
 * 2、the field does not exist.
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoQueryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 生成测试数据
     */
    @Before
    public void before() {
        mongoTemplate.dropCollection(TestQueryModel.class);
        TestQueryModel m1 = TestQueryModel.builder()
                .name("zk1")
                .results(Lists.newArrayList(
                        Result.builder().product("abc").score(10).build(),
                        Result.builder().product("xyz").score(5).build()
                ))
                .resultsTwo(Lists.newArrayList( 82, 85, 88))
                .build();
        TestQueryModel m2 = TestQueryModel.builder()
                .name("zk2")
                .results(Lists.newArrayList(
                        Result.builder().product("abc").score(8).build(),
                        Result.builder().product("xyz").score(7).build()
                ))
                .resultsTwo(Lists.newArrayList( 75, 88, 89))
                .build();
        TestQueryModel m3 = TestQueryModel.builder()
                .name("zk3")
                .results(Lists.newArrayList(
                        Result.builder().product("abc").score(7).build(),
                        Result.builder().product("xyz").score(8).build()
                ))
                .resultsTwo(Lists.newArrayList( 66, 70, 109))
                .build();

        mongoTemplate.insertAll(Lists.newArrayList(m1, m2, m3));
    }

    /**
     * 健值类型为数组的查询：$elemMatch
     *
     * The $elemMatch operator matches documents that contain an array field with
     * at least one element that matches all the specified query criteria.
     *
     * 查询同时满足 results.product = "xyz" and results.score >= 8 的记录
     */
    @Test
    public void testElemMatch1() {
        Criteria criteria = Criteria.where("product").is("xyz").and("score").gte(8);
        Query query = Query.query(
                new Criteria("results")
                        .elemMatch(criteria)
        );
        List<TestQueryModel> result = mongoTemplate.find(query, TestQueryModel.class);
        System.err.println("查询结果：" + JsonUtils.toJsonHasNullKey(result));
    }

    /**
     * 健值类型为数组的查询：$elemMatch
     *
     * 查询result_two数组中"至少"存在一个【数据x】同时满足：【数据x】> 80 且 【数据x】< 85
     */
    @Test
    public void testElemMatch2() {
        Criteria criteria = new Criteria().gt(80).lt(85);
        Query query = Query.query(
                new Criteria("results_two")
                        .elemMatch(criteria)
        );
        List<TestQueryModel> result = mongoTemplate.find(query, TestQueryModel.class);
        System.err.println("查询结果：" + JsonUtils.toJsonHasNullKey(result));
    }

    /**
     * $ne：即not equal：
     * 1、selects the documents where the value of the field is not equal to the specified value.
     * 2、the field does not exist.
     */
    @Test
    public void testNe() {
        Query query = Query.query(
                new Criteria("name").ne("zk2")
        );
        List<TestQueryModel> result = mongoTemplate.find(query, TestQueryModel.class);
        System.err.println("查询结果：" + JsonUtils.toJsonHasNullKey(result));
    }

    @Document("test_query_model")
    @Data
    @ToString
    @Builder
    private static class TestQueryModel {
        private String id;
        @Indexed(unique = true)
        private String name;
        private List<Result> results;
        @Field("results_two")
        private List<Integer> resultsTwo;
    }

    @Data
    @ToString
    @Builder
    private static class Result {
        private String product;
        private int score;

    }

}
