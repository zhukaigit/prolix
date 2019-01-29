package com.zk.springbootswagger2.mongo.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {

  @Override
  public Decimal128 convert(BigDecimal source) {
    int scale = source.scale() > 6 ? 6 : source.scale();
    return new Decimal128(source.setScale(scale, RoundingMode.HALF_UP));
  }
}
