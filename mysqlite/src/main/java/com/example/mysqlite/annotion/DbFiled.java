package com.example.mysqlite.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PROJECT_NAME:MyApplication
 * PACKAGE_NAME:com.example.mysqlite.annotion
 * USER:Frank
 * DATE:2018/10/29
 * TIME:9:23
 * DAY_NAME_FULL:星期一
 * DESCRIPTION:On the description and function of the document
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbFiled {
    String value();
}
