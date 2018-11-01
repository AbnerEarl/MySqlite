package com.example.frank.mysqlite;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mysqlite.BaseDao;

import com.example.mysqlite.BaseDaoFactory;
import com.example.mysqlite.DataUtitls;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BaseDao<Person> baseDao;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
       baseDao= BaseDaoFactory.getInstance(null).getBaseDao(Person.class);
       findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Person person2=new Person();
               person2.setName("Frank2");
               person2.setPassword("5202");
               baseDao.insert(person2);
               Person person=new Person();
               person.setName("Frank");
               person.setPassword("520");
               Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.alipay);
               person.setPhoto(DataUtitls.BitmapToBytes(bitmap));
               long result=baseDao.insert(person);
               Toast.makeText(MainActivity.this,"插入数据："+result+" 条",Toast.LENGTH_SHORT).show();
           }
       });

       findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Person where=new Person();
               where.setName("Frank");
               where.setPassword("520");
               List<Person> list=baseDao.query(where);
               Bitmap bitmap=BitmapFactory.decodeByteArray(list.get(0).getPhoto(),0,list.get(0).getPhoto().length);
               Toast.makeText(MainActivity.this,"查询数据："+list.get(0).getName()+","+list.get(0).getPassword(),Toast.LENGTH_SHORT).show();
               imageView.setImageBitmap(bitmap);
           }
       });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person where=new Person();
                where.setName("Frank");
                Person person=new Person();
                person.setPassword("52014");
                long result=baseDao.update(person,where);
                Toast.makeText(MainActivity.this,"更新数据："+result+" 条",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person where=new Person();
                where.setName("Frank");
                long result=baseDao.delete(where);
                Toast.makeText(MainActivity.this,"删除数据："+result+" 条",Toast.LENGTH_SHORT).show();

            }
        });

    }


}
