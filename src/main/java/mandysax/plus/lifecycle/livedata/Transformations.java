package mandysax.plus.lifecycle.livedata;

import mandysax.plus.lifecycle.livedata.Observer;

public final class Transformations
{

	/*
	liveData is Monitored livedata
	map method
	*/
	
	public static <T extends MutableLiveData<V>,E extends Object,V extends Object> T map(LiveData<E> liveData, final Map<E,V> map)
	{
		final MutableLiveData<V> mapLiveData= new MutableLiveData<V>();
		liveData.observeForever(new Observer<E>(){

				@Override
				public void onChanged(E p1)
				{
					mapLiveData.setValue(map.map(p1));
				}


			});
		return (T)mapLiveData;
	}
	
	public static <T extends MutableLiveData<Object>,E extends MutableLiveData<V>,V extends Object> E switchMap(final T liveData, final switchMap<T,V> switchMap)
	{
		final MutableLiveData<V> switchMapLiveData= new MutableLiveData<V>();
		liveData.observeForever(new Observer<Object>(){

				@Override
				public void onChanged(Object p1)
				{ 
					switchMapLiveData.setValue(switchMap.map(liveData).getValue());
				}

			});
		return (E)switchMapLiveData;
	}
}
