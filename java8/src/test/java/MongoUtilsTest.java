import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.Converter;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonWriter;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * MongoUtils
 *
 * @author zrj
 * @since 2022/3/29
 **/
public class MongoUtilsTest {


    final String dbName = "castle_zeus";
    final String rule_exec_log_table = "rule_exec_log";

    private static MongoClient getMongoClient() {
        String mongoURI = "mongodb://castle_zeus_tst:castle_zeus_tst_881719@s-tj7c9d689dce7d94883.mongodb.rds.aliyuncs.com:3717,s-tj733095c8ea4e94234.mongodb.rds.aliyuncs.com:3717/castle_zeus";
        MongoClientOptions.Builder builder = MongoClientOptions.builder()
                .connectTimeout(5000);
        MongoClientURI uri = new MongoClientURI(mongoURI, builder);
        return new MongoClient(uri);
    }

    /**
     * 创建集合
     */
    @Test
    public void createCollection() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = getMongoClient();
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
            System.out.println("Connect to database successfully");
            mongoDatabase.createCollection("test");

            System.out.println("test集合创建成功");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 删除集合
     */
    @Test
    public void deleteCollection() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = getMongoClient();
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            collection.drop();

            System.out.println("test集合删除成功");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 获取集合
     */
    @Test
    public void getCollection() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = getMongoClient();
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功，collection：" + collection);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 插入文档
     */
    @Test
    public void insertMany() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = getMongoClient();
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");


            //插入文档
            //1. 创建文档 org.bson.Document 参数为key-value的格式
            //2. 创建文档集合List<Document>
            //3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)

