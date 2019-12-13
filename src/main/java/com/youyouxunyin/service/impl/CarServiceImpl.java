package com.youyouxunyin.service.impl;

import com.youyouxunyin.entity.Car;
import com.youyouxunyin.mapper.CarMapper;
import com.youyouxunyin.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarMapper carMapper;

    @Override
    public void insert(Car car) {
        carMapper.insert(car);
    }

    @Override
    public void update_1(Car car) {
        carMapper.update_1(car);
    }

    @Override
    public void update_2(Map<String, String> param) {
        carMapper.update_2(param);
    }

    @Override
    public List<Car> query(Car car) {
        return carMapper.query(car);
    }

    @Override
    public List<Car> query_2(Car car) {
        return carMapper.query_2(car);
    }

    @Override
    public List<Car> query_3(Map<String,Object> map) {
        return carMapper.query_3(map);
    }
}
