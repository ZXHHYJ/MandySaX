package mandysax.lifecycle;
import android.content.Intent;
import android.os.Bundle;

public class AppCompatActivity extends FragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void startActivity(Intent intent)
	{
		super.startActivity(intent);
	}

	@Override
	public void startActivity(Intent intent, Bundle options)
	{
		super.startActivity(intent, options);
	}

}
