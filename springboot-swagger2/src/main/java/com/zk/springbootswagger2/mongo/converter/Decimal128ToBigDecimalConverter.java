package com.zk.springbootswagger2.mongo.converter;


import java.math.BigDecimal;
import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {

  @Override
  public BigDecimal convert(Decimal128 source) {
    return source.bigDecimalValue();
  }

}
