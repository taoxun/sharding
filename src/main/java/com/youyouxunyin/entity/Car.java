package com.youyouxunyin.entity;

import com.youyouxunyin.annotations.Sharding;
import com.youyouxunyin.util.IdUtil;
import com.youyouxunyin.util.RandomUtil;
import lombok.Data;

@Data
@Sharding(tableName = "car",field = "creatTime",mode = "date")
public class Car {
    private long id;
    private String number;
    private String brand;
    private String creatTime;
    private long userId;

    public Car() {
    }

    public Car(RandomUtil random) {
        this.id = IdUtil.getId();
        this.number = random.getCarNumber();
        this.brand = random.getCarBrand();
        String time = random.nextTime();
        this.creatTime = time.replace(time.substring(0,4),"2019");
        this.userId = IdUtil.getId();
    }
}
