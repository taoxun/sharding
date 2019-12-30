package com.youyouxunyin.algorithm;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class AlgorithmHandler {

    private Map<String, Algorithm> algorithm = new HashMap<>();

    @PostConstruct
    public void init(){
        algorithm.put("range",new RangeAlgorithm());
        algorithm.put("hash",new HashAlgorithm());
    }

    public String handler(String mode,String name,Object value,int length){
        return algorithm.get(mode).doSharding(name, value,length);
    }
}
