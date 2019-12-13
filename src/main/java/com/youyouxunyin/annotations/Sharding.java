package com.youyouxunyin.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sharding {

    String tableName();
    String field();
    String mode();
}
