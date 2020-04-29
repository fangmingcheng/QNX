package zgt.com.example.myzq.model.common.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseActivity;
import zgt.com.example.myzq.bean.ZXItemBean;
import zgt.com.example.myzq.model.common.adapter.Myfragment_ViewpageAdapter;
import zgt.com.example.myzq.model.common.information.fragment.TTFragment;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.Log;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

public class InformationActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.myviewpager)
    ViewPager myviewpager;
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    private ZXItemBean zxItemBean;
    private List<ZXItemBean> zxlist = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    // fragment的集合
    private ArrayList<Fragment> list;
    // viewpage适配器
    private Myfragment_ViewpageAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_information;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        StatusBarUtil.statusBarLightMode(this);
        getData();

    }

    private void setData(List<ZXItemBean> zxlist){
        for(int i = 0;i<zxlist.size();i++){
            titleList.add(zxlist.get(i).getName());
//            tab_layout.addTab(tab_layout.newTab().setText(zxlist.get(i).getName()));
        }
        list = new ArrayList<Fragment>();
        if(zxlist.size()>0){
            for(int j=0;j<zxlist.size();j++){
                list.add(TTFragment.getTT(zxlist.get(j).getUuid()));
            }
        }

        adapter = new Myfragment_ViewpageAdapter(getSupportFragmentManager(),list,titleList);
        myviewpager.setOffscreenPageLimit(list.size());
        myviewpager.setAdapter(adapter);
        tab_layout.setupWithViewPager(myviewpager);

        // viewpage监听事件，重写onPageSelected()方法，实现左右滑动页面
//        myviewpager.setOnPageChangeListener(this);
        // 默认第一页
        // 初始按钮颜色
    }


    @OnClick({R.id.Iv_back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.Iv_back:
                finish();
                break;
        }
    }


    @Override
    public void onPageSelected(int arg0) {
        Log.d("InformationFragment",arg0+"");
        // 根据每次选中的按钮，重置颜色
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }


    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"getClassZxList.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json = jsonObject.getJSONObject("data");
                        JSONArray array = json.getJSONArray("zxList");
                        for(int i = 0;i<array.length();i++){
                            zxItemBean = new ZXItemBean();
                            zxItemBean.setName(array.getJSONObject(i).getString("name"));
                            zxItemBean.setUuid(array.getJSONObject(i).getString("uuid"));
                            zxlist.add(zxItemBean);
                        }
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData(zxlist);
                            }
                        });


                    } else if(a==-1){

                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent().setClass(InformationActivity.this, LoginActivity.class));
                                finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                      runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(InformationActivity.this, msg);
                            }
                        });
                    }
                } catch (JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });
                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(InformationActivity.this, "网络连接异常");
                    }
                });
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

}
