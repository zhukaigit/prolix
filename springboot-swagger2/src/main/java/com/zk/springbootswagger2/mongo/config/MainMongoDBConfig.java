package com.zk.springbootswagger2.mongo.config;

import com.google.common.collect.Lists;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
//@ImportResource(locations = "classpath:local.properties")
@EnableMongoRepositories(basePackages = {
    "com.zk.springbootswagger2.mongo.repository"}, mongoTemplateRef = "mongoTemplate")
public class MainMongoDBConfig extends MongoDBConfig {

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

//    @Autowired(required = false)
//    private MongoCommandListener mongoCommandListener;

  @Override
  protected String getDatabaseName() {
    return this.db;
  }

  @Override
  protected Mongo mongo() throws Exception {
    uri = String.format(uri, db);
    MongoClientOptions.Builder builder = MongoClientOptions.builder()
        .connectionsPerHost(connectionsPerHost)
        .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier)
        .connectTimeout(connectTimeout)
        .heartbeatConnectTimeout(heartbeatConnectTimeout)
        .heartbeatFrequency(heartbeatFrequency)
        .maxConnectionIdleTime(maxConnectionIdleTime)
        .maxWaitTime(maxWaitTime)
        .socketKeepAlive(isSocketKeepAlive)
        .socketTimeout(socketTimeout)
        .readPreference(ReadPreference.primaryPreferred())
        .writeConcern(WriteConcern.FSYNCED);
//        builder = Objects.nonNull(mongoCommandListener) ? builder.addCommandListener(mongoCommandListener) : builder;
    MongoClientURI mongoClientURI = new MongoClientURI(uri, builder);
    return new MongoClient(mongoClientURI);
  }

  @Override
  @Bean
  public CustomConversions customConversions() {
    List<Converter> converters = Lists.newArrayList();
    return new CustomConversions(converters);
  }

  @Override
  @Bean
  protected MongoMappingContext mongoMappingContext() throws ClassNotFoundException {
    return super.mongoMappingContext();
  }

  @Override
  @Bean(name = "matrixConverter")
  public MappingMongoConverter mappingMongoConverter() throws Exception {
    return super.mappingMongoConverter();
  }

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
  }

}
