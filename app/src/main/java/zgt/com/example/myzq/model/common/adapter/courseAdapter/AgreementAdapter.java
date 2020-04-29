package zgt.com.example.myzq.model.common.adapter.courseAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zgt.com.example.myzq.R;
import zgt.com.example.myzq.base.MyBaseAdapter;
import zgt.com.example.myzq.bean.order.Agreement;
import zgt.com.example.myzq.model.common.order.WebViewActivity;


/**
 * Created by ThinkPad on 2019/3/29.
 */

public class AgreementAdapter extends MyBaseAdapter<Agreement> {

    public AgreementAdapter(Context context){
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_agreement, null);
            viewHolder = new ViewHolder();
            viewHolder.Tv_attach = convertView.findViewById(R.id.Tv_attach);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."))
        String  s = getItem(position).getFilepath();
//        String[] strings=s.split("\\\\");
        if(getItem(position).getFilename().length()>0){
            viewHolder.Tv_attach.setText("《"+getItem(position).getFilename()+"》");
        }
        viewHolder.Tv_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent().setClass(context, WebViewActivity.class).putExtra("url",s));
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView Tv_attach;
    }
}
