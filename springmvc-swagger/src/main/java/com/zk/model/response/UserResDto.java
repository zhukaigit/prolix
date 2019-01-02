package com.zk.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ApiModel
public class UserResDto {
    @ApiModelProperty(value = "用户名", required = true)
    private String name;
    @ApiModelProperty(value = "年龄", required = true)
    private Integer age;
    @ApiModelProperty(value = "爱好", required = true)
    private String fav;
    @ApiModelProperty(value = "薪资", required = true)
    private String salary;
    @ApiModelProperty(value = "申请时间", required = true)
    private Date applyTime;

    public UserResDto(UserResDtoBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.fav = builder.fav;
        this.salary = builder.salary;
        this.applyTime = builder.applyTime;
    }

    public static class UserResDtoBuilder {
        private String name;
        private Integer age;
        private String fav;
        private String salary;
        private Date applyTime;

        public UserResDtoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserResDtoBuilder setAge(Integer age) {
            this.age = age;
            return this;
        }

        public UserResDtoBuilder setFav(String fav) {
            this.fav = fav;
            return this;
        }

        public UserResDtoBuilder setSalary(String salary) {
            this.salary = salary;
            return this;
        }

        public UserResDtoBuilder setApplyTime(Date applyTime) {
            this.applyTime = applyTime;
            return this;
        }

        public UserResDto build() {
            return new UserResDto(this);
        }

    }
}
