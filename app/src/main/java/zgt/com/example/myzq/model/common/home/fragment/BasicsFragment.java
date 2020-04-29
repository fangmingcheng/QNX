package zgt.com.example.myzq.model.common.home.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.Members;
import zgt.com.example.myzq.bean.MyMember;
import zgt.com.example.myzq.model.common.login.LoginActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * 基础版本
 * A simple {@link Fragment} subclass.
 */
public class BasicsFragment extends BaseFragment {

    @BindView(R.id.Tv_price)
    TextView Tv_price;
    @BindView(R.id.Tv_status)
    TextView Tv_status;
    @BindView(R.id.Tv_time)
    TextView Tv_time;
    @BindView(R.id.Tv_content)
    TextView Tv_content;
//    @BindView(R.id.Bt_open)
//    Button Bt_open;

    private List<Members> list=new ArrayList<>();
    private Members members;
    private MyMember myMember;

    public BasicsFragment() {
        // Required empty public constructor
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_basics;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }).start();
    }

    private void setData(){
        Tv_price.setText(Html.fromHtml("价格：<font color='#FFFFFF' size='20dp'>"+list.get(0).getPrice()+"</font>元/年"));
        Tv_content.setText(list.get(0).getIntro());
        if("基础版".equals(SPUtil.getTypename())){
//            Tv_price.setText(Html.fromHtml("价格：<font color='#FFFFFF' size='20dp'>"+list.get(0).getPrice()+"</font>元/年"));
            Tv_status.setText("已开通");
            Tv_time.setText(SPUtil.getStartdate()+" - "+SPUtil.getEnddate());
//            Bt_open.setVisibility(View.GONE);
        }else {
            Tv_status.setVisibility(View.GONE);
            Tv_time.setVisibility(View.GONE);
        }
    }

    private void getData(){
        RequestParams requestParams = new RequestParams(SPUtil.getServerAddress()+"goldtg.do");
        requestParams.setConnectTimeout(30 * 1000);
        requestParams.addParameter("token", SPUtil.getToken());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int a=jsonObject.getInt("result");
                    if (a==1) {
                        JSONObject json=jsonObject.getJSONObject("data");
                        myMember=new MyMember();
                        myMember.setTypename(json.getString("typename"));
                        myMember.setType(json.getInt("type"));
                        myMember.setEnddate(json.getString("enddate"));
                        myMember.setStartdate(json.getString("startdate"));
                        myMember.setTeacherid(json.getString("teacherid"));
                        myMember.setTsort(json.getInt("tsort"));
                        JSONArray jsonArray=json.getJSONArray("productTypelist");
                        for (int i=0;i<jsonArray.length();i++){
                            members=new Members();
                            members.setUuid(jsonArray.getJSONObject(i).getString("uuid"));
                            members.setIntro(jsonArray.getJSONObject(i).getString("intro"));
                            members.setModulenamestr(jsonArray.getJSONObject(i).getString("modulenamestr"));
                            members.setPrice(jsonArray.getJSONObject(i).getInt("price"));
                            members.setTypename(jsonArray.getJSONObject(i).getString("typename"));
                            members.setIntro(jsonArray.getJSONObject(i).getString("intro"));
                            if(myMember.getTypename().equals(members.getTypename())){
                                members.setStartDate(myMember.getStartdate());
                                members.setEndDate(myMember.getEnddate());
                            }
                            list.add(members);
                        }
                        if(getActivity()==null){
                            return;
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });
                    } else if(a==-1){
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("token失效：请重新登陆").setCancelable(false).setNegativeButton("取消", null).
//                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
//                                                getActivity().finish();
//                                                dialog.dismiss();
//                                            }
//                                        }).create().show();
                                startActivity(new Intent().setClass(getActivity(),LoginActivity.class));
                                getActivity().finish();
                            }
                        });

                    }else {

                        final String msg=jsonObject.getString("message");
                        if(getActivity()==null){
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast(getActivity(), msg);
                            }
                        });
                    }
                } catch (JSONException e) {
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(getActivity(), getString(R.string.login_parse_exc));
                        }
                    });

                } finally {

                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(getActivity()==null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast( getActivity(), "网络连接异常");
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
