# Gift-for-Mandy-Sa
送给封茗囧菌的小礼物
*这个项目最初的简介是：此项目为封茗囧菌而做

# MandySaX
mandysax is an open source library that facilitates Android program development. It supports responsive programming, activity and fragment life cycle management, MVVM architecture development... Welcome to start

# Preview
<img src="preview/BottomNavigationBar.jpg"/>
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

ViewModel的生命周期：<br/>
![image](https://upload-images.jianshu.io/upload_images/2226848-731acbf01fa30c06.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/522)

# Usage BottomNavigationBar
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
   
   BottomNavigationBar的方法
   
   setType(NavShowType type)
   >type:显示类型
   >NORMAL, //普通模式，包含文字和图片
   >NO_TEXT, //无文字模式
   >NO_IMAGE, //无图模式
   >CHECKED_SHOW_TEXT //普通无文字 选中出现文字
	
   setSelected(int index)
   >index:下标，相当于点击了第index个按钮
   
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

