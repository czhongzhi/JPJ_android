package com.runbo.jpj.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.entity.JpjPicRecord;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.runbo.jpj.R;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.util.AnimImgLoadingListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by czz on 2017/3/24.
 */
public class BrowseAdapter extends BaseAdapter {
    private List<JpjPicRecord> datas;
    private Context context;
    private LayoutInflater inflater;

    public static final DateFormat Day_Format = new SimpleDateFormat("MM/dd HH:mm");


    public BrowseAdapter(Context context, List<JpjPicRecord> datas){
        this.datas = datas;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return datas == null?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas == null?null:datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PicViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_browse_pic,null);
            holder = new PicViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (PicViewHolder) convertView.getTag();
        }
        JpjPicRecord pic = (JpjPicRecord) getItem(position);

        holder.item_name.setText(pic.getUser_name());
        holder.item_type.setText((pic.getChannel_No() == 1 ? "主":"副"));

        long time = (long) pic.getPictureCaptureDT();
        if(time != 0){
            time *= 1000;
            String temp = Day_Format.format(new Date(time));
            holder.item_time.setText(temp);
        }

        String picUrl = Constants.ApiUrl.tBASE_PIC_URL + pic.getThumbWebURL();
        ImageLoader.getInstance().displayImage(picUrl,holder.pic,
                MyApplication.options,new AnimImgLoadingListener());

        return convertView;
    }


    private class PicViewHolder{
        private ImageView pic;
        private TextView item_name;
        private TextView item_type;
        private TextView item_time;

        public PicViewHolder(View view){
            pic = (ImageView) view.findViewById(R.id.id_item_pic);
            item_name = (TextView) view.findViewById(R.id.id_item_name);
            item_type = (TextView) view.findViewById(R.id.id_item_type);
            item_time = (TextView) view.findViewById(R.id.id_item_time);


        }

    }
}
