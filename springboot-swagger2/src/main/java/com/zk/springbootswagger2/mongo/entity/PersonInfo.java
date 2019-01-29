package com.zk.springbootswagger2.mongo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@ToString
@Document("person_info")
@CompoundIndexes({
    @CompoundIndex(name = "idx_channel_name", def = "{'channel': 1, 'name': 1}")
})
public class PersonInfo {

  @Id
  @Indexed
  @JsonProperty("_id")
  private String id;
  @Indexed(unique = true)
  @Field("user_name")
  private String name;
  private String channel;
  private Date birthday;
  private String[] favourites;
  private List<Toy> toys;
  private String newField;
  private BigDecimal salary;

  // 玩具
  @Data
  @Builder
  @ToString
  public static class Toy {
    private String name;
    private double price;
  }

}
