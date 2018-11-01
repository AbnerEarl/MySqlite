package com.example.mysqlite;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * PROJECT_NAME:MyApplication
 * PACKAGE_NAME:com.example.mysqlite
 * USER:Frank
 * DATE:2018/10/31
 * TIME:22:10
 * DAY_NAME_FULL:星期三
 * DESCRIPTION:On the description and function of the document
 **/
public class BaseDaoFactory {
    private static final BaseDaoFactory instance=new BaseDaoFactory();
    String sqliteDataBasePath;
    SQLiteDatabase sqLiteDatabase;



    private BaseDaoFactory(){
         sqliteDataBasePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.db";
         sqLiteDatabase=SQLiteDatabase.openOrCreateDatabase(sqliteDataBasePath,null);
    }

    public static BaseDaoFactory getInstance(){
        return instance;
    }

    public synchronized <T>BaseDao<T> getBaseDao(Class<T> entityClass){
        BaseDao<T> baseDao=null;
        try {
            baseDao=BaseDao.class.newInstance();
            baseDao.init(entityClass,sqLiteDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return baseDao;
    }
}
