package kiway.cn.webviewjszhuru.webutils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.loader.GlideImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cn.bingoogolapple.qrcode.zxingdemo.ScanActivity;
import kiway.cn.webviewjszhuru.NetWorkUtils.NetWorkUtils;
import kiway.cn.webviewjszhuru.WebActivity;
import kiway.cn.webviewjszhuru.downloadFile.DownLoadFile;
import kiway.cn.webviewjszhuru.downloadFile.HttpUploadFile;
import kiway.cn.webviewjszhuru.map.MapActivity;
import kiway.cn.webviewjszhuru.photoviews.ViewPhotosActivity;
import okhttp3.Callback;

import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.ResultMessage888;
import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.ResultMessage999;
import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.messageWhat4;
import static kiway.cn.webviewjszhuru.webutils.HanderMessageWhat.messageWhat5;
import static kiway.cn.webviewjszhuru.webutils.JavascriptFunction.fail;
import static kiway.cn.webviewjszhuru.webutils.JavascriptFunction.success;

//js回调
public class JavaScriptObject {
    private WebActivity mContxt;
    Callback callback;

    public JavaScriptObject(WebActivity mContxt, Callback callback) {
        this.mContxt = mContxt;
        this.callback = callback;
    }

    //扫码功能
    @JavascriptInterface
    public void _scanQRCode(String name, String fun) {
        JavascriptFunction.jsFuntion = name;
        mContxt.startActivityForResult(new Intent(mContxt, ScanActivity.class), ResultMessage999);
    }

    //选择图片
    @JavascriptInterface
    public void _chooseImage(String name, String fun) {
        JavascriptFunction.jsFuntion = name;
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());// 图片加载器
        imagePicker.setSelectLimit(9);// 设置可以选择几张
        imagePicker.setMultiMode(true);// 是否为多选
        imagePicker.setCrop(true);// 是否剪裁
        imagePicker.setShowCamera(true);// 是否显示摄像
        Intent intent = new Intent(mContxt, ImageGridActivity.class);
        mContxt.startActivityForResult(intent, ResultMessage888);
    }

    //图片预览
    @JavascriptInterface
    public void _previewImage(String name, String fun) {
        Log.e("************", fun);
        JavascriptFunction.jsFuntion = name;
        try {
            JSONObject jsonObject = new JSONObject(fun);
            Bundle bundle = new Bundle();
            bundle.putString("urls",
                    jsonObject.optString("urls"));
            bundle.putInt("position", StringUtils.returnUrlPosition(jsonObject));
            Intent intent = new Intent(mContxt, ViewPhotosActivity.class);
            intent.putExtras(bundle);
            mContxt.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //上传图片
    @JavascriptInterface
    public void _uploadImage(String name, String param) {
        Toast.makeText(mContxt, param, Toast.LENGTH_SHORT).show();
        JavascriptFunction.jsFuntion = name;
        try {
            JSONObject da = new JSONObject(param);
            try {
                mContxt.app.mOkHttpClient.newCall(HttpUploadFile.returnUploadImgRequser(new File(da.optString
                        ("localId")), HttpUploadFile
                        .FileType.Image))
                        .enqueue(callback);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //下载图片
    @JavascriptInterface
    public void _downloadImage(String name, String param) {
        JavascriptFunction.jsFuntion = name;
        try {
            JSONObject data = new JSONObject(param);
            String s = data.optString("serverId");
            DownLoadFile.downoalFile(s, mContxt.app, mContxt.handler, s.split("/")[s.split("/").length - 1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取网络状态
    @JavascriptInterface
    public void _getNetworkType(String name, String param) {
        JavascriptFunction.jsFuntion = name;
        ;
        JSONObject object = new JSONObject();
        try {
            object.put("networkType", NetWorkUtils.getAPNType(mContxt)).toString();
            mContxt.callBackJs(name, success, object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            mContxt.callBackJs(name, fail, object.toString());
        }

    }

    //查询地理地址
    @JavascriptInterface
    public void _openLocation(String name, String param) {
        JavascriptFunction.jsFuntion = name;
        mContxt.startActivity(new Intent(mContxt, MapActivity.class));
    }

    //获取当前地理地址
    @JavascriptInterface
    public void _getLocation(String name, String param) {
        JavascriptFunction.jsFuntion = name;
        try {
            mContxt.callBackJs(name, success, new JSONObject().put("adress", MapActivity.address).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //微信支付
    @JavascriptInterface
    public void _chooseWXPay(String name, String param) {
        JavascriptFunction.jsFuntion = name;
        Toast.makeText(mContxt, "我已经接收到了网页的命令，但是我这边并没有逻辑", Toast.LENGTH_LONG).show();
    }

    //开始录音
    @JavascriptInterface
    public void _startRecord(final String name, String param) {
        JavascriptFunction.jsFuntion = name;
        mContxt.handler.sendEmptyMessage(messageWhat5);
    }

    //停止录音
    @JavascriptInterface
    public void _stopRecord(final String name, String param) {
        JavascriptFunction.jsFuntion = name;
        mContxt.handler.sendEmptyMessage(messageWhat4);
    }

    //分享功能
    @JavascriptInterface
    public void _onMenuShareAppMessage(final String name, String param) {
        JavascriptFunction.jsFuntion = name;
        Toast.makeText(mContxt, "对不起，这边分享逻辑没弄", Toast.LENGTH_SHORT).show();
    }

    //分享功能
    @JavascriptInterface
    public void _checkJsApi(final String name, String param) {
        JavascriptFunction.jsFuntion = name;
        Toast.makeText(mContxt, "跟我说你要检查什么", Toast.LENGTH_SHORT).show();
    }
}
