package mandysax.plus.transaction;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import mandysax.plus.lifecycle.livedata.MutableLiveData;
import mandysax.plus.lifecycle.livedata.Observer;

public class Transaction<T>
{
	/*私有管理器*/
	private final TransactionManager<T> mManager=new TransactionManager<T>();

	public Transaction<T> setOnUpDatahListener(OnUpDatahListener<T> listener)
	{
		mManager.mListener = listener;
		return this;
	}

	/*
	*设置是否自动提交
	*/
	public Transaction<T> setAutoCommit(boolean autoCommit)
	{
		mManager.mIsAutoCommit = autoCommit;
		return this;
	}

	/*
	*回退
	*/
	public boolean rollback()
	{
		return rollback(mManager.mBackList.size() - 1);
	}

	/*
	*指定回退位置
	*/
	public boolean rollback(int tarkIndex/*回退坐标*/)
	{
		List<T> backData=null;
		if (tarkIndex >= 0)
			backData = mManager.mBackList.get(tarkIndex);
		if (mManager.mListener != null)
		{
			if (backData != null)
				for (T data:backData)
					mManager.mListener.backData(data);
			
		}
		if (backData != null)
		{
			return mManager.mBackList.remove(backData);
		}
		return false;
	}

	public TransactionManager<T> beginTransaction()
	{
		if (!mManager.mTarkList.isEmpty())
		{
			mManager.mTarkList.clear();
		}
		return mManager;
	}

	public class TransactionManager<T>
	{
		private MutableLiveData<T> mTark=new MutableLiveData<T>().observeForever(new Observer<T>(){

				@Override
				public void onChanged(T p1)
				{
					mTarkList.add(p1);
					if (mIsAutoCommit)commit();
				}	
			});

		private final List<CopyOnWriteArrayList<T>> mBackList=new CopyOnWriteArrayList<CopyOnWriteArrayList<T>>();

		private final List<T> mTarkList=new CopyOnWriteArrayList<T>();

		private OnUpDatahListener<T> mListener;

		private boolean mIsAutoCommit=false;

		public TransactionManager<T> putData(T data)
		{
			mTark.setValue(data);
			return this;
		}

		public void commit()
		{
			if (mTarkList.isEmpty())return;
			CopyOnWriteArrayList<T> commitData=new CopyOnWriteArrayList<T>();
			commitData.addAll(mTarkList);
			mBackList.add(commitData);
			mTarkList.clear();
			if (mListener != null)
				for (T data:commitData)
				{
					mListener.commitData(data);
				}
		}

		public void chancel()
		{
			if (mListener != null)
				for (T data:mTarkList)
					mListener.chancelData(data);	
			mTarkList.clear();
		}

	}

	public abstract interface OnUpDatahListener<T>
	{
		void commitData(T data)

		void chancelData(T data)

		void backData(T data)
	}
}
