package com.youyouxunyin.controller;

import com.github.pagehelper.PageHelper;
import com.youyouxunyin.entity.Car;
import com.youyouxunyin.service.CarService;
import com.youyouxunyin.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    CarService carService;

    RandomUtil random = new RandomUtil();

    @RequestMapping("/insert")
    public Car insert() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int k=0;k<10;k++){
            new Thread(() -> {
                for (int i=0;i<10;i++){
                    Car car = new Car(random);
                    carService.insert(car);
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        return null;
    }

    @RequestMapping("/update_1")
    public Car update_1(){
        Car car = new Car();
        car.setCreatTime("2019-08-10 16:36:19");
        car.setId(1205041548211785729L);
        car.setBrand("奥迪");
        carService.update_1(car);
        return car;
    }
    @RequestMapping("/update_2")
    public Map update_2(){
        Map<String,String> param = new HashMap<>();
        param.put("creatTime","2019-08-10 16:36:19");
        param.put("id","1205041548211785729");
        param.put("brand","劳斯莱斯");
        carService.update_2(param);
        return param;
    }

    @RequestMapping("/query")
    public List<Car> query(){
        Car car = new Car();
        car.setCreatTime("2019-08-10 16:36:19");
        car.setId(1205041548211785729L);
        return carService.query(car);
    }
    @RequestMapping("/query_2")
    public List<Car> query_2(){
        Car car = new Car();
        car.setCreatTime("2019-01-10 16:36:19");
        return carService.query_2(car);
    }
    @RequestMapping("/query_3")
    public List<Car> query_3(){

        PageHelper.offsetPage(1, 3);
        Car car = new Car();
        car.setCreatTime("2019-02-10 16:36:19");
        Map<String, Object> map = new HashMap<>();
        //map.put("creatTime","2019-02-10 16:36:19");
        map.put("sharding",car);
        return carService.query_3(map);
    }
}
