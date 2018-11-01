package com.example.frank.mysqlite;

import com.example.mysqlite.annotion.DbFiled;
import com.example.mysqlite.annotion.DbTable;

/**
 * PROJECT_NAME:MyApplication
 * PACKAGE_NAME:com.example.frank.mysqlite
 * USER:Frank
 * DATE:2018/10/29
 * TIME:9:21
 * DAY_NAME_FULL:星期一
 * DESCRIPTION:On the description and function of the document
 **/
@DbTable("tb_person")
public class Person {
    @DbFiled("tb_name")
    public String name;
    @DbFiled("tb_password")
    public String password;
    @DbFiled("tb_photo")
    public byte[] photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
