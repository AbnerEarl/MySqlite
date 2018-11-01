# MySqlite
仿照华为应用开发框架，手写数据库处理框架，采用接口设计模式，自定义注解，实现类似OrmLite的功能，但是此框架比OrmLite更加的灵活、更加的轻量级，支持本地高并发，给予HashMap采用的缓存优化设计，实现对数据库的操作更快、更迅速。封装之后，简单的两行代码实现对数据的增删改查。


# 最新版本

版本号：[![](https://www.jitpack.io/v/YouAreOnlyOne/MySqlite.svg)](https://www.jitpack.io/#YouAreOnlyOne/MySqlite)

使用自行替换下面的版本号，以获得最新版本。

# 使用体验

1、下载app，安装之后进行，增删改查操作：

https://github.com/YouAreOnlyOne/MySqlite/blob/master/source/demo.apk。
    
安装之后，直接运行。
    

# 使用方法

这里分别介绍在不同项目环境中，如何引用对该库的依赖。

## Android中使用：

方法一：

1.第一步，在项目的build.gradle下配置，注意是项目的build.gradle：

     allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
    
    
2.第二步,在app的build.gradle下添加如下依赖：

    dependencies {
            ...
            implementation 'com.github.YouAreOnlyOne:MySqlite:版本号'
            ...
     }
    
    
方法二：
    
 1.第一步，下载依赖的包：
 
https://github.com/YouAreOnlyOne/MySqlite/blob/master/source/mysqlite-release.aar 。

并放到项目的 libs 目录下面。
    
 2.第二步,在app的build.gradle下添加如下依赖，注意，两个依赖是平级关系：
    
    repositories {
       flatDir {
           dirs 'libs'
         }
    }
    
    dependencies {
            ...
            compile(name:'mysqlite-release', ext:'aar')
            ...
    }
 

	
# 使用示例：

1、首先新建一个实体类Person，采用注解的方式进行，如下所示：
	
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

2、然后利用工厂在Activity中实例化BaseDao，如下所示：

	BaseDao<Person> baseDao= BaseDaoFactory.getInstance().getBaseDao(Person.class);
	
这一行代码不仅实例化BaseDao，也会在数据库中自动创建表。

## 插入数据

	Person person2=new Person();
      person2.setName("Frank2");
      person2.setPassword("5202");
      baseDao.insert(person2);
	       
## 查询数据

	Person where=new Person();
      where.setName("Frank");
      List<Person> list=baseDao.query(where);

## 更新数据

	Person where=new Person();
      where.setName("Frank");
      Person person=new Person();
      person.setPassword("52014");
      long result=baseDao.update(person,where);

## 删除数据

	Person where=new Person();
      where.setName("Frank");
      long result=baseDao.delete(where);
	
	




 
 
# 项目用到的权限

  在manifest文件中添加访问的权限：
 

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

   

 
# 使用指南
 
 更新中……
 
# 相关介绍


OkHttp 、Retrofit 、Volley 、RxJava、Novate在Android中Web网络请求一行代码解决。

https://blog.csdn.net/u014374009/article/details/82933127

一行代码实现Ftp文件上传、文件下载、文件删除和进度监听的工具类的使用。

https://blog.csdn.net/u014374009/article/details/82944107

一行代码解决AndFix热修复和热跟更新问题，集成了阿里的开源库，修复程序的缺陷bug漏洞和功能页面等.

https://blog.csdn.net/u014374009/article/details/83052178




# 其它信息

1.项目还有很多不完善的地方，欢迎大家指导。

2.项目持续更新开源，有兴趣加入项目或者跟随项目的伙伴，可以邮件联系！ 

3.关注或者喜欢或者尝试使用或者感兴趣的伙伴可以，点击 ~ follow、fork、star ~ 。

# 作者邮箱

ycj52011@outlook.com


