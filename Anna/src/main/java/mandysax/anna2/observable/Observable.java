package mandysax.anna2.observable;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import mandysax.anna2.ModelFactory;
import mandysax.anna2.annotation.Array;
import mandysax.anna2.callback.Callback;
import mandysax.anna2.utils.JsonUtils;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class Observable<T> {

    private Callback<T> mCallback;

    private final Class<T> mModel;

    private final String mUrl;//url

    private final String[] mPath;//数据解析路径

    private final HashMap<String, String> mParams, mHeaders;//携带参数和请求头

    private final int mType;//请求类型 0get 1post

    private Observable(int type, String url, String path, HashMap<String, String> params, HashMap<String, String> headers, Class<T> model) {
        mType = type;
        mUrl = url;
        if (path != null)
            mPath = path.split("/");
        else mPath = null;
        mParams = params;
        mModel = model;
        mHeaders = headers;
    }

    public static <T> Observable create(int type, String url, String path, HashMap<String, String> params, HashMap<String, String> headers, Class<T> model) {
        return new Observable<>(type, url, path, params, headers, model);
    }

    private String getUrl(String path, Map<String, String> paramsMap) {
        if (paramsMap != null) {
            StringBuilder pathBuilder = new StringBuilder(path + "?");
            for (String key : paramsMap.keySet()) {
                pathBuilder.append(key).append("=").append(paramsMap.get(key)).append("&");
            }
            path = pathBuilder.toString();
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public void set(Callback<T> callback) {
        mCallback = callback;
        OkHttpClient httpClient = new OkHttpClient();

        Request.Builder builder = mType == 0 ? new Request.Builder().url(getUrl(mUrl, mParams)) : new Request.Builder().url(mUrl);
        if (mHeaders != null)
            for (Map.Entry<String, String> set : mHeaders.entrySet()) {
                builder.header(set.getKey(), set.getValue());
            }
        switch (mType) {
            case 0:
                builder.get();
                break;
            case 1:
                FormBody.Builder formBody = new FormBody.Builder();
                for (Map.Entry<String, String> set : mParams.entrySet()) {
                    formBody.add(set.getKey(), set.getValue());
                }
                builder.post(formBody.build());
                break;
        }
        Request request = builder.build();
        Call call = httpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {

            private final Handler mHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                mHandler.post(() -> mCallback.onFailure(call.hashCode()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                AtomicReference<String> data = new AtomicReference<>(response.body().string());
                mHandler.post(() -> {
                    try {
                        if (mPath != null) data.set(JsonUtils.Parsing(data.get(), mPath));
                        if (mModel.getAnnotation(Array.class) != null) {
                            JSONArray json = new JSONArray(data.get());
                            for (int i = 0; i < json.length(); i++) {
                                mCallback.onResponse(i == json.length() - 1, ModelFactory.create(mModel, json.getString(i)));
                            }
                        } else
                            mCallback.onResponse(true, ModelFactory.create(mModel, data.get()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        mCallback.onFailure(response.code());
                    }
                });
            }

        });
    }

}
