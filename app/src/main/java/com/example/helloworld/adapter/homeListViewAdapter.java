package com.example.helloworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helloworld.R;
import com.example.helloworld.bean.HomeSummary;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/22.
 */
public class homeListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HomeSummary> list;

    public homeListViewAdapter(Context context, ArrayList<HomeSummary> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view  == null){
            viewHolder = new ViewHolder();

            view = LayoutInflater.from(context).inflate(R.layout.home_list_item,null);
            viewHolder.tv_classfiy = (TextView) view.findViewById(R.id.tv_classfy);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.textView2);
            viewHolder.tv_summary = (TextView) view.findViewById(R.id.textView5);
            viewHolder.tv_auth = (TextView) view.findViewById(R.id.textView6);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);

            //setTag
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_classfiy.setText(list.get(i).getClassify());
        viewHolder.tv_title.setText(list.get(i).getTitle());
        viewHolder.tv_summary.setText(list.get(i).getSummary());
        viewHolder.tv_auth.setText(list.get(i).getAuth());

        viewHolder.imageView.setImageResource(list.get(i).getImageId());
        return view;
    }

    public final class ViewHolder{
        public TextView tv_classfiy;
        public TextView tv_title;
        public TextView tv_summary;
        public TextView tv_auth;
        public ImageView imageView;
    }
}
