package com.example.mysqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mysqlite.annotion.DbFiled;
import com.example.mysqlite.annotion.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PROJECT_NAME:MyApplication
 * PACKAGE_NAME:com.example.mysqlite
 * USER:Frank
 * DATE:2018/10/29
 * TIME:9:26
 * DAY_NAME_FULL:星期一
 * DESCRIPTION:On the description and function of the document
 **/
public class BaseDao<T> implements IBaseDao<T> {
    private SQLiteDatabase database;
    private Class<T> entityClass;
    private String tableName;
    private boolean isInit=false;
    private HashMap<String,Field> cacheMap;

    protected synchronized boolean init(Class<T> entity,SQLiteDatabase sqLiteDatabase){
        if (!isInit){
            entityClass=entity;
            database=sqLiteDatabase;
            tableName=entity.getAnnotation(DbTable.class).value();
            if (!sqLiteDatabase.isOpen()){
                return false;
            }
            if (!autoCreateTable()){
                return false;
            }
            isInit=true;
        }
        initCacheMap();
        return isInit;
    }

    private void initCacheMap() {
        //查一次空表，做缓存
        cacheMap=new HashMap<>();
        String sql="select * from "+this.tableName+" limit 1,0";
        Cursor cursor=database.rawQuery(sql,null);
        //得到字段名数组
        String[] coloumNames=cursor.getColumnNames();
        Field[]columnFields=entityClass.getDeclaredFields();
        for (String columnName:coloumNames){

            Field resultField=null;
            for (Field field:columnFields){
                if (columnName.equals(field.getAnnotation(DbFiled.class).value())){
                    resultField=field;
                    break;
                }
            }
            if (resultField!=null){
                cacheMap.put(columnName,resultField);
            }
        }
        cursor.close();

    }

