package kiway.cn.webviewjszhuru.downloadFile;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Android on 2016/4/20.
 */
public class FileUtils {


    //创建私有文件夹
    public static String createZipFloder() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "webviewDemo");
        if (!file.exists())
            file.mkdirs();
        return file.getAbsolutePath();
    }

    //创建私有文件夹
    public static String createDocFloder() {
        File file1 = new File(createZipFloder(), "img");
        if (!file1.exists())
            file1.mkdirs();
        return file1.getAbsolutePath();
    }
    public static String loadAsset(String path, Context context) {
        if (context == null || TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
            return readStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        try {
            StringBuilder builder = new StringBuilder(inputStream.available() + 10);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] data = new char[4096];
            int len = -1;
            while ((len = bufferedReader.read(data)) > 0) {
                builder.append(data, 0, len);
            }

            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
            }
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
            }
        }

        return "";
    }
}


