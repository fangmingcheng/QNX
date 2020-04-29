package zgt.com.example.myzq.utils;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import zgt.com.example.myzq.bean.HttpResult;
import zgt.com.example.myzq.model.common.login.LoginActivity;

public class JavaScriptinterface {
    Context context;
    public JavaScriptinterface(Context c) {
        context= c;
    }

    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void dologin(String s) {
        HttpResult result=new HttpResult();
        //"http://192.168.91.102:8080/hyjj/hsStock/toList?pageName=zixuan&token=&mark=&code=&type="+type
        Log.e("JavaScriptinterface","成功");

        try {
            JSONObject jsonObject = new JSONObject(s);
            result=new HttpResult();
            result.setUrl(jsonObject.getString("url"));
            result.setCode(jsonObject.getString("code"));
            result.setMark(jsonObject.getString("mark"));
            result.setType(jsonObject.getString("type"));

        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("result",result);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        Log.e("JavaScriptinterface",s);
        Log.e("JavaScriptinterface",result.getUrl());
    }
}
