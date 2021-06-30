package mandysax.anna2;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import mandysax.anna2.annotation.Body;
import mandysax.anna2.annotation.Get;
import mandysax.anna2.annotation.Header;
import mandysax.anna2.annotation.Path;
import mandysax.anna2.annotation.Post;
import mandysax.anna2.annotation.Query;
import mandysax.anna2.observable.Observable;
import mandysax.anna2.utils.GenericUtils;

@SuppressWarnings("ALL")
public final class NetworkHandler implements InvocationHandler {
    private final Anna2 mAnna;

    private String mUrl;//url

    private HashMap<String, String> mBody;//post提交的信息

    private HashMap<String, String> mHeader;//请求头

    private String mPath;//string解析路径

    private Class mReturnGenericClazz;//callback的泛型类型

    NetworkHandler(Anna2 anna) {
        mAnna = anna;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        mReturnGenericClazz = GenericUtils.getGenericType(method);//获取需要返回的泛型类型
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation instanceof Get)
                return handlerGet(method, (Get) annotation, args);
            if (annotation instanceof Post)
                return handlerPost(method, (Post) annotation, args);
        }
        return null;
    }

    private Observable handlerGet(Method method, Get get, Object[] params) {
        mUrl = mAnna.getBaseUrl() + get.value();//拼接url
        method.getParameterAnnotations();
        handlerAnnotation(method, params);
        if (mReturnGenericClazz != null)
            return Observable.create(0, mUrl, mPath, mBody, mHeader, mReturnGenericClazz);
        throw new NullPointerException("Wrong return type");
    }

    private void handlerAnnotation(Method method, Object[] params) {
        if (mUrl == null || mReturnGenericClazz == null) throw new RuntimeException();
        handlerParams(params);
        Annotation[] annotation = method.getAnnotations();
        for (Annotation annotation1 : annotation) {
            if (annotation1 instanceof Path) {
                if (mPath != null) throw new RuntimeException("Only one @Path is allowed");
                mPath = ((Path) annotation1).value();
            }
        }
        Annotation[][] annotations2 = method.getParameterAnnotations();
        int m = 0;
        for (int i = 0; i < annotations2.length; i++) {
            if (annotations2[i][0] instanceof Query) {
                Query query = (Query) annotations2[i][0];
                mUrl += (m == 0 ? "?" : "&");
                mUrl += query.value() + "=" + params[i];
                m++;
            } else if (annotations2[i][0] instanceof Body) {
                if (mBody == null)
                    mBody = new HashMap<>();
                Body body = (Body) annotations2[i][0];
                mBody.put(body.value(), params[i].toString());
            } else if (annotations2[i][0] instanceof Header) {
                if (mHeader == null)
                    mHeader = new HashMap<>();
                Header header = (Header) annotations2[i][0];
                mHeader.put(header.value(), params[i].toString());
            }
        }

    }

    private void handlerParams(Object[] params) {
        if (params != null)
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof ArrayList) {
                    String str = params[i].toString();
                    if (str.length() == 0) continue;
                    if (str.charAt(0) == '[' && str.substring(str.length() - 1).equals("]")) {
                        str = str.substring(1, str.length() - 1);//去掉内容两边的[]
                        params[i] = str;
                    }
                }
            }
    }

    private Observable handlerPost(Method method, Post post, Object[] params) {
        mUrl = mAnna.getBaseUrl() + post.value();
        handlerAnnotation(method, params);
        if (mReturnGenericClazz != null)
            return Observable.create(1, mUrl, mPath, mBody, mHeader, mReturnGenericClazz);
        throw new NullPointerException("Wrong return type");
    }

}