    private boolean autoCreateTable() {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("CREATE TABLE IF NOT EXISTS ");
        stringBuffer.append(tableName+" (");
        Field[] fields=entityClass.getDeclaredFields();
        for (Field field:fields ){
            Class type=field.getType();
            if (type==String.class){
                stringBuffer.append(field.getAnnotation(DbFiled.class).value()+" TEXT,");
            }else if (type==Double.class){
                stringBuffer.append(field.getAnnotation(DbFiled.class).value()+" DOUBLE,");
            }else if (type==Integer.class){
                stringBuffer.append(field.getAnnotation(DbFiled.class).value()+" INTEGER,");
            }else if (type==Long.class){
                stringBuffer.append(field.getAnnotation(DbFiled.class).value()+" BIGINT,");
            }else if (type==byte[].class){
                stringBuffer.append(field.getAnnotation(DbFiled.class).value()+" BLOB,");
            }else if (type==String.class){
                stringBuffer.append(field.getAnnotation(DbFiled.class).value()+" TEXT,");
            }else {
                continue;
            }
        }
        if (stringBuffer.charAt(stringBuffer.length()-1)==','){
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
        }
        stringBuffer.append(")");
        try {
            Log.i("CreateTable",stringBuffer.toString());
            this.database.execSQL(stringBuffer.toString());
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Long insert(T entity) {
        ContentValues contentValues=getValuesByInsert(entity);
        long result=database.insert(tableName,null,contentValues);
        return result;
    }

    @Override
    public List<T> query(T entity) {
        return queryData(entity,null,null,null,null,null);
    }

    @Override
    public List<T> query(T entity, String orderBy) {
        return queryData(entity,null,null,null,null,orderBy);
    }

    @Override
    public List<T> query(T entity, String groupBy, String having) {
        return queryData(entity,null,null,groupBy,having,null);
    }

    @Override
    public List<T> query(T entity, Integer startIndex, Integer limit) {
        return queryData(entity,startIndex,limit,null,null,null);
    }

    @Override
    public List<T> query(T entity, Integer startIndex, Integer limit, String orderBy) {
        return queryData(entity,startIndex,limit,null,null,orderBy);
    }

    @Override
    public List<T> query(T entity, Integer startIndex, Integer limit, String groupBy, String having) {
        return queryData(entity,startIndex,limit,groupBy,having,null);
    }

    @Override
    public List<T> query(T entity, Integer startIndex, Integer limit, String groupBy, String having, String orderBy) {
        return queryData(entity,startIndex,limit,groupBy,having,orderBy);
    }

    @Override
    public Long update(T entityUpdate, T entityWhere) {
        ContentValues contentValues=getValuesByUpdate(entityUpdate);
        Condition condition=new Condition(getContentValues(entityWhere));
        long result=database.update(tableName,contentValues,condition.getWhereClause(),condition.getWhereArgs());
        return result;
    }

    @Override
    public Long delete(T entity) {
        //ContentValues contentValues=getValues(entity);
        Condition condition=new Condition(getContentValues(entity));
        long result=database.delete(tableName,condition.getWhereClause(),condition.getWhereArgs());
        return result;
    }

    private List<T> queryData(T where,Integer startIndex,Integer limit,String groupBy,String having,String orderBy){
        String limitString=null;
        if (startIndex!=null&&limit!=null){
            limitString=startIndex+","+limit;
        }
        Condition condition=new Condition(getContentValues(where));
        Cursor cursor=null;
        List<T> result=new ArrayList<>();
        try{
            cursor=database.query(tableName,null,condition.getWhereClause(),condition.getWhereArgs(),groupBy,having,orderBy,limitString);
            if (cursor!=null){
                result=getResult(cursor,where);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return result;
    }

    private ContentValues getContentValues(T entity) {
        ContentValues contentValues=new ContentValues();
        try{
            for (Map.Entry<String,Field> me:cacheMap.entrySet()){
                if (me.getValue().get(entity)==null){
                    continue;
                }
                contentValues.put(me.getKey(),me.getValue().get(entity).toString());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return contentValues;
    }

    private ContentValues getValuesByInsert(T entity) {
        ContentValues contentValues=new ContentValues();
        Iterator<Map.Entry<String ,Field>> iterator=cacheMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String ,Field> fieldEntry=iterator.next();
            Field field=fieldEntry.getValue();
            String key=fieldEntry.getKey();
            field.setAccessible(true);
            try {
                Object object=field.get(entity);
                Class type=field.getType();
                if (type==String.class){
                    String value= (String) object;
                    contentValues.put(key,value);
                }else if (type==Double.class){
                    Double value= (Double) object;
                    contentValues.put(key,value);
                }else if (type==Integer.class){
                    Integer value= (Integer) object;
                    contentValues.put(key,value);
                }else if (type==Long.class){
                    Long value= (Long) object;
                    contentValues.put(key,value);
                }else if (type==byte[].class){
                    byte[] value= (byte[]) object;
                    contentValues.put(key,value);
                }else {
                    continue;
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }

    private ContentValues getValuesByUpdate(T entity) {
        ContentValues contentValues=new ContentValues();
        Iterator<Map.Entry<String ,Field>> iterator=cacheMap.entrySet().iterator();
        while (iterator.hasNext()){

            Map.Entry<String ,Field> fieldEntry=iterator.next();
            Field field=fieldEntry.getValue();
            String key=fieldEntry.getKey();

            field.setAccessible(true);
            try {

                Object object=field.get(entity);
                Class type=field.getType();
                if (type==String.class){
                    String value= (String) object;
                    if (value!=null){
                        contentValues.put(key,value);
                    }

                }else if (type==Double.class){
                    Double value= (Double) object;
                    if (value!=null) {
                        contentValues.put(key, value);
                    }
                }else if (type==Integer.class){
                    Integer value= (Integer) object;
                    if (value!=null) {
                        contentValues.put(key, value);
                    }
                }else if (type==Long.class){
                    Long value= (Long) object;
                    if (value!=null) {
                        contentValues.put(key, value);
                    }
                }else if (type==byte[].class){
                    byte[] value= (byte[]) object;
                    if (value!=null) {
                        contentValues.put(key, value);
                    }
                }else {
                    continue;
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return contentValues;
    }

    protected List<T> getResult(Cursor cursor,T where){
        ArrayList list=new ArrayList();
        Object item;
        while (cursor.moveToNext()){
            try {
                item=where.getClass().newInstance();
                Iterator<Map.Entry<String,Field>> iterator=cacheMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String,Field> entry=iterator.next();
                    String colmunName=entry.getKey();
                    Field field=entry.getValue();
                    int columnIndex=cursor.getColumnIndex(colmunName);
                    Class type=field.getType();
                    if (columnIndex!=-1){
                        if (type==String.class){
                              field.set(item,cursor.getString(columnIndex));
                        }else if (type==Double.class){
                            field.set(item,cursor.getDouble(columnIndex));
                        }else if (type==Integer.class){
                            field.set(item,cursor.getInt(columnIndex));
                        }else if (type==Long.class){
                            field.set(item,cursor.getLong(columnIndex));
                        }else if (type==byte[].class){
                            field.set(item,cursor.getBlob(columnIndex));
                        }else {
                            continue;
                        }
                    }
                }
                list.add(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return list;
    }

    class Condition{
        private String whereClause;
        private String[]whereArgs;
        public Condition(ContentValues whereClause){
            ArrayList list=new ArrayList();
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(" 1=1 ");
            Set keys=whereClause.keySet();
            Iterator iterator=keys.iterator();
            while (iterator.hasNext()){
                String key=(String)iterator.next();
                String value=(String)whereClause.get(key);

                if (value!=null){
                    stringBuilder.append("and "+key+"=?");
                    list.add(value);
                }

            }

            this.whereClause=stringBuilder.toString();
            this.whereArgs=(String[])list.toArray(new String[list.size()]);
        }

        public String[] getWhereArgs(){
            return whereArgs;
        }
        public String getWhereClause(){
            return whereClause;
        }
    }
}
