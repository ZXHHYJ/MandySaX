package mandysax.Lifecycle.ViewModel;


public class ViewModel
{
	private String tag;

	public void onCleared()
	{
		ViewModelManagement.ViewModelData.remove(this);
	}

	public ViewModel()
	{
		ViewModelManagement.ViewModelData.add(this);
		ViewModelManagement.ViewModelData.get(ViewModelManagement.ViewModelData.size() - 1).setTag(getClass().toString());
	}

	private void setTag(String tag)
	{
		this.tag = tag;
	}

	public String getTag()
	{
		return tag;
	}

}
