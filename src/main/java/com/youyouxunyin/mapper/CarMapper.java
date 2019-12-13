package com.youyouxunyin.mapper;

import com.youyouxunyin.entity.Car;

import java.util.List;
import java.util.Map;

public interface CarMapper {

    void insert(Car car);

    void update_1(Car car);

    void update_2(Map<String, String> param);

    List<Car> query(Car car);

    List<Car> query_2(Car car);

    List<Car> query_3(Map<String, Object> map);
}
