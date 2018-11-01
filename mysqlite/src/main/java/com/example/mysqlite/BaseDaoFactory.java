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
    //private static  BaseDaoFactory instance=new BaseDaoFactory();
    private static  BaseDaoFactory instance=null;
    private String sqliteDataBasePath;
    private SQLiteDatabase sqLiteDatabase;



    private BaseDaoFactory(String mySqliteDataBasePath){
        if (mySqliteDataBasePath==null||mySqliteDataBasePath.trim().equals("")||!mySqliteDataBasePath.contains(".db")){
            sqliteDataBasePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/mysqlite.db";
        }else {
            sqliteDataBasePath= mySqliteDataBasePath;
        }
         sqLiteDatabase=SQLiteDatabase.openOrCreateDatabase(sqliteDataBasePath,null);
    }

    public static BaseDaoFactory getInstance(String sqliteDataBasePath){
        if (instance==null){
            instance=new BaseDaoFactory(sqliteDataBasePath);
        }
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
