package kiway.cn.webviewjszhuru.webutils;

import com.lzy.imagepicker.bean.ImageItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/6.
 */

public class StringUtils {
    //js返回数据字符串 选择图片时候用到
    public static String getJsCallbackArrayStringImg(ArrayList<ImageItem> arrayList){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("{localIds: ['");
        for (int i=0;i<arrayList.size();i++){
            if (i!=0)
                stringBuffer.append(" , '");
            stringBuffer.append("file://");
            stringBuffer.append(arrayList.get(i).path);
            stringBuffer.append("'");
        }
        stringBuffer.append("]}");//{a:1}
        return stringBuffer.toString();
    }
    //js返回数据字符串
    public static String getJsCallbackArrayString(ArrayList<String> arrayList){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("{localIds: ['");
        for (int i=0;i<arrayList.size();i++){
            if (i!=0)
                stringBuffer.append(" , '");
            stringBuffer.append("file://");
            stringBuffer.append(arrayList.get(i));
            stringBuffer.append("'");
        }
        stringBuffer.append("]}");//{a:1}
        return stringBuffer.toString();
    }
    //预览图片判断点击的位置
    public static int returnUrlPosition( JSONObject data){
        int position=0;
        String item=data.optString("current");
        JSONArray array=data.optJSONArray("urls");
        for (int i=0;i<array.length();i++){
            if (item.equals(array.optString(i)))
                position=i;
        }
        return position;
    }
}
