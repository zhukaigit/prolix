package com.zk.springbootswagger2.mongo.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 官网：https://docs.spring.io/spring-data/mongodb/docs/2.1.4.RELEASE/reference/html/#mongo-template.type-mapping
 */
@Configuration
@PropertySource("classpath:mongo.properties")
@EnableMongoRepositories(basePackages = {"com.zk.springbootswagger2.mongo.repository"},
    mongoTemplateRef = "mongoTemplate")
public class MyMongoDBConfig {

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

//  @Bean
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

  @Bean
  public MongoDbFactory mongoDbFactory() {
    return new SimpleMongoDbFactory(mongoClient(), db);
  }

  @Bean("mongoTemplate")
  public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
    return new MongoTemplate(mongoDbFactory);
  }

}
