package kiway.cn.webviewjszhuru;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import kiway.cn.webviewjszhuru.downloadFile.HttpUploadFile;
import kiway.cn.webviewjszhuru.sound.AudioRecoderUtils;
import kiway.cn.webviewjszhuru.sound.PopupWindowFactory;
import kiway.cn.webviewjszhuru.sound.TimeUtils;
import kiway.cn.webviewjszhuru.webutils.JavaScriptObject;
import kiway.cn.webviewjszhuru.webutils.JavascriptFunction;
import kiway.cn.webviewjszhuru.webutils.MyWebChromeClient;
import kiway.cn.webviewjszhuru.webutils.MyWebViewClient;
import kiway.cn.webviewjszhuru.webutils.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.ResultMessage888;
import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.ResultMessage999;
import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.messageWhat2;
import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.messageWhat4;
import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.messageWhat5;
import static kiway.cn.webviewjszhuru.webutils.JavascriptFunction.complete;
import static kiway.cn.webviewjszhuru.webutils.JavascriptFunction.success;

public class WebActivity extends AppCompatActivity implements Callback, AudioRecoderUtils.OnAudioStatusUpdateListener {
    public WebView wv;
    public App app;
    private ImageView mImageView;
    private TextView mTextView;
    public AudioRecoderUtils mAudioRecoderUtils;
    public PopupWindowFactory mPop;
    public RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        app = (App) getApplicationContext();
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        initData();
        load();
        initSoundRecord();
    }

    private void initData() {
        wv = (WebView) findViewById(R.id.wv);
        //跨域问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            wv.getSettings().setAllowUniversalAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = wv.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(wv.getSettings(), true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        int screenDensity = getResources().getDisplayMetrics().densityDpi ;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM ;
        switch (screenDensity){
            case DisplayMetrics.DENSITY_LOW :
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break ;
        }
        settings.setDefaultZoom(zoomDensity);
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.setInitialScale(25);
        wv.setWebViewClient(new MyWebViewClient(this));
        wv.setWebChromeClient(new MyWebChromeClient(this));
        wv.addJavascriptInterface(new JavaScriptObject(this, this), "wx1");//js
    }

    private void load() {
        wv.loadUrl("file:///android_asset/index1.html");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == ResultMessage888) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker
                        .EXTRA_RESULT_ITEMS);
                callBackJs(JavascriptFunction.jsFuntion, success, StringUtils.getJsCallbackArrayStringImg(images));
            } else {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == ResultMessage999 && data != null) { //扫描二维码返回
            String result = data.getStringExtra("result");
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            callBackJs(JavascriptFunction.jsFuntion, success, "'" + result + "'");
        }
    }

    //回调js
    public void callBackJs(final String name, final String callbackType, final String arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wv.loadUrl("javascript:wx.callback." + name + "." + callbackType + "(" + arg + ")");
            }
        });
    }


    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (call.request().url().toString().equals(HttpUploadFile.uploadUserPicUrl)) {
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("");
            callBackJs(JavascriptFunction.jsFuntion, success, StringUtils.getJsCallbackArrayString(arrayList));

        }
    }

    /**********************以下为录音*************************/
    void initSoundRecord() {
        //PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);
        mPop = new PopupWindowFactory(this, view);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(this);
    }

    //开始录音
    public void startRecord() {
        mAudioRecoderUtils.startRecord();
        mPop.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
    }

    //停止录音
    public void stopRecord() {
        mPop.dismiss();
        mAudioRecoderUtils.stopRecord();
    }

    @Override
    public void onUpdate(double db, long time) {
        mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
        if (TimeUtils.long2String(time).equals("01:00")) {
            mPop.dismiss();
            mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
        } else {
            mTextView.setText(TimeUtils.long2String(time));
        }
    }

    @Override
    public void onStop(String filePath) {
        mTextView.setText(TimeUtils.long2String(0));
        kiway.cn.webviewjszhuru.downloadFile.Logger.log(filePath);
        try {
            String filePat = new JSONObject().put("localId",filePath).toString();
            callBackJs("event.onVoiceRecordEnd", complete, filePat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**********************以上为录音*************************/

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case messageWhat2:
                    ArrayList<String> arrayList = new ArrayList<String>();
                    arrayList.add(msg.obj.toString());
                    callBackJs(JavascriptFunction.jsFuntion, success, StringUtils.getJsCallbackArrayString(arrayList));
                    break;
                case messageWhat5:
                    startRecord();
                    break;
                case messageWhat4:
                    stopRecord();
                    break;
            }
        }
    };
}
