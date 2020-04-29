package zgt.com.example.myzq.model.common.home.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.Catalog;
import zgt.com.example.myzq.bean.Courseware;
import zgt.com.example.myzq.model.common.adapter.GridViewAdapter;
import zgt.com.example.myzq.model.common.custom_view.MyGridView;
import zgt.com.example.myzq.model.common.custom_view.MyJzvdStd;
import zgt.com.example.myzq.model.common.home.CoursewareDetailActivity;
import zgt.com.example.myzq.utils.SPUtil;
import zgt.com.example.myzq.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogFragment extends BaseFragment {

    @BindView(R.id.gridview)
    MyGridView gridview;
    GridViewAdapter adapter;
    Courseware courseware;
    MyJzvdStd jz_video;

    private List<Catalog> list=new ArrayList<>();

    public CatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        list= ((CoursewareDetailActivity)activity).getList1();
        courseware=((CoursewareDetailActivity)activity).getCourseware();
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//       if(courseware==null){
//
//       }else {
//           if(courseware.getPrice()>0){
//               Tv_money.setText( Html.fromHtml("全套课件：<font color='#FF0000'>"+courseware.getPrice()+"</font>元"));
////            Html.fromHtml("全套课件：<font color='#FF0000'>"+courseware.getPrice()+"</font>元");
//           }else {
//               Ll_all_catalog.setVisibility(View.GONE);
//           }
//       }
        adapter=new GridViewAdapter(list,getActivity());
        if(list.size()>0){
            adapter.setSelection(0);
        }
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);
                adapter.notifyDataSetChanged();
                jz_video=getActivity().findViewById(R.id.jz_video);
                if(list.get(position).getIsfee()==0) {
                    jz_video.setUp(SPUtil.getServerAddress().substring(0, (SPUtil.getServerAddress().length() - 5)) + list.get(position).getVideourl(), "");
                }else {
                    ToastUtil.showShortToast(getActivity(),"该章节需要付费观看");
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_catalog;
    }
}
