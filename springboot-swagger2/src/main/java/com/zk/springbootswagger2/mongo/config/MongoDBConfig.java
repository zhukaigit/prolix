package com.zk.springbootswagger2.mongo.config;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public abstract class MongoDBConfig {

  protected abstract String getDatabaseName();

  protected abstract Mongo mongo() throws Exception;

  /**
   * Creates a {@link SimpleMongoDbFactory} to be used by the {@link MongoTemplate}. Will use the
   * {@link Mongo} instance configured in {@link #mongo()}.
   *
   * @return mongodb factory
   * @see #mongo()
   */
  protected MongoDbFactory mongoDbFactory() throws Exception {
    return new SimpleMongoDbFactory((MongoClient) mongo(), getDatabaseName());
  }

  /**
   * Return the base package to scan for mapped {@link Document}s. Will return the package name of
   * the configuration class' (the concrete class, not this one here) by default. So if you have a
   * {@code com.acme.AppConfig} extending {@link AbstractMongoConfiguration} the base package will
   * be considered {@code com.acme} unless the method is overriden to implement alternate
   * behaviour.
   *
   * @return the base package to scan for mapped {@link Document} classes or {@literal null} to not
   * enable scanning for entities.
   */
  protected String getMappingBasePackage() {

    Package mappingBasePackage = getClass().getPackage();
    return mappingBasePackage == null ? null : mappingBasePackage.getName();
  }

  /**
   * Creates a {@link MongoMappingContext} equipped with entity classes scanned from the mapping
   * base package.
   *
   * @see #getMappingBasePackage()
   */
  protected MongoMappingContext mongoMappingContext() throws ClassNotFoundException {

    MongoMappingContext mappingContext = new MongoMappingContext();
    mappingContext.setInitialEntitySet(getInitialEntitySet());
    mappingContext.setSimpleTypeHolder(customConversions().getSimpleTypeHolder());
    mappingContext.setFieldNamingStrategy(fieldNamingStrategy());

    return mappingContext;
  }

  /**
   * Register custom {@link Converter}s in a {@link CustomConversions} object if required. These
   * {@link CustomConversions} will be registered with the {@link #mappingMongoConverter()} and
   * {@link #mongoMappingContext()}. Returns an empty {@link CustomConversions} instance by
   * default.
   *
   * @return must not be {@literal null}.
   */
  protected CustomConversions customConversions() {
    return new CustomConversions(Collections.emptyList());
  }

  /**
   * Creates a {@link MappingMongoConverter} using the configured {@link #mongoDbFactory()} and
   * {@link #mongoMappingContext()}. Will get {@link #customConversions()} applied.
   *
   * @see #customConversions()
   * @see #mongoMappingContext()
   * @see #mongoDbFactory()
   */
  protected MappingMongoConverter mappingMongoConverter() throws Exception {

    DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
    MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver,
        mongoMappingContext());
    converter.setCustomConversions(customConversions());

    return converter;
  }

  /**
   * Scans the mapping base package for classes annotated with {@link Document}.
   *
   * @see #getMappingBasePackage()
   */
  protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {

    String basePackage = getMappingBasePackage();
    Set<Class<?>> initialEntitySet = new HashSet<Class<?>>();

    if (StringUtils.hasText(basePackage)) {
      ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(
          false);
      componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));
      componentProvider.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));

      for (BeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {
        initialEntitySet.add(ClassUtils.forName(candidate.getBeanClassName(),
            AbstractMongoConfiguration.class.getClassLoader()));
      }
    }

    return initialEntitySet;
  }

  /**
   * Configures whether to abbreviate field names for domain objects by configuring a {@link
   * CamelCaseAbbreviatingFieldNamingStrategy} on the {@link MongoMappingContext} instance created.
   * For advanced customization needs, consider overriding {@link #mappingMongoConverter()}.
   */
  protected boolean abbreviateFieldNames() {
    return false;
  }

  /**
   * Configures a {@link FieldNamingStrategy} on the {@link MongoMappingContext} instance created.
   *
   * @since 1.5
   */
  protected FieldNamingStrategy fieldNamingStrategy() {
    return abbreviateFieldNames() ? new CamelCaseAbbreviatingFieldNamingStrategy()
        : PropertyNameFieldNamingStrategy.INSTANCE;
  }
}
