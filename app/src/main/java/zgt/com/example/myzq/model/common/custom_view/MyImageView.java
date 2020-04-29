package zgt.com.example.myzq.model.common.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ThinkPad on 2019/6/4.
 */

public class MyImageView extends AppCompatImageView{
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;

    private Paint mPaintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
      private Bitmap mRawBitmap;
      private BitmapShader mShader;
      private Matrix mMatrix = new Matrix();

    //子线程不能操作UI，通过Handler设置图片
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
//                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(),"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
     protected void onDraw(Canvas canvas) {
                 Bitmap rawBitmap = getBitmap(getDrawable());
                 if (rawBitmap != null){
                         int viewWidth = getWidth();
                        int viewHeight = getHeight();
                         int viewMinSize = Math.min(viewWidth, viewHeight);
                         float dstWidth = viewMinSize;
                         float dstHeight = viewMinSize;
                         if (mShader == null || !rawBitmap.equals(mRawBitmap)){
                                 mRawBitmap = rawBitmap;
                                 mShader = new BitmapShader(mRawBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                             }
                         if (mShader != null){
                                 mMatrix.setScale(dstWidth / rawBitmap.getWidth(), dstHeight / rawBitmap.getHeight());
                                 mShader.setLocalMatrix(mMatrix);
                             }
                         mPaintBitmap.setShader(mShader);
                        float radius = viewMinSize / 2.0f;
                         canvas.drawCircle(radius, radius, radius, mPaintBitmap);
                     } else {
                         super.onDraw(canvas);
                     }
             }

             private Bitmap getBitmap(Drawable drawable){
                 if (drawable instanceof BitmapDrawable){
                         return ((BitmapDrawable)drawable).getBitmap();
                     } else if (drawable instanceof ColorDrawable){
                         Rect rect = drawable.getBounds();
                                 int width = rect.right - rect.left;
                         int height = rect.bottom - rect.top;
                         int color = ((ColorDrawable)drawable).getColor();
                         Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                         Canvas canvas = new Canvas(bitmap);
                         canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
                         return bitmap;
                     } else {
                         return null;
                     }
             }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置网络图片
    public void setImageURL(final String path) {
        //开启一个线程用于联网
        new Thread() {
            @Override
            public void run() {
                try {
                    //把传过来的路径转成URL
                    URL url = new URL(path);
                    //获取连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //使用GET方法访问网络
                    connection.setRequestMethod("GET");
                    //超时时间为10秒
                    connection.setConnectTimeout(10000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        //使用工厂把网络的输入流生产Bitmap
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        //利用Message把图片发给Handler
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        inputStream.close();
                    }else {
                        //服务启发生错误
                        handler.sendEmptyMessage(SERVER_ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //网络连接错误
                    handler.sendEmptyMessage(NETWORK_ERROR);
                }
            }
        }.start();
    }

}
