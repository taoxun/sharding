package com.youyouxunyin.service;

import com.youyouxunyin.entity.User;

import java.util.List;

public interface UserService {
    void insert(User user);
    void update(User user);
    List<User> query(User user);
}
