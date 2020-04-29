package zgt.com.example.myzq.model.common.personal_center;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.utils.CleanDataUtils;

import static org.litepal.LitePalApplication.getContext;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.Tv_cache)
    TextView Tv_cache;
    private ProgressDialog loadDialog ;


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        try {
            String totalCacheSize = CleanDataUtils.getTotalCacheSize(getContext());
            Tv_cache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.Iv_back,R.id.Rl_notice,R.id.Rl_cache,R.id.Rl_upgrade,R.id.Rl_about})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
                break;
            case R.id.Rl_notice:

                break;
            case R.id.Rl_cache:
                try {
                    loadDialog = new ProgressDialog(SettingActivity.this);
                    loadDialog.setMessage("正在清理缓存……");
                    loadDialog.setCancelable(false);
                    loadDialog.show();
                    CleanDataUtils.clearAllCache(getContext());
                    String size = CleanDataUtils.getTotalCacheSize(getContext());
                    Tv_cache.setText(size);
                    if(loadDialog.isShowing()){
                        loadDialog.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.Rl_upgrade:

                break;
            case R.id.Rl_about:
                startActivity(new Intent().setClass(this,Change_PasswordActivity.class));
                break;

        }
    }

}
