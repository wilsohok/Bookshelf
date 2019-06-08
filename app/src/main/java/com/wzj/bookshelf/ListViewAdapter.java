package com.wzj.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by wjh on 2019/5/18.
 */

public class ListViewAdapter extends BaseAdapter {

    private List<Book> data;

    private LayoutInflater layoutInflater;

    private Context context;

    public ListViewAdapter(Context context , List<Book> data)
    {
        this.context = context;

        this.data = data;

        this.layoutInflater = LayoutInflater.from(context);

    }

    //组件定义
    public final class Zujian{

        public ImageView cover;

        public TextView name;

        public TextView introduct;

    }

    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Zujian zujian = null;

        if(convertView == null)
        {
            zujian = new Zujian();

            convertView = layoutInflater.inflate(R.layout.book_list_item,null);

            zujian.cover = (ImageView)convertView.findViewById(R.id.book_cover);
            zujian.cover.setBackgroundResource(data.get(position).getCover());

            zujian.name = (TextView)convertView.findViewById(R.id.book_name);
            zujian.name.setText(data.get(position).getName());

            zujian.introduct = (TextView)convertView.findViewById(R.id.book_introduction);
            zujian.introduct.setText(data.get(position).getIntroduct());

            convertView.setTag(zujian);

        }
        else
        {
            zujian = (Zujian)convertView.getTag();
        }

        return convertView;
    }
}
