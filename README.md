# 初衷

最开始是模仿 AndroidX 中优秀的理念和组件，随着自己对 MVVM 架构的理解和 MandySaX 的完善，这个项目转而为一些轻量级的 app 提供 MVVM 架构支持。

**我希望它可以帮助到你们**

# 介绍

**推荐的架构：**

![输入图片说明](https://images.gitee.com/uploads/images/2021/0110/213209_e5b36d37_8555846.png "")

# 注意事项

*目前这个 MandySaX 只建议用于一些轻量级的项目，正在慢慢扩充 MandySaX...*

> 自 2.0.0 版本之后，MandySaX 已可以用于部分生产环境

# 导入

暂时省略...

# 使用

### 使用 ViewModel

新建一个类并继承自 ViewModel

```java
public class MainViewModel extends ViewModel
{
public MainViewModel(String p0)
{
....
}
...
}
```

在继承

```mandysax.plus.fragment.FragmentActivity```的 activity 或```mandysax.plus.fragment.Fragment```的 fragment 中写入如下代码（推荐）

```java
private MainViewModel viewModel;
@Override
protected void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);
viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
}
```

默认情况下 ViewModel 不支持向构造函数中传递参数，若需要向构造函数中传递参数

```java
private MainViewModel viewModel;
@Override
protected void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);
viewModel = ViewModelProviders.of(this, new Factory(){
@Override
public <T extends ViewModel> T create(Class<T> modelClass)
{
return (T)new MainViewModel("参数");
}
}).get(MainViewModel.class);
}
```

### 注意：千万不要让 ViewModel 持有 Activity 或 Fragment 的实例，ViewModel 的生命周期是长于它们的，这会导致 Activity 或 Fragment 无法释放而造成内存泄露

**ViewModel**的生命周期：

![输入图片说明](https://images.gitee.com/uploads/images/2021/0110/213330_1a34051c_8555846.jpeg "ViewModel 的生命周期.jpg")

**ViewModel**的方法和说明：

| 方法 | 说明 |
| :--: | :--: |
| onCleared() | ViewModel 回收时调用 |

**ViewModelProviders**的方法和说明：

| 方法 | 说明 |
| :--: | :--: |
| of(LifecycleOwner lifecycle) | 返回一个 ViewModelLifecycle |
| of(LifecycleOwner lifecycle,Factory factory) | 自定义 ViewModel 的构造方法并返回一个 ViewModelLifecycle |

**ViewModelLifecycle**的方法和说明：

| 方法 | 说明 |
| :--: | :--: |
| get(final Class<T> name) | 返回要获取的 ViewModel 实例 |
| ... | ... |

至此 ViewModel 的使用方法已全部介绍

### 使用 LiveData

>LiveData 是一个用于持有数据并支持数据可被监听（观察），十分建议搭配 ViewModel 使用

定义一个可变的 LiveData 对象，并假设它的泛型类型为 String

```java
private final MutableLiveData<String> livedata = new MutableLiveData<String>();
```

也可以在初始化时直接设置数据

```java
private final MutableLiveData<String> livedata = new MutableLiveData<String>("默认数据");
```

向可变的 LiveData 提交数据

```java
livedata.setValue("数据");
```

>在线程不安全的情况下则建议使用 postValue 来更新数据

```java
livedata.postValue("在子线程中更新数据")
```

观察 LiveData 的数据变化

```java
livedata.observeForever(new Observer<String>() {
@Override
public void onChanged(String p1)
{
System.out.println(p1);
}
});
```

>上述的监听方法无法跟随 Activity 或 Fragment 的生命周期，若需要在 Activity 不活跃时暂停响应、销毁时取消监听，可以这样设置

```java
//this 为 LifecycleOwner 的实例
//注：FragmentActivity 和 Fragment 本身就是一个 LifecycleOwner 实例
livedata.observe(this, new Observer<String>(){
@Override
public void onChanged(String p1)
{
System.out.println(p1);
}
});
```

考虑到数据的封装性，MandySaX 也提供不可变的 LiveData

```java
private final LiveData<String> _livedata = livedata;
//livedata 为上面的 MutableLiveData 实例
```

>建议只暴露不可变的 LiveData，将可变的 LiveData 私有，从而保证了数据的封装性

**LiveData**的方法和说明：

| 方法 | 说明 |
| :--: | :--: |
| setValue(T value) | 直接更新数据 |
| postValue(T value) | 以线程安全的方式更新数据 |
| observe(LifecycleOwner lifecycle, Observer observer) | 以注重生命周期的方法观察数据 |
| observeForever(Observer observer) | 直接观察数据 |

至此 LiveData 的使用方法已全部介绍

### 使用 Fragment

> 起初 MandySaX 并没有想过做自己 fragment，做这个 fragment 的原因是我在自己的项目中用到了 fragment，测试时发现不同系统版本的 fragment 的实际运行效果各不相同，甚至闪退，后来发现谷歌在 Android9.0 时也已经废弃了系统 fragment，自此开始了自己的 fragment 的开发

fragment 是可以让你的 app 纵享丝滑的设计，优化 app 时，如果可以，你可以把 activity 换成 fragment，减少内存占有和提高 app 运行的效率，如果你的 app 当前或以后有移植平板等大屏设备的计划，那时可以让你节省大量时间和精力。

![输入图片说明](https://images.gitee.com/uploads/images/2021/0110/213410_a2788ba0_8555846.png "")

**生命周期，和系统的大差不差**

![输入图片说明](https://images.gitee.com/uploads/images/2021/0110/213435_3b4f5a50_8555846.png "")

至此 Fragment 的使用方法已全部介绍

### 使用 FragmentActivity

**因为和系统 Activity 几乎一致，故只介绍一些特别的地方**

**FragmentActivity**的方法和说明：

| 方法 | 说明 |
| :--: | :--: |
| getMannger() | 返回一个 FragmentMannger |
| setContentView(Class fragmentClass) | 使用 fragment 作为 activity 的主界面 |
| getActivityFragment() | 返回当前 activity 的主界面 fragment 实例 |
| findFragmentByTag(Object tag) | 获取标签为 tag 的 fragment 实例 |

至此 FragmentActivity 的使用方法已**基本**介绍

...

**还有四个重要组件未介绍，后期补齐，见谅**

# 更新内容

1.修复 Fragment 在特定条件下没有 removeView 的问题

2.修复 Fragment 生命周期错乱的问题

3.修复 Fragment 可能发生的内存泄漏的问题

4.修复 Fragment 的 isAdded 方法返回值没有可靠性的问题

5.Fragment 返回栈重构

6.Fragment 控制器重构

7.Fragment 支持<fragment>标签

8.Fragment 支持显示隐藏、进栈出栈动画

9.修复 Fragment 控制器在部分生命周期中 add/remove 可能发生的闪退问题

10.修复 Fragment 的 replace 方法同时搭配添加返回栈可能导致的 Fragment 显示错乱问题

11.修复 Fragment 添加返回栈后返回 Fragment 无法释放的问题

12.修复 Fragment 在隐藏时也可以接收生命周期事件的问题

13.优化 Fragment 的 onActivityCreated 方法执行时 Activity 可能还没有构建完毕的问题

14.Fragment 控制器新增针对 Fragment 返回栈的四个 API

15.Fragment 支持获取自身 Tag

16.Fragment 支持 onBackPressed()方法，优先级高于 Activity

17.Lifecycle 新添加 onRestart 周期

18.ViewModelProviders 支持自定义 ViewModelStore 来提供更大的作用域

19.AndroidViewModel 和 ViewModel 分离

20.Anna 更改部分 API

21.Anna 支持单独对某个变量设置 path 了，请使用 PATH 注解

22.Anna 支持 POST 和 LONG 两个新的注解

23.Anna 新增 postKey 方法自动插入参数

23.优化 Anna 网络请求

24...

更多更新内容请参考源码。