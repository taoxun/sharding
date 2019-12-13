package com.youyouxunyin.mapper;

import com.youyouxunyin.entity.User;

import java.util.List;

public interface UserMapper {

    void insert(User user);
    void update(User user);
    List<User> query(User user);
}
