package kiway.cn.webviewjszhuru;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/6/6.
 */

public class App extends Application {
    public OkHttpClient mOkHttpClient =
            new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(30, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)//设置连接超时时间
                    .build();

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        SDKInitializer.initialize(this);
    }

}
