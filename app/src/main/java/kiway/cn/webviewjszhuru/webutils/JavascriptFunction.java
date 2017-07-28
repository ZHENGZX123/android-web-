package kiway.cn.webviewjszhuru.webutils;

/**
 * Created by Administrator on 2017/6/6.
 */

public class JavascriptFunction {
    //js传过来的办法名
    public static String jsFuntion = "";
    public static String success = "success";
    public static String fail = "fail";
    public static String cancel = "cancel";
    public static String complete = "complete";
    //往网页注入js方法
    public final static String arr = "   var arr = [" +
            "'scanQRCode'," +
            "'chooseImage'," +
            "'previewImage'," +
            "'downloadImage'];\n";
    //for循环注入
    public final static String function = "javascript:(function(){" +
            "   wx.callback = {};" +
            arr +
            "   for(var i in arr){\n" +
            //" alert(arr[i]);" +
            "        var methodName = arr[i];\n" +
            "        var _methodName = '_'+methodName;\n" +
            "        wx.callback[methodName] = {success:function(){},fail:function(){}};" +
            "        wx[methodName] = (function(methodName, _methodName){\n" +
            "            return function(arg){\n" +
            "            if(arg){\n" +
            "                wx.callback[methodName].success = arg.success;\n" +
            "                wx.callback[methodName].fail = arg.fail;\n" +
            "                delete arg.success;\n" +
            "                delete arg.fail;\n" +
            "                wx[_methodName](methodName, JSON.stringify(arg));\n" +
            "            } else {\n" +
            "                wx[_methodName]();\n" +
            "            }\n" +
            "              }" +
            "        })(methodName, _methodName)\n" +
            "    }"
            + "})()";


}
