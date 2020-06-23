package zgt.com.example.myzq.utils;

import android.content.Context;
import android.content.SharedPreferences;

import zgt.com.example.myzq.MyApp;


/**
 * Created by hasee on 2017/9/13.
 * SP缓存工具类
 */

public class SPUtil {

    public static boolean isRead(){
        return getConfigSharedPreferences().getBoolean("isRead", false);
    }

    public static boolean isRead_1(){
        return getConfigSharedPreferences().getBoolean("isRead_1", false);
    }

    public static String getToken() {
        return getLoginSharedPreferences().getString("token", "");
    }
      // http://192.168.0.109:8080/zgstock/app/   http://118.190.105.66:12588/app/
    //https://api.zgziben.com/app/     http://test.zgziben.com/app/
    public static String getServerAddress() {
        return getLoginSharedPreferences().getString("address", "https://api.zgziben.com/app/");
    }

    public static String getAccess_Token() {
        return getLoginSharedPreferences().getString("access_token", "C9EDCE796E074088A19D6F21FE83BBEE2020060315200522C521F0");
    }

    public static String getHTMLAddress() {
        return getLoginSharedPreferences().getString("address", "http://stock.bdcgw.cn/app/");
    }

    //会员id
    public static String getMemberid() {
        return getLoginSharedPreferences().getString("memberid", "");
    }
    //会员账号
    public static String getUsername() {
        return getLoginSharedPreferences().getString("username", "");
    }
    //会员昵称
    public static String getNickname() {
        return getLoginSharedPreferences().getString("nickname", "");
    }
    //会员功能模块权限代码串
    public static String getModulecode() {
        return getLoginSharedPreferences().getString("modulecode", "");
    }

    //会员功能模块权限名称串
    public static String getModulename() {
        return getLoginSharedPreferences().getString("modulename", "");
    }

    //0非金投顾，1金投顾
    public static int getMembertype() {
        return getLoginSharedPreferences().getInt("membertype", 0);
    }

    //版本名称
    public static String getEmail() {
        return getLoginSharedPreferences().getString("email", "");
    }

    //版本名称
    public static String getTypename() {
        return getLoginSharedPreferences().getString("typename", "");
    }

    //老师主键id
    public static String getTeacherid() {
        return getLoginSharedPreferences().getString("teacherid", "");
    }
    //服务开始时间
    public static String getStartdate() {
        return getLoginSharedPreferences().getString("startdate", "");
    }
    //服务结束时间
    public static String getEnddate() {
        return getLoginSharedPreferences().getString("enddate", "");
    }

    //服务结束时间
    public static String getHeadimg() {
        return getLoginSharedPreferences().getString("headimg", "");
    }


    public static SharedPreferences getLoginSharedPreferences() {
        return MyApp.getInstance().getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    public static SharedPreferences getConfigSharedPreferences() {
        return MyApp.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
    }
}
