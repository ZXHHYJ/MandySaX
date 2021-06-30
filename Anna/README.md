# Anna
以注解的方式进行网络请求
# 我是怎么取名的？
图个乐，从歌名里取得《安娜的橱窗》
# 创建构建器
*因为大多数项目的服务器域名都是只有一个* 
```Java
public class ServiceCreator
{
private static final Anna2 anna=Anna2.Build().baseUrl("https://x.xx.xxx");//域名地址建议以'/'结尾

public static <T> T create(Class<T> clazz){
return anna.newProxy(clazz);//创建代理类
}
}
```
# 创建请求方法
1.创建一个 interface 文件，名称建议以 xxxService 命名
2.创建数据模型 class model，建议以 xxxModel 命名
以下是示例：
```Java
public interface xxxService
{
@GET("api/xxx")//设置请求类型和请求地址，最后的请求地址是 baseUrl+补充地址：https://x.xx.xxx/api/xxx
@Query("name")//设置方法上的参数类型和 key
Observable<TestModel> test(String name)
}
```
假设服务器返回的json是这样的
```
{name:"李白",Fans:99}
```
解析的数据模型应该是这个样子
```Java
//默认为string类型数据
//JSONArray类型的数据可添加@Array注解
public class xxxModel
{
@Key("name")
public String userName;

@Key("Fans")
public int fans;
}
```
使用
```Java
ServiceCreator.create(/*你的interface .class文件*/).test(/*内容*/).set(new Callback<InfoModel>(){

@Override
public void onResponse(boolean loaded, InfoModel t)
{
//loaded为是否已经加载成功，除非model解析的数据为JSONArray，否则不用关心它的返回值
//请求成功，t为解析后生成的实体类
}

@Override
public void onFailure(int code)
{
//网络请求错误
}
});
```
# 补充说明
注解说明其`作用域`：

| 注解 | 作用 | 作用域 |
|:----:|:----:|:----:|
|   Key   |   解析json并赋值   |   所有json支持的变量   |
|   Body   |   post的body   |   方法   |
|   Header   |   设置单个请求头以:分隔(只能有一个:)   |   方法   |
|   Headers   |   根据对应位置的参数设置请求头   |   方法   |
|   Query   |   以拼接URL的方式   |   方法   |
|   Value   |   替换URL中所有的{value.name}   |   方法   |
|   Path   |   设置数据的解析路径   |   方法和变量   |


*参数被其他注解占用时可以以“,”分隔*
