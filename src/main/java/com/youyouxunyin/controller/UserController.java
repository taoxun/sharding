package com.youyouxunyin.controller;

import com.youyouxunyin.entity.User;
import com.youyouxunyin.service.UserService;
import com.youyouxunyin.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    RandomUtil random = new RandomUtil();

    @RequestMapping("/insert")
    public User insert() throws InterruptedException {
        int count = 100;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        long start = System.currentTimeMillis();
        for (int i =0;i<count;i++){
            new Thread(() -> {
                User user = new User(random);
                userService.insert(user);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("新增数据花费时间:"+(end-start));
        return new User();
    }

    @RequestMapping("/update")
    public void update(@RequestBody User user){
        userService.update(user);
    }

    @RequestMapping("/query")
    public List<User> query(){
        User user = new User();
        //user.setId(1205394511203667969l);
        user.setTel("151");
        return userService.query(user);
    }
}
