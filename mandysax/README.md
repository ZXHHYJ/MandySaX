# 初衷

这个项目最开始是个浏览器，叫做 MandySa，很多人怀疑是给封茗囧菌做的，这个不用怀疑，一开始确实是为她做的，纪念陪伴自己整个青春的偶像封茗囧菌。

**以此纪念我逝去的青春。**

# 介绍

MandySaX 是一个开源库，可加快 Android 程序开发。 它支持响应式编程，有 viewmodel，支持 activity 和 fragment 生命周期管理，最近 MandySaX 也为自己的 activity 开发了属于自己的 fragment。MandySaX 多用于 MVVM 架构的轻量级应用，欢迎 start。

**这是我们推荐的架构：**

![输入图片说明](https://images.gitee.com/uploads/images/2021/0110/213209_e5b36d37_8555846.png "")

如果需要它的使用方法，请继续阅读吧

# 注意事项

目前这个 MandySaX 只建议用于一些轻量级的项目，正在慢慢扩充 MandySaX...

# 导入

把下载项目文件，放到需要导入的项目的起始目录，然后长按，点击导入

![输入图片说明](https://images.gitee.com/uploads/images/2021/0110/213300_3a2cf839_8555846.png "")

下面我们可以开始使用了

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