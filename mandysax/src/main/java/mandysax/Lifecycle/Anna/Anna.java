package mandysax.Lifecycle.Anna;

public class Anna
{
	
	public static AnnaString getString(String url)
	{
		return new AnnaString(url);
	}

	public static AnnaBitmap getBitmap(String url)
	{
		return new AnnaBitmap(url);
	}

	public static AnnaJsonArray getJsonArray(String url)
	{
		return new AnnaJsonArray(url);
	}

}
