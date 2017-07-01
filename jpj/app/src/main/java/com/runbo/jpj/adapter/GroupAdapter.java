package com.runbo.jpj.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.entity.JpjDevice;
import com.chat.entity.JpjGroup;
import com.runbo.jpj.R;
import com.runbo.jpj.api.ApiErrorCode;
import com.runbo.jpj.api.PanLvApi;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.util.LogUtil;
import com.runbo.jpj.util.PollTimer;
import com.runbo.jpj.util.ToastUtil;

import java.util.List;

/**
 * Created by czz on 2017/3/30.
 */
public class GroupAdapter extends BaseExpandableListAdapter {
    private List<JpjGroup> groups;

    private Context context;
    private LayoutInflater inflate;

    private String channel_No;

    public GroupAdapter(Context context,List<JpjGroup> groups){
        this.context = context;
        inflate = LayoutInflater.from(context);
        this.groups = groups;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getJpjDevices().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getJpjDevices().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        //return false;
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (GroupViewHolder) convertView.getTag();
        }

        final JpjGroup group = (JpjGroup) getGroup(groupPosition);

        //group被选中操作
        if (MyApplication.selected_g.get(group.getGroup_id())){
            holder.g_item.setBackgroundColor(context.getResources().getColor(R.color.bg_title));
        }else{
            holder.g_item.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        holder.g_tv.setText(group.getGroup_name());
        holder.g_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().setCurrGroupID(group.getGroup_id());
                MyApplication.getInstance().setCurrDeviceID(-1);

                MyApplication.getInstance().getCurrSelectEntity();

                MyApplication.setSelectedGroupItem(group.getGroup_id());
                MyApplication.initSelectedChild();
                notifyDataSetChanged();

                Intent intent = new Intent(Constants.BroadCastAction.BROADCASE_MAIN_TIILE);
                intent.putExtra("main_title",group.getGroup_name());
                context.sendBroadcast(intent);

                Intent intent2 = new Intent(Constants.BroadCastAction.BROADCASE_FM_SWITCH);
                context.sendBroadcast(intent2);
            }
        });
        holder.g_tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,group.getGroup_name(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_child, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ChildViewHolder) convertView.getTag();
        }

        JpjGroup group = (JpjGroup) getGroup(groupPosition);
        final JpjDevice device = (JpjDevice) getChild(groupPosition,childPosition);

        //child被选中操作
        if (MyApplication.selected_c.get(device.getDeviceIndex()) && MyApplication.parentPos == group.getGroup_id()){
            holder.c_item.setBackgroundColor(context.getResources().getColor(R.color.bg_title));
        }else{
            holder.c_item.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        }

        holder.c_tv.setText(device.getUser_name());
        holder.c_tv2.setText(device.getDeviceIndex()+"");
        holder.c_click.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPhotographDialog(device.getDeviceID(),device.getUser_name());
                return true;
            }
        });
        holder.c_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JpjGroup jpjGroup = (JpjGroup) getGroup(groupPosition);
                JpjDevice jpjDevice = (JpjDevice) getChild(groupPosition,childPosition);

                MyApplication.getInstance().setCurrGroupID(jpjGroup.getGroup_id());
                MyApplication.getInstance().setCurrDeviceID(jpjDevice.getDeviceIndex());

                MyApplication.getInstance().getCurrSelectEntity();

                MyApplication.setSelectedChildItem(jpjGroup.getGroup_id(),jpjDevice.getDeviceIndex());
                MyApplication.initSelectedGroup();
                notifyDataSetChanged();

                Intent intent = new Intent(Constants.BroadCastAction.BROADCASE_MAIN_TIILE);
                intent.putExtra("main_title",jpjGroup.getGroup_name()+"/"+jpjDevice.getUser_name());
                MyApplication.mContext.sendBroadcast(intent);

                Intent intent2 = new Intent(Constants.BroadCastAction.BROADCASE_FM_SWITCH);
                MyApplication.mContext.sendBroadcast(intent2);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //return false;
        return  true;
    }

    /**
     * 手动拍照提示
     */
    private void showPhotographDialog(final String device_id,final String name) {
        channel_No = "1";
        AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.mContext);

        LayoutInflater inflater = LayoutInflater.from(MyApplication.mContext);
        final View dialogView = inflater.inflate(R.layout.layout_dialog,null);
        builder.setView(dialogView);

        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText(name + "  是否现在拍照？");
        RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.select_camera_num);

        if (channel_No.equals("1")){
            radioGroup.check(R.id.main);
        }else if (channel_No.equals("2")){
            radioGroup.check(R.id.vice);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.main:
                        channel_No = "1";
                        break;
                    case R.id.vice:
                        channel_No = "2";
                        break;
                }
            }
        });

        builder.setTitle("温馨提示：");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                photographfoForJpj(device_id,name,channel_No);
            }
        });
        builder.create().show();
    }

    /**
     * 手动拍照
     * @param device_id
     * @param channel_No
     */
    private void photographfoForJpj(String device_id,String device_name, String channel_No){
        MyApplication.getInstance().getApi().photographfoForJpj(device_id,channel_No,new PhotoCallBack(device_id,device_name,channel_No));
    }

    private class PhotoCallBack extends PanLvApi.ClientAjaxCallback{
        private String device_id;
        private String channel_No;
        private String device_name;
        public PhotoCallBack(String device_id,String device_name,String channel_No){
            this.device_id = device_id;
            this.channel_No = channel_No;
            this.device_name = device_name;
        }

        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);

            String data = ApiErrorCode.getData(t);
            if (data == null) {
                ToastUtil.show(MyApplication.mContext,ApiErrorCode.getErrCodeDescribe(t));
                return;
            }

            LogUtil.e("PhotoCallBack -- "+data);
            String lastTime = data;
            //开启轮询定时器，查询图片
            PollTimer pollTimer = new PollTimer(device_name,device_id,channel_No,lastTime);
            pollTimer.startTimer();
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
        }
    }

    class GroupViewHolder{
        private ImageView g_im;
        private TextView g_tv;
        private LinearLayout g_item;
        public GroupViewHolder(View view){
            g_im = (ImageView) view.findViewById(R.id.item_group_img);
            g_tv = (TextView) view.findViewById(R.id.item_group_tv);
            g_item = (LinearLayout) view.findViewById(R.id.item_group);
        }
    }

    class ChildViewHolder{
        private ImageView c_im;
        private TextView c_tv,c_tv2;
        private LinearLayout c_item;
        private RelativeLayout c_click;
        public ChildViewHolder(View view){
            c_im = (ImageView) view.findViewById(R.id.item_child_img);
            c_tv = (TextView) view.findViewById(R.id.item_child_tv);
            c_tv2 = (TextView) view.findViewById(R.id.item_child_tv2);
            c_item = (LinearLayout) view.findViewById(R.id.item_child);
            c_click = (RelativeLayout) view.findViewById(R.id.item_child_click);
        }
    }
}
