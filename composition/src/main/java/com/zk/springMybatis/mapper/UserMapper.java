package com.zk.springMybatis.mapper;

import com.zk.springMybatis.model.UserInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<UserInfo> {

    UserInfo findById(int id);

    int insertUserInfo(@Param("name") String name);

    int updateById(@Param("id") Long id, @Param("name") String name);

    int updateUser(UserInfo userInfo);
}
