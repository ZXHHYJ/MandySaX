package mandysax.anna2;

import androidx.annotation.NonNull;

import java.lang.reflect.Proxy;

public class Anna2 {
    private String mBaseUrl;

    //构造函数私有化
    Anna2() {
    }

    @NonNull
    @org.jetbrains.annotations.Contract(value = " -> new", pure = true)
    public static Anna2 Build() {
        return new Anna2();
    }

    public Anna2 baseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public final <T> T newProxy(Class<T> clazz)//构建代理类
    {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new NetworkHandler(this));
    }

}
