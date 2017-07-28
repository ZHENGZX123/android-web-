package kiway.cn.webviewjszhuru.webutils;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kiway.cn.webviewjszhuru.WebActivity;
import kiway.cn.webviewjszhuru.downloadFile.FileUtils;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MyWebViewClient extends WebViewClient {
    WebActivity activity;

    public MyWebViewClient(WebActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        view.loadUrl("javascript:" + FileUtils.loadAsset("demo.js", activity));
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        view.loadUrl("javascript:(function(){for(var key in wx){wx1[key]=wx[key]};window.wx=window.wx1;})()");
    }
}
