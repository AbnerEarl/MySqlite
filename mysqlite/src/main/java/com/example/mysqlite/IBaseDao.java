package com.example.mysqlite;

import java.util.List;

/**
 * PROJECT_NAME:MyApplication
 * PACKAGE_NAME:com.example.mysqlite
 * USER:Frank
 * DATE:2018/10/29
 * TIME:9:16
 * DAY_NAME_FULL:星期一
 * DESCRIPTION:On the description and function of the document
 **/
public interface IBaseDao<T> {
    Long insert(T entity);
    List<T> query(T entity);
    List<T> query(T entity,String orderBy);
    List<T> query(T entity,String groupBy,String having);
    List<T> query(T entity,Integer startIndex,Integer limit);
    List<T> query(T entity,Integer startIndex,Integer limit,String orderBy);
    List<T> query(T entity,Integer startIndex,Integer limit,String groupBy,String having);
    List<T> query(T entity,Integer startIndex,Integer limit,String groupBy,String having,String orderBy);
    Long update(T entityUpdate,T entityWhere);
    Long delete(T entity);

}
