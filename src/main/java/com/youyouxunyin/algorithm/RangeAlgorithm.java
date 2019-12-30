package com.youyouxunyin.algorithm;


import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RangeAlgorithm implements Algorithm {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String doSharding(String tableName, Object value,int length) {
        if (value!=null){
            try{
                DateUtil.parseDateTime(value.toString());
                String replace = value.toString().substring(0, 7).replace("-", "");
                String newName = tableName+"_"+replace;
                return newName;
            }catch (DateException ex){
                logger.error("日期格式不符合要求!传入参数:{},正确格式:{}",value.toString(), DatePattern.NORM_DATETIME_FORMAT);
                return tableName;
            }
        }
        return tableName;
    }

}
