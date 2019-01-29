package com.zk.springbootswagger2.mongo.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.zk.springbootswagger2.mongo.converter.BigDecimalToDecimal128Converter;
import com.zk.springbootswagger2.mongo.converter.Decimal128ToBigDecimalConverter;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 官网：https://docs.spring.io/spring-data/mongodb/docs/2.1.4.RELEASE/reference/html/#mongo-template.type-mapping
 */
@Configuration
@PropertySource("classpath:mongo.properties")
@EnableMongoRepositories(basePackages = {"com.zk.springbootswagger2.mongo.repository"},
    mongoTemplateRef = "mongoTemplate")
public class MyMongoDBConfig extends AbstractMongoConfiguration {

  @Value("${mongo.config.maindb}")
  private String db;

  @Value("${mongo.config.connections.per.host}")
  private int connectionsPerHost;

  @Value("${mongo.config.threads.allowed.to.block.for.connection.multiplier}")
  private int threadsAllowedToBlockForConnectionMultiplier;

  @Value("${mongo.config.connect.timeout}")
  private int connectTimeout;

  @Value("${mongo.config.max.wait.time}")
  private int maxWaitTime;

  @Value("${mongo.config.socket.keep.alive}")
  private boolean isSocketKeepAlive;

  @Value("${mongo.config.socket.timeout}")
  private int socketTimeout;

  @Value("${mongo.config.connect.heartbeatConnectTimeout}")
  private int heartbeatConnectTimeout;

  @Value("${mongo.config.connect.heartbeatFrequency}")
  private int heartbeatFrequency;

  @Value("${mongo.config.connect.maxConnectionIdleTime}")
  private int maxConnectionIdleTime;

  @Value("${mongo.uri}")
  private String uri;

  @Override
  protected String getDatabaseName() {
    return db;
  }

  @Bean
  public MongoClient mongoClient() {
    MongoClientOptions.Builder builder = MongoClientOptions.builder()
        .connectionsPerHost(connectionsPerHost)
        .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier)
        .connectTimeout(connectTimeout)
        .heartbeatConnectTimeout(heartbeatConnectTimeout)
        .heartbeatFrequency(heartbeatFrequency)
        .maxConnectionIdleTime(maxConnectionIdleTime)
        .maxWaitTime(maxWaitTime)
//        .socketKeepAlive(isSocketKeepAlive)  // 已废弃，默认true
        .socketTimeout(socketTimeout)
        .readPreference(ReadPreference.primaryPreferred())
        .writeConcern(WriteConcern.FSYNCED);
    MongoClientURI mongoClientURI = new MongoClientURI(uri, builder);
    return new MongoClient(mongoClientURI);
  }

  @Override
  @Bean
  public MappingMongoConverter mappingMongoConverter() throws Exception {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
    MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext());
    // 添加自定义转换器
    ArrayList<Converter> list = new ArrayList<>();
    list.add(new BigDecimalToDecimal128Converter());
    list.add(new Decimal128ToBigDecimalConverter());
    converter.setCustomConversions(new MongoCustomConversions(list));
    return converter;
  }

  @Override
  @Bean("mongoTemplate")
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
  }

}
