package zgt.com.example.myzq.model.common.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
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

import zgt.com.example.myzq.utils.Log;

/**
 * Created by ThinkPad on 2019/6/4.
 */

public class MyImageBackgroundView extends AppCompatImageView{
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;


    private Paint mPaint;

    private int mWidth;

    private int mHeight;

    private int mRadius;//圆半径

    private RectF mRect;//矩形凹行大小

    private int mRoundRadius;// 圆角大小

    private BitmapShader mBitmapShader;//图形渲染

    private Matrix mMatrix;

    private int mType;// 记录是圆形还是圆角矩形

    public static final int TYPE_CIRCLE = 0;// 圆形
    public static final int TYPE_ROUND = 1;// 圆角矩形
    public static final int TYPE_OVAL = 2;//椭圆形
    public static final int DEFAUT_ROUND_RADIUS = 10;//默认圆角大小


    float width, height;


    //子线程不能操作UI，通过Handler设置图片
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    Log.e("SIZE",bitmap.getAllocationByteCount()+"");
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



    public MyImageBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    public int getType() {
        return mType;
    }


    /**
     * 设置图片类型：圆形、圆角矩形、椭圆形
     * @param mType
     */
    public void setType(int mType) {
        if(this.mType != mType){
            this.mType = mType;
            invalidate();
        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 如果是绘制圆形，则强制宽高大小一致
//        if (mType == TYPE_CIRCLE) {
//            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
//            mRadius = mWidth / 2;
//            setMeasuredDimension(mWidth, mWidth);
//        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMatrix = new Matrix();
        mRoundRadius = DEFAUT_ROUND_RADIUS;
        if (null == getDrawable()) {
            return;
        }
        //第一步
        setBitmapShader();

//        if (mType == TYPE_CIRCLE) {
//            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
//        } else if (mType == TYPE_ROUND) {
            canvas.drawRoundRect(mRect, mRoundRadius, mRoundRadius, mPaint);
//        }else if(mType == TYPE_OVAL){
//            canvas.drawOval(mRect, mPaint);
//        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new RectF(0, 0, getWidth(), getHeight());
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 设置BitmapShader
     */
    private void setBitmapShader() {
        Drawable drawable = getDrawable();
        if (null == drawable) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(drawable);

        // 将bitmap作为着色器来创建一个BitmapShader
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (mType == TYPE_CIRCLE) {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = mWidth * 1.0f / bSize;

        } else if (mType == TYPE_ROUND || mType == TYPE_OVAL) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);

    }

    /**
     * 设置圆角大小
     * @param mRoundRadius
     */
    public void setRoundRadius(int mRoundRadius) {
        if(this.mRoundRadius != mRoundRadius){
            this.mRoundRadius = mRoundRadius;
            invalidate();
        }

    }




    public MyImageBackgroundView(Context context) {
        super(context);
    }

    public MyImageBackgroundView(Context context, AttributeSet attrs) {
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
