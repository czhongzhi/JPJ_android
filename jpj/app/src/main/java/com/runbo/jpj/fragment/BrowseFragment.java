package com.runbo.jpj.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chat.entity.JpjDevice;
import com.chat.entity.JpjGroup;
import com.chat.entity.JpjPicRecord;
import com.runbo.jpj.R;
import com.runbo.jpj.adapter.BrowseAdapter;
import com.runbo.jpj.api.ApiErrorCode;
import com.runbo.jpj.api.PanLvApi;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants.BroadCastAction;
import com.runbo.jpj.ui.MainActivity;
import com.runbo.jpj.ui.PicDetailActivity;
import com.runbo.jpj.util.LogUtil;
import com.runbo.jpj.util.PollTimer;
import com.runbo.jpj.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 图片浏览
 */
public class BrowseFragment extends Fragment {

    private PtrClassicFrameLayout mPtrFrame;
    private GridView mGridView;
    private BrowseAdapter adapter;
    private List<JpjPicRecord> datas = new ArrayList<>();
    private String channel_No;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        View empty = inflater.inflate(R.layout.layout_empty,null);

        initData();
        initView(view,empty);

        return view;
    }

    private void initData() {

        JpjGroup group = MyApplication.currJpjGroup;
        JpjDevice device = MyApplication.currJpjDevice;

        if (device == null) {
            if (group == null) {
            } else {
                getJpjPicforNet(group.getGroup_id() + "", "group");
            }
        } else {
            getJpjPicforNet(device.getDeviceID(), "device");
        }
    }

    private void initView(View view,View empty) {
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_framelayout_browse);
        mGridView = (GridView) view.findViewById(R.id.id_browse_gv);
        adapter = new BrowseAdapter(getContext(), datas);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(itemClick);//点击开大图
        mGridView.setOnItemLongClickListener(itemLongClick);//长按手动拍照

        //((ViewGroup)view.getParent()).addView(empty);
        //mGridView.setEmptyView(empty);

        mPtrFrame.setLastUpdateTimeRelateObject(this);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

    }

    private AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getContext(),"点击看大图",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), PicDetailActivity.class);
            JpjPicRecord pic = (JpjPicRecord) adapter.getItem(position);
            intent.putExtra("JpjPicRecord", pic);
            startActivity(intent);
        }
    };

    private AdapterView.OnItemLongClickListener itemLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            JpjPicRecord picRecord = (JpjPicRecord)adapter.getItem(position);
            showPhotographDialog(picRecord.getDeviceID(),picRecord.getUser_name());
            return true;
        }
    };

    private void getJpjPicforNet(String id, String type) {
        MyApplication.mContext.sendBroadcast(new Intent(BroadCastAction.BROADCAST_DIALOG_SHOW));
        MyApplication.getInstance().getApi().queryPicLatestforJpj(id, type, new PicCallBack());
    }

    /**
     * 手动拍照提示
     */
    private void showPhotographDialog(final String device_id, final String name) {
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
    private void photographfoForJpj(String device_id,String device_name,String channel_No){
        MyApplication.getInstance().getApi().photographfoForJpj(device_id,channel_No,new PhotoCallBack(device_id,device_name,channel_No));
    }

    private class PicCallBack extends PanLvApi.ClientAjaxCallback {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);
            MyApplication.mContext.sendBroadcast(new Intent(BroadCastAction.BROADCAST_DIALOG_CANCEL));
            mPtrFrame.refreshComplete();

            String data = ApiErrorCode.getData(t);
            if (data == null) {
                return;
            }

            datas.clear();
            List<JpjPicRecord> pics = JSON.parseArray(data, JpjPicRecord.class);
            datas.addAll(pics);

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
            MyApplication.mContext.sendBroadcast(new Intent(BroadCastAction.BROADCAST_DIALOG_CANCEL));
            mPtrFrame.refreshComplete();
        }
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

}
