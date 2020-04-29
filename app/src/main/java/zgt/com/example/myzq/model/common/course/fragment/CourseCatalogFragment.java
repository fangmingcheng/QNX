package zgt.com.example.myzq.model.common.course.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.BaseFragment;
import zgt.com.example.myzq.bean.classes.Course;
import zgt.com.example.myzq.bean.classes.CourseCatalog;
import zgt.com.example.myzq.bean.classes.CourseContent;
import zgt.com.example.myzq.bean.classes.CourseDetail;
import zgt.com.example.myzq.model.common.adapter.courseAdapter.CourseCatalogAdapter;
import zgt.com.example.myzq.model.common.course.CourseDetailActivity;
import zgt.com.example.myzq.model.common.custom_view.MyJzvdStd;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseCatalogFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.Tv_live)
    TextView Tv_live;
    @BindView(R.id.Tv_video)
    TextView Tv_video;
    @BindView(R.id.Tv_course)
    TextView Tv_course;
    @BindView(R.id.Tv_video_1)
    TextView Tv_video_1;

    MyJzvdStd jz_video;

    private LinearLayoutManager mLayoutManager;

    private List<CourseCatalog> list = new ArrayList<>();
    private CourseCatalogAdapter adapter;

    private Course course;
    private CourseDetail courseDetail;

//    public  Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//                    Bundle bundle = msg.getData();
//                    Map map=new HashMap();
//                    int code = bundle.getInt("code");
//                    if(code == 1){//跳转直播界面
//
//                    }else if(code == 2){
//                        getData(course.getUuid(),2);
//                    }else if(code == 3){
//                        getData(course.getUuid(),3);
//                    }
//                    break;
//            }
//
//        }
//    };

    public CourseCatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_course_catalog;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        jz_video=getActivity().findViewById(R.id.jz_video);
        if(courseDetail!=null) {
            Tv_live.setText("直播(" + courseDetail.getLiveCount() + ")");
            Tv_video.setText("视频(" + courseDetail.getVideoCount() + ")");
            Tv_course.setText("课件(" + courseDetail.getFileCount() + ")");
            Tv_video_1.setText("音频(" + courseDetail.getAudioCount() + ")");
        }

        initRecyclerView();
    }
    private void initRecyclerView(){
//    设置增加删除item的动画效果
//        recyclerview.setItemAnimator(new DefaultItemAnimator());
//    瀑布流
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);
        adapter = new CourseCatalogAdapter(getActivity(), list,courseDetail.getIsbuy());
        recyclerview.setAdapter(adapter);
//        //设置 Footer 为 球脉冲
        adapter.setOnItemClickListener(new CourseCatalogAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view,CourseCatalogAdapter.ViewName viewName,int position) {
//                int position = recyclerview.getChildAdapterPosition(view);
                List<CourseContent> courseContents =  list.get(position).getList();
                switch (view.getId()){
                    case R.id.Ll_catalog:
                        if(list.get(position).isOpen()){
                            list.get(position).setOpen(false);
                        }else {
                            list.get(position).setOpen(true);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                }

            }
        });

    }



    @Override
    public void initToolBar() {

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        list=((CourseDetailActivity)activity).getcourseCatalogList();
        courseDetail = ((CourseDetailActivity)activity).getCourseDetail();
    }


}
