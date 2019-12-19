package com.youyouxunyin.entity;

import com.youyouxunyin.annotations.Sharding;
import com.youyouxunyin.util.IdUtil;
import com.youyouxunyin.util.RandomUtil;
import lombok.Data;

@Data
@Sharding(tableName = "user",field = "id",mode = "hash",length = 16)
public class User {
    private Long id;
    private String name;
    private String address;
    private String tel;
    private String email;

    public User() {
    }

    public User(RandomUtil random) {
        this.id = IdUtil.getId();
        this.name = random.getChineseName();
        this.address = random.getAddress();
        this.tel = random.getTel();
        this.email = random.getEmail(6,12);
    }
}
