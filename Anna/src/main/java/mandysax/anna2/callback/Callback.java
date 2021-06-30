package mandysax.anna2.callback;

public interface Callback<T>
{
   void onResponse(boolean loaded,T t);
   
   void onFailure(int code);
}
