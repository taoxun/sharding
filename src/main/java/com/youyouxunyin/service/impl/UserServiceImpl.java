package com.youyouxunyin.service.impl;

import com.youyouxunyin.entity.User;
import com.youyouxunyin.mapper.UserMapper;
import com.youyouxunyin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;



    @Transactional
    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public List<User> query(User user) {
        return userMapper.query(user);
    }
}
