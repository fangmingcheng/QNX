package zgt.com.example.myzq.utils;

import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.net.URL;

/**
 * Created by ThinkPad on 2019/5/28.
 */

public class LoadImageUtil {
    public static Drawable loadImageFromNetwork(String urladdr) {
// TODO Auto-generated method stub
        Drawable drawable = null;
        try{
            //judge if has picture locate or not according to filename
            drawable = Drawable.createFromStream(new URL(urladdr).openStream(),"image.jpg");
        }catch(IOException e){
            Log.d("test",e.getMessage());
        }
        if(drawable == null){
            Log.d("test","null drawable");
        }else{
            Log.d("test","not null drawable");

        }
        return drawable;
    }
}
