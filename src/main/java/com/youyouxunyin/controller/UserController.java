package com.youyouxunyin.controller;

import com.youyouxunyin.entity.User;
import com.youyouxunyin.service.UserService;
import com.youyouxunyin.util.Page;
import com.youyouxunyin.util.PageContext;
import com.youyouxunyin.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    RandomUtil random = new RandomUtil();

    @RequestMapping("/insertUser")
    public User insertUser (){
        User user = new User(random);
        userService.insert(user);
        return user;
    }

    @RequestMapping("/insert")
    public User insert() throws InterruptedException {
        int count = 50;
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
    public Page<User> query(Long id,String tel,@RequestParam(value = "pageNum", required = false,defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", required = false,defaultValue = "3") int pageSize) throws InterruptedException {
        User user = new User();
        if (!StringUtils.isEmpty(id)){
            user.setId(id);
        }
        if (!StringUtils.isEmpty(tel)){
            user.setTel(tel);
        }
        PageContext.startPage(pageNum, pageSize);
        /*List list = userService.query(user);

        return list;*/
        return (Page<User>) userService.query(user);
    }
}
