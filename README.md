# Gift-for-Mandy-Sa
送给封茗囧菌的小礼物
*这个项目最初的简介是：此项目为封茗囧菌而做

# MandySaX
mandysax is an open source library that facilitates Android program development. It supports responsive programming, activity and fragment life cycle management, MVVM architecture development... Welcome to start

# MandySaX MVVM-ViewModel

>新建ViewModel类
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

在继承**AppCompatActivity**的activity或**FragmentCompat**的fragment中写入如下代码（推荐）
```java
private final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
```

>默认情况下ViewModel不支持向构造函数中传递参数，若需要向构造函数中传递参数
```java
private final MainViewModel viewModel=ViewModelProviders.of(this, new Factory(){

			@Override
			public <T extends ViewModel> T create(Class<T> modelClass)
			{
				return (T)new MainViewModel("参数");
			}
		}).get(MainViewModel.class);
```

>注意：千万不要让ViewModel持有Activity或Fragment的实例，ViewModel的生命周期是长于它们的，这会导致Activity或Fragment无法释放而造成内存泄露

**ViewModel**的生命周期：
![image](http://39.106.7.220/mandysa/ViewModel.jpg)

**ViewModel**的方法和说明：
| 方法 | 说明 |
| :--: | :--: |
| onCleared() | ViewModel回收时调用 |

**ViewModelProviders**的方法和说明：
| 方法 | 说明 |
| :--: | :--: |
| of(LifecycleOwner lifecycle) | 返回一个ViewModelLifecycle |
| of(LifecycleOwner lifecycle,Factory factory) | 自定义ViewModel的构造方法并返回一个ViewModelLifecycle |

**ViewModelLifecycle**的方法和说明：
| 方法 | 说明 |
| :--: | :--: |
| get(final Class<T> name) | 返回要获取的ViewModel实例 |
| ... | ... |

# MandySaX MVVM-LiveData
>LiveData是一个用于持有数据并支持数据可被监听（观察），十分建议搭配ViewModel使用

定义一个可变的LiveData对象，并假设它的泛型类型为String
```java
private final MutableLiveData<String> livedata = new MutableLiveData<String>();
```
也可以在初始化时直接设置数据
```java
private final MutableLiveData<String> livedata = new MutableLiveData<String>("默认数据");
```

向可变的LiveData提交数据
```java
livedata.setValue("数据");
```
>若在子线程中需要更新数据，且数据变化后需要[更新UI]，则建议使用postValue来更新数据
```java
livedata.postValue("在子线程中更新数据")
```

观察LiveData的数据变化
```java
livedata.observeForever(new Observer<String>() {

				@Override
				public void onChanged(String p1)
				{
					System.out.println(p1);
				}
			});
```
>上述的监听方法无法跟随Activity或Fragment的生命周期，若需要在Activity不活跃时暂停响应、销毁时取消监听，可以这样设置
```java
//this为LifecycleOwner的实例
//注：AppCompatActivity和FragmentCompat本身就是一个LifecycleOwner实例
livedata.observe(this, new Observer<String>(){

				@Override
				public void onChanged(String p1)
				{
				    System.out.println(p1);
				}
					
		});
```
考虑到数据的封装性，MandySaX也提供不可变的LiveData
```java
private final LiveData<String> _livedata = livedata;
//livedata为上面的MutableLiveData实例
```
>建议只暴露不可变的LiveData，将可变的LiveData私有，从而保证了数据的封装性
  
  **LiveData**的方法和说明：  
| 方法 | 说明 |
| :--: | :--: |
| setValue(T value) | 更新数据 |
| postValue(T value) | 切换到主线程并更新数据 |
| observe(LifecycleOwner lifecycle, Observer observer) | 以注重生命周期的方法观察数据 |
| observeForever(Observer observer) | 观察数据 |

# Usage BottomNavigationBar

控件演示：
![image](http://39.106.7.220/mandysa/BottomNavigationBar.jpg)

   > 在布局xml文件中放置
   ```xml
   <mandysax.design.BottomNavigationBar
   android:layout_height="wrap_content"
   android:layout_width="match_parent"
   app:textCheckedColor="@color/checked"
   app:textUnCheckedColor="@color/unchecked"
   app:show_type="no_image"/>
   ```
   
   * textCheckedColor 字体选中颜色
   * textUnCheckedColor 字体未选中颜色
   * show_type 显示模式（下方4种模式的小写形式）
   
   **BottomNavigationBar**的方法、说明和参数：
| 方法 | 说明 |
| :--: | :--: |
| setType(NavShowType type) | 设置控件样式 |
| setSelected(int index) | 设置选中的item |
| 参数 | 说明 |
| NavShowType.NORMAL | 普通模式，包含文字和图片 |
| NavShowType.NO_TEXT | 无文字模式 |
| NavShowType.NO_IMAGE | 无图模式 |
| NavShowType.CHECKED_SHOW_TEXT | 普通无文字 选中出现文字 |

   > 在java代码中为bar添加子项
   
   ```java
bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bnBar);
        /*
        add bottom navigation item
        param1 bottom text
        param2 bottom image checked
        param3 bottom image unchecked
         */
        bottomNavigationBar.addItemView("test",选中图片资源,未选中图片资源);
```

> 设置点击回调,以处理之后与ViewPager或FragmentPage的交互

```java
//set Callback
        bottomNavigationBar.setOnItemViewSelectedListener(new BottomNavigationBar.OnItemViewSelectedListener() {
            /**
             * call when item clicked
             * @param v clicked item
             * @param index item index
             */
            @Override
            public void onItemClcik(View v, int index) {

            }
        });
```

# Usage FragmentPage
>注：此控件十分建议搭配注解使用

与**BottomNavigationBar**搭配使用：
![image](http://39.106.7.220/mandysa/FragmentPage.jpg)


在布局xml文件中放置
```xml
<mandysax.design.FragmentPage
 android:layout_width="match_parent"
 android:layout_height="match_parent"/>
 ```
 
 为这个FragmentPage设置一个底部栏
 ```xml
 <?xml version="1.0" encoding="utf-8"?>
<mandysax.design.FragmentPage xmlns:android="http://schemas.android.com/apk/res/android"
	xmlns:app="http://schemas.android.com/apk/res-auto"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:id="@+id/mainFragmentPage">

	<mandysax.design.BottomNavigationBar
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:id="@+id/mainBottomNavigationBar"/>
		
		...

</mandysax.design.FragmentPage>
 ```
 
 往底部栏上添加一个控件
 ```xml
 <?xml version="1.0" encoding="utf-8"?>
<mandysax.design.FragmentPage xmlns:android="http://schemas.android.com/apk/res/android"
	xmlns:app="http://schemas.android.com/apk/res-auto"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:id="@+id/mainFragmentPage">

	...

	<mandysax.design.MusicView
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:id="@+id/mainMusicView"
		android:layout_marginLeft="5dp"
		android:layout_marginRight="5dp"
		app:navigationbar_top="true"/>

</mandysax.design.FragmentPage>
 ```
 
 > 你可以使用FragmentPage的add方法添加多个fragment
 ```java
 public List<Fragment> add(Fragment... fragment)
	{
		...
	}

 ```
 **FragmentPage**的方法和说明：
 | 方法 | 说明 |
| :--: | :--: |
| add(Fragment... fragment) | 添加fragment，FragmentPage会自动管理 |
| showFragment(int index) | 显示下标为index的fragment |

 # Usage Fragment&FragmentActivity
 
 * 仅1.4.0 beta1或更高版本
 
 新建一个class并继承自**mandysa.lifecycle.Fragment**
 重写**onCreateView**方法，在此返回**Fragment**的布局
 ![image](http://39.106.7.220/mandysa/Fragment.jpg)
 >在FragmentActivity中显示Fragment
 修改你的**Activity**继承自**FragmentActivity**或**AppCompatActivity**
 在onCreateView中使用如下代码
 ![image](http://39.106.7.220/mandysa/ShowFragment.jpg)

**getMannger**的方法和说明：
 | 方法 | 说明 |
| :--: | :--: |
| add(int id, Fragment fragment) | 在指定控件上添加fragment |
| replace(int id, Class replaceFragment) | 在指定控件上显示fragment |
| add(int id, Fragment fragment, Object tag) | 在指定控件上添加fragment，并设置标签 |
| findFragmentByTag(Object tag) | 按tag寻找fragment |
| show(Fragment fragment) | 显示fragment |
| hide(Fragment fragment) | 隐藏fragment |
| remove(Fragment fragment) | 移除fragment |
| addToBackStack() | 为fragment添加返回栈 |


 # Usage 注解
 
 >注：需要继承AppCompatActivity或FragmentCompat的类才可以使用（1.4.1 beta1之后废弃）
 
 * @BindLayoutId(R.layout.x)
 >设置布局为R.layout.x
 * @BindFragment(R.id.x)
 >加载fragment，并添加到R.id.x，在屏幕旋转后依旧可以保证两个fragment对象一致
 * @BindView(R.id.x)
 >绑定控件R.id.x
 * @BindView
 >根据变量名称去绑定控件,findViewById(R.id.变量名称),R为当前context的getPackageName()路径


示例：
 ```java
 import ...;

@BindLayoutId(R.layout.main)//设置布局
public class MainActivity extends AppCompatActivity
{

	@BindFragment(R.id.mainFragmentPage)
	private HomeFragment home_fragment;

	@BindFragment(R.id.mainFragmentPage)
	private RecommendFragment recommend_fragment;

	@BindFragment(R.id.mainFragmentPage)
	private SearchFragment search_fragment;

	@BindFragment(R.id.mainFragmentPage)
	private MyFragment my_fragment;

	@BindView
	private FragmentPage mainFragmentPage;

    ...
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mainFragmentPage.add(home_fragment, recommend_fragment, search_fragment, my_fragment);
		//关键
		...
	}
	
	...
}
```

# Update log

+ v1.4.1 beta1
  - 修复fragment生命周期onActivityCreated错误
  - 2020-10-21 17:46

+ v1.4.0
  - 完成了fragment的开发
  - 2020-10-19 20:26

+ v1.4.0 beta3
  - 修复fragment的部分bug，优化部分细节
  - 2020-10-09 22:22

+ v1.4.0 beta2
  - 新增lifecycle成员fragment(50%)
  - 2020-10-05 00:42

+ v1.4.0 beta1
  - 新增Xlist列表，使用HashMap存储数据，披着List的外衣(有顺序)，目前发现其使用场景十分狭小
  - 优化大量细节
  - 删除部分不规范的代码
  - 删除几乎无用的代码（少量注解）
  - 大量修改ViewModel实现方法，详见demo
  - 修复bug（LiveData生命周期bug...)
  - 2020-10-02 22:59

+ v1.3.5
  - 新增不可存储null的基于HashMap的SafetyHashMap（鸡肋）
  - Anna支持以继承的方法设置默认URL和keyword
  - FragmentPage和FragmentCompat大更新，有望在1.4.0中实现“导航”功能
  - LiveData支持感知生命周期
  - 2020-09-27 21:29

+ v1.3.0
  - 优化Anna
  - 将部分不符合规范的类名进行的更改
  - 提升运行效率（感知不强）
  - 大量细节优化
  - 简化部分api
  - 2020-09-17 23:19

+ v1.2.0
  - 在构建ViewModel时支持向ViewModel传递参数
  - 完成Anna的开发
  - AppCompatActivity、FragmentCompat完美支持注解
  - 细节优化
  - 修复bug
  - 2020-09-01 23:21

+ v1.1.0
  - 大改项目架构，更加符合代码规范
  - 新增design组件库，支持BottomNavigationBar、FragmentPage、MusicView三大控件
  - 新增FragmentCompat，默认实现LifecycleAbstract，支持获取ViewModel
  - Anna雏形
  - MutableLiveData与LiveData互相转换
  - ViewModel支持获取Context
  - 添加注解
  - 修复bug
  - 2020-08-29 23:37

+ v1.0
  - 更名为MandySaX
  - 完成生命周期组件ViewModel、LiveData、Lifecycle三个主要组件
  - 2020-07-26 19:25
