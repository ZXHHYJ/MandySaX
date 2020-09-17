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
... 
}
```

在继承AppCompatActivity的activity或FragmentCompat的fragment中写入如下代码（推荐）
```java
private final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
```

ViewModel的生命周期：
![image](http://39.106.7.220/mandysa/ViewModel.jpg)

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
   
   BottomNavigationBar的方法和参数
| 方法 | 说明 |
| :--: | :--: |
| setType(NavShowType type) | 设置控件样式 |
| setSelected(int index) | 设置选中的item |
| NavShowType | 说明 |
| :--: | :--: |
| NavShowType.NORMAL | 普通模式，包含文字和图片 |
| NavShowType.NO_TEXT | 无文字模式 |
| NavShowType.NO_IMAGE | | 无图模式 |
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

> 设置点击回调,以处理之后与ViewPager或Fragment的交互

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

与BottomNavigationBar搭配使用：
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
 showFragment(int index)
 >index:下标，显示第index个fragment
 
 # Usage 注解
 
 >注：需要继承AppCompatActivity或FragmentCompat
 
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

