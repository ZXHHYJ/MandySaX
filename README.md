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

**ViewModel**的方法：
| 方法 | 说明 |
| :--: | :--: |
| onCleared() | ViewModel回收时调用 |

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
>上述的监听方法无法跟随Activity或Fragment的生命周期，若需要在Activity销毁时取消监听，可以这样设置
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
  **LiveData**的方法和参数
  
| 方法 | 说明 |
| :--: | :--: |
| setValue(T value) | 更新数据 |
| postValue(T value) | 切换到主线程并更新数据 |
| observe(this, Observer observer) | 以注重生命周期的方法观察数据 |
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
   
   **BottomNavigationBar**的方法和参数
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
 
 > 你可以使用FragmentPage的add方法添加多个fragment
 ```java
 public List<Fragment> add(Fragment... fragment)
	{
		...
	}

	public List<Fragment> add(Fragment... fragment, int index)
	{
		...
	}

 ```
 **showFragment**的方法：
 | 方法 | 说明 |
| :--: | :--: |
| add(Fragment... fragment) | 添加fragment |
| add(Fragment... fragment, int index) | 添加fragment，index为默认显示的fragment下标 |
| showFragment(int index) | 显示下标为index的fragment |

 # Usage 注解
 
 >注：需要继承AppCompatActivity或FragmentCompat的类才可以使用
 
 * @BindLayoutId(R.layout.x)
 >设置布局为R.layout.x
 * @BindFragment(R.id.x)
 >加载fragment，并添加到R.id.x，在屏幕旋转后依旧可以保证两个fragment对象一致
 * @BindView(R.id.x)
 >绑定控件R.id.x
 * @BindView
 >根据变量名称去绑定控件,findViewById(R.id.变量名称),R为当前context的getPackageName()路径
 * @ViewClick(R.id.x)
 ```java
 @ViewClick(R.id.x)
	public void VOID()
	{
	}
```
>为R.id.x设置点击事件

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

