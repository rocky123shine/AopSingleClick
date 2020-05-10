package com.example.aopsingleclick.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用户行为
@Target(ElementType.METHOD) //目标 作用在方法之上
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleClickBehavior {
    int value()default 200;
}