            Document document = new Document("title", "MongoDB")
                    .append("description", "database")
                    .append("likes", 100)
                    .append("by", "Fly6")
                    .append("loves", "")
                    .append("person", new Document("name", "zk").append("age", 12))
                    .append("time", new Date());
            List<Document> documents = new ArrayList<>();
            documents.add(document);
            collection.insertMany(documents);
            System.out.println("文档插入成功");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 更新文档
     */
    @Test
    public void updateMany() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = getMongoClient();
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            //更新文档   将文档中likes=100的文档修改为likes=200
            collection.updateMany(Filters.eq("likes", 100), new Document("$set", new Document("likes", 200)));
            //检索查看结果
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            System.out.println("获取游标成功，mongoCursor：" + mongoCursor);
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }
            System.out.println("更新文档完成");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 删除文档
     */
    @Test
    public void findOneRemove() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = getMongoClient();
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
            System.out.println("Connect to database successfully");
            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            //删除符合条件的第一个文档
            collection.deleteOne(Filters.eq("likes", 200));
            //删除所有符合条件的文档
            collection.deleteMany(Filters.eq("likes", 200));
            //检索查看结果
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            System.out.println("获取游标成功，mongoCursor：" + mongoCursor);
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }
            System.out.println("删除文档完成");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    public void testFind_all() {
        commonFind(null, "test");
    }

    @Test
    public void testFind_page() {
        commonFind(null, null, 0, 1, rule_exec_log_table);
    }

    @Test
    public void testFind_sort() {
        Bson sort = Filters.eq("time", 1);// 排序：-1:降序；1：升序
        commonFind(null, sort, null, null, "test");
    }

    @Test
    public void testFindRegex() {
        Bson condition = Filters.and(Filters.regex("by", "fly[1,2]", "$i"));// option：$i标识忽略大小写，不传则默认关注大小写
        commonFind(condition, "test");
    }

    @Test
    public void testFindOr() {
        ArrayList<Bson> byFieldBson = Lists.newArrayList(
                Filters.eq("by", "Fly0"),
                Filters.eq("by", "Fly3")
        );
        Bson condition = Filters.and(Filters.eq("description", "database"),
                Filters.or(byFieldBson));// option：$i标识忽略大小写，不传则默认关注大小写
        commonFind(condition, "test");
    }

    @Test
    public void testFindNe() {
        Bson condition = Filters.and(Filters.ne("loves", null), Filters.ne("loves", ""));// option：$i标识忽略大小写，不传则默认关注大小写
        commonFind(condition, "test");
    }

    private List<Document> commonFind(Bson condition, String tableName) {
        return commonFind(condition, null, null, null, tableName);
    }

    private List<Document> commonFind(Bson condition, Bson sort, Integer start, Integer limit, String tableName) {
        // 连接到 mongodb 服务
        MongoClient mongoClient = getMongoClient();
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        System.out.println("Connect to database successfully");
        MongoCollection<Document> collection = mongoDatabase.getCollection(tableName);
        System.out.println("集合 test 选择成功");

        // 打印查询条件参数
        System.out.println("\r\n================ condition start ===================");
        StringBuilder conditionStr = new StringBuilder("查询条件:");
        if (condition == null) {
            conditionStr.append("【condition = {}").append("】");
        } else {
            conditionStr.append("【condition = ").append(bsonToString(condition)).append("】");
        }
        if (sort != null) {
            conditionStr.append("【sort = ").append(bsonToString(sort)).append("】");
        }
        if (start != null) {
            conditionStr.append("【start = ").append(start).append(", limit = ").append(limit).append("】");
        }
        System.out.println(conditionStr);
        System.out.println("================ condition end ===================\n");

        // 执行查询
        FindIterable<Document> findIterable = condition == null ? collection.find() : collection.find(condition);
        if (sort != null) {
            findIterable.sort(sort);// 排序：如Bson sort = Filters.eq("time", 1);// 排序：-1:降序；1：升序
        }
        if (start != null) {
            findIterable.skip(start);// 分页
        }
        if (limit != null) {
            findIterable.limit(limit);// 分页
        }

        // 结果输出
        List<Document> result = new ArrayList<>();
        MongoCursor<Document> iterator = findIterable.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            Document document = iterator.next();
            System.out.println("index = " + count + ", 查询结果：" + Bson2JsonUtils.toJson(document));
            result.add(document);
        }
        System.out.println("结果条数：" + count);
        return result;
    }

    private static String bsonToString(Bson bson) {
        if (bson == null) {
            return "";
        }
        BsonDocument bsonDocument = bson.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
        return bsonDocument.toJson();
    }

    @Test
    public void testFind3() throws ParseException {
        // 连接到 mongodb 服务
        MongoClient mongoClient = getMongoClient();
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        System.out.println("Connect to database successfully");
        MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        System.out.println("集合 test 选择成功");

        Bson time = Filters.and(Filters.gt("time", DateUtils.parseDate("2022-09-16 22:33:57", "yyyy-MM-dd HH:mm:ss")),
                Filters.lt("time", DateUtils.parseDate("2022-09-22 22:33:57", "yyyy-MM-dd HH:mm:ss")));
//        Bson time = Filters.and();
        Bson condition = Filters.and(Filters.eq("title", "MongoDB"), time);


        System.out.println("================ condition start ===================");
        BsonDocument bsonDocument = condition.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
        System.out.println("查询条件: " + bsonDocument.toJson());
        System.out.println("================  ===================");

        FindIterable<Document> findIterable = collection.find(condition);

        MongoCursor<Document> iterator = findIterable.iterator();


        while (iterator.hasNext()) {
            Document document = iterator.next();
            System.out.println("查询结果：" + document.toJson());
        }
    }

    public static <T> List<T> convertList(List<Document> documentList, Class<T> tClass) {
        List<T> list = new ArrayList<>();
        for (Document document : documentList) {
            String s = Bson2JsonUtils.toJson(document);
            list.add(JSONObject.parseObject(s, tClass));
        }
        return list;
    }

    @Data
    public static class RuleExecLog {
        private String traceId;
        /**
         * 调用方名称
         */
        private String caller;
        private String plateNo;
        private String frameNo;
        private String paOrgCode;
        private String applyNo;
        private String policyNo;
        private String quoteId;

        private String zaFlowId;

        private String env;
        private String ruleVersion;
        private Date time;
        /**
         * 碰撞到的规则id
         */
        private List<String> hitRuleIds;
        private Map<String, Object> factor;
        private Map<String, Object> result;

    }


    @Test
    public void testFind2() {
        Bson condition = Filters.and(
                Filters.in("traceId", "162493_78351fd1d5264241", "78351fd1d5264241"),
                Filters.eq("ruleVersion", "offer_rule_current_20220907194756")
        );// option：$i标识忽略大小写，不传则默认关注大小写
        List<Document> documentList = commonFind(condition, "rule_exec_log");
        List<RuleExecLog> ruleExecLogs = convertList(documentList, RuleExecLog.class);
        int index = 1;
        for (RuleExecLog ruleExecLog : ruleExecLogs) {
            System.out.println("index = " + index++ + ", 【查询结果】：" + JSONObject.toJSONString(ruleExecLog));

        }

    }

    // ============================================
    //             Bson2JsonUtils - 开始
    // ============================================



    public static class Bson2JsonUtils {
        public static String toJson(Document document) {
            JsonWriterSettings build = JsonWriterSettings.builder()
                    .outputMode(JsonMode.EXTENDED)
                    .int64Converter((Long value, StrictJsonWriter writer) -> writer.writeString(Long.toString(value)))
                    .int32Converter((Integer value, StrictJsonWriter writer) -> writer.writeNumber(Integer.toString(value)))
                    .doubleConverter((Double value, StrictJsonWriter writer) -> writer.writeNumber(Double.toString(value)))
                    .build();
            String str = document.toJson(build);

            JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder()
                    .outputMode(JsonMode.EXTENDED)
                    .objectIdConverter((ObjectId id, StrictJsonWriter writer) -> writer.writeString(id.toString()))
                    .dateTimeConverter((Long value, StrictJsonWriter writer) -> writer.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault())
                            .format(Instant.ofEpochMilli(value))))
                    .timestampConverter((BsonTimestamp value, StrictJsonWriter writer) -> writer.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault())
                            .format(Instant.ofEpochMilli(value.getValue()))))
                    .decimal128Converter((Decimal128 value, StrictJsonWriter writer) -> writer.writeString(value.toString()))
                    .int64Converter((Long value, StrictJsonWriter writer) -> writer.writeString(Long.toString(value)))
                    .int32Converter((Integer value, StrictJsonWriter writer) -> writer.writeNumber(Integer.toString(value)))
                    .doubleConverter((Double value, StrictJsonWriter writer) -> writer.writeNumber(Double.toString(value)))
                    .booleanConverter((Boolean value, StrictJsonWriter writer) -> writer.writeBoolean(value))
                    .build();

            return document.toJson(jsonWriterSettings);
        }

    }


// ============================================
//            Bson2JsonUtils - 结束
// ============================================
}