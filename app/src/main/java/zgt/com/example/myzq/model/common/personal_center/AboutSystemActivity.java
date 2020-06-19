package zgt.com.example.myzq.model.common.personal_center;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.model.common.permission.PermissionUtils;
import zgt.com.example.myzq.model.common.permission.request.IRequestPermissions;
import zgt.com.example.myzq.model.common.permission.request.RequestPermissions;
import zgt.com.example.myzq.utils.CommonUtil;
import zgt.com.example.myzq.utils.StatusBarUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class AboutSystemActivity extends BaseActivity {

    @BindView(R.id.Tv_version)
    TextView Tv_version;

    @BindView(R.id.Tv_phoneNum)
    TextView Tv_phoneNum;

    IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_system;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        StatusBarUtil.statusBarLightMode(this);
        Tv_version.setText("v"+CommonUtil.getVersionName());
        String str_2 = "0755-22671286";
        Tv_phoneNum.setText(Html.fromHtml( "<font color='#00d4b4'>" + "<u>" + str_2 + "</u>" + "</font>"));
    }

    @OnClick({R.id.Iv_back,R.id.Tv_phoneNum,R.id.Tv_agreement,R.id.Tv_privacy})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
                break;
            case R.id.Tv_phoneNum:
                if(!requestPermissions()){
                    ToastUtil.showShortToast(AboutSystemActivity.this,"请授权电话权限");
                    return;
                }else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData( Uri.parse("tel:"+Tv_phoneNum.getText().toString().trim()));
                    startActivity(intent);
                }
                break;
            case R.id.Tv_agreement:
                startActivity(new Intent().setClass(AboutSystemActivity.this,AgreementActivity.class));
                break;
            case R.id.Tv_privacy:
                startActivity(new Intent().setClass(AboutSystemActivity.this,PrivacyActivity.class));

                break;
        }

    }

    //请求权限
    private boolean requestPermissions(){
        //需要请求的权限
        String[] permissions = {Manifest.permission.CALL_PHONE};
        //开始请求权限
        return requestPermissions.requestPermissions(
                this,
                permissions,
                PermissionUtils.ResultCode1);
    }

}
