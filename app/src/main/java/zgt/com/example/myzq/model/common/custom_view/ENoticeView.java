package zgt.com.example.myzq.model.common.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

/**
 * Created by ThinkPad on 2019/7/19.
 */

public class ENoticeView extends ViewFlipper{
    /**
     * 切换的时间间隔
     * */
    private int flipIntervalTime = 3000;
    /**
     * 切换的动画的时间
     * */
    private int durationTime = 500;
    private NoticeAdapter mAdapter;
    private OnItemClickListener listener;

    public ENoticeView(Context context) {
        super(context);
        init();
    }

    public ENoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        setFlipInterval(flipIntervalTime);
        setInAnimation(inFromBottomAnimation());
        setOutAnimation(outToTopAnimation());
        setAutoStart(false);
    }
    private void start(){
        if(this.mAdapter == null||this.mAdapter.getCount() <= 0){
            return;
        }
        removeAllViews();
        //只有数据源大于2条的时候才会开启自动切换
        if(this.mAdapter.getCount() >1)
            setAutoStart(true);
        else
            setAutoStart(false);
        for(int i =0;i<this.mAdapter.getCount();i++){
            View view = this.mAdapter.getView(getContext(),i);
            addView(view);
            if(listener != null){
                view.setTag(i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick((Integer) view.getTag());
                    }
                });
            }
        }
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
    }

    /**
     * 定义从下侧进入的动画效果
     * @return
     */
    protected Animation inFromBottomAnimation() {
        Animation inFromBottomAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromBottomAnimation.setDuration(durationTime);
        inFromBottomAnimation.setInterpolator(new AccelerateInterpolator());
        return inFromBottomAnimation;
    }

    /**
     * 定义从上侧退出的动画效果
     * @return
     */
    protected Animation outToTopAnimation() {
        Animation outToTopAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        outToTopAnimation.setDuration(durationTime);
        outToTopAnimation.setInterpolator(new AccelerateInterpolator());
        return outToTopAnimation;
    }
    /**
     * 设置切换的时间间隔
     * */
    public void setFlipIntervalTime(int flipIntervalTime) {
        this.flipIntervalTime = flipIntervalTime;
    }
    /**
     * 设置切换的动画的时间
     * */
    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }
    /**
     * 设置适配器
     * */
    public void setAdapter(NoticeAdapter mAdapter){
        this.mAdapter = mAdapter;
        start();
    }
    /**
     * 设置Item点击事件
     * */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener  = listener;
        start();
    }
    public interface OnItemClickListener{
        void onClick(int position);
    }
}




