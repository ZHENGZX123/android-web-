package kiway.cn.webviewjszhuru.webutils;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

import kiway.cn.webviewjszhuru.WebActivity;

/**
 * Created by Administrator on 2017/6/6.
 */

public class MyWebChromeClient extends WebChromeClient {
    WebActivity activity;

    public MyWebChromeClient(WebActivity activity) {
    }
    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        Log.e("***************", message);
        super.onConsoleMessage(message, lineNumber, sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.e("***************", consoleMessage.message());
        return super.onConsoleMessage(consoleMessage);
    }

}
