package zgt.com.example.myzq.model.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread( new Runnable( ) {
            @Override
            public void run() {
                //耗时任务，比如加载网络数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 这里可以睡几秒钟，如果要放广告的话
                        // sleep(3000);
                        startActivity(new Intent().setClass(LunchActivity.this,WelcomeActivity.class));
                        LunchActivity.this.finish();
                    }
                });
            }
        } ).start();
    }
}
