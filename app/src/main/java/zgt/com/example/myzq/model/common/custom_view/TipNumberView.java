package zgt.com.example.myzq.model.common.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * Created by ThinkPad on 2019/6/23.
 */

public class TipNumberView extends AppCompatTextView {
    private Paint mBgPaint ;
    PaintFlagsDrawFilter pfd;
    public TipNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.RED);
        mBgPaint.setAntiAlias(true);

        pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    }
    public TipNumberView(Context context) {
        this(context,null);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到测量的高度和宽度
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int max = Math.max(measuredWidth, measuredHeight);
        //设置控件区域大小
        setMeasuredDimension(max, max);
    }

    //设置背景颜色
    @Override
    public void setBackgroundColor(int color){
        mBgPaint.setColor(color);
    }

    /**
     * 设置通知个数显示
     * @param text
     */
    public void setNotifiText(int text){
        setText(text+"");
    }

    public void setNotifiText(String text){
        setText(text);
    }

    //绘图
    @Override
    public void draw(Canvas canvas) {
        //设置绘图无锯齿
        canvas.setDrawFilter(pfd);
        canvas.drawCircle(getWidth()/2, getHeight()/2, Math.max(getWidth()/2, getHeight())/2, mBgPaint);
        super.draw(canvas);
    }
}
