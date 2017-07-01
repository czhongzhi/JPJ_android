package com.runbo.jpj.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;


import com.alibaba.fastjson.JSON;
import com.chat.entity.JpjDevice;
import com.chat.entity.JpjGroup;
import com.runbo.jpj.R;
import com.runbo.jpj.adapter.GroupAdapter;
import com.runbo.jpj.api.ApiErrorCode;
import com.runbo.jpj.api.PanLvApi;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants.BroadCastAction;
import com.runbo.jpj.preferences.LoginPrefereces;
import com.runbo.jpj.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域群组
 */
public class GroupFragment extends Fragment{
    private ExpandableListView expandableListView;
    private GroupAdapter exAdapter;

    private List<JpjGroup> jpjGroups = new ArrayList<>();

    private boolean isFirstOpenView = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.ex_listview_layout);

        exAdapter = new GroupAdapter(MyApplication.mContext,jpjGroups);
        expandableListView.setAdapter(exAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Toast.makeText(ExListViewActivity.this,"groupPosition - "+groupPosition,Toast.LENGTH_SHORT).show();
                //在这里保存打开的组id

                return false;
            }
        });

    }

    private void initData() {
        jpjGroups.clear();
        jpjGroups.addAll(MyApplication.getInstance().DbService.queryJpjGroups());
        initSelectStatus(jpjGroups);
        exAdapter.notifyDataSetChanged();
        MyApplication.getInstance().getCurrSelectEntity();
        sendBroadReceiver();

        if(!isFirstOpenView){
            getJpjGroupforNet();
            isFirstOpenView = true;
        }
    }

    private void getJpjGroupforNet(){
        String userid = LoginPrefereces.getNumber(getContext());
        MyApplication.getInstance().getApi().queryGroupforJpj(userid,new GroupCallBack());
    }

    private class  GroupCallBack extends PanLvApi.ClientAjaxCallback{
        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);
            //关闭等待dialog

            String data = ApiErrorCode.getData(t);
            if(data == null){
                return;
            }

            jpjGroups.clear();
            List<JpjGroup> groups = JSON.parseArray(data,JpjGroup.class);
            jpjGroups.addAll(groups);

            initSelectStatus(jpjGroups);

            List<String> groupIdList = MyApplication.getInstance().DbService.queryJpjGidList();

            for(JpjGroup g : jpjGroups){
                MyApplication.getInstance().DbService.updataJpjGroup(g);
                if(groupIdList.size() > 0){
                    try{
                        groupIdList.remove(String.valueOf(g.getGroup_id()));
                    }catch (Exception e){}
                }
                List<JpjDevice> devices = g.getJpjDevices();
                for(JpjDevice d : devices){
                    MyApplication.getInstance().DbService.updataJpjDevice(d);
                }
            }

            if(groupIdList.size() > 0){
                for(String grouid : groupIdList){
                    MyApplication.getInstance().DbService.deleteInvalidData(String.valueOf(grouid));
                }
            }

            exAdapter.notifyDataSetChanged();
            MyApplication.getInstance().getCurrSelectEntity();
            sendBroadReceiver();
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
            LogUtil.e("GroupCallBack onFailure "+strMsg);
            //关闭等待dialog
        }

        @Override
        public void onStart() {
            super.onStart();
            //开启等待dialog
        }
    }

    private void sendBroadReceiver() {
        int dID = MyApplication.getInstance().getCurrDeviceID();
        Intent intent = new Intent(BroadCastAction.BROADCASE_MAIN_TIILE);
        JpjGroup g = MyApplication.currJpjGroup;
        JpjDevice d = MyApplication.currJpjDevice;
        if(dID == -1){
            intent.putExtra("main_title",(g == null ? "- - - -" : g.getGroup_name()));
        }else{
            String gNume = (g == null ? "- - - -" : g.getGroup_name());
            String dNume = (d == null ? "-" : d.getUser_name());
            intent.putExtra("main_title",gNume+"/"+dNume);
        }
        MyApplication.mContext.sendBroadcast(intent);
    }


    /**
     * 初始化选中状态
     * @param jpjGroups
     */
    private void initSelectStatus(List<JpjGroup> jpjGroups){
        if (jpjGroups == null || jpjGroups.size() == 0){
            return;
        }
        int currGroupID = MyApplication.getInstance().getCurrGroupID();
        int currDeviceID = MyApplication.getInstance().getCurrDeviceID();
        if (currGroupID == -1){//默认选中第一组
            currGroupID = jpjGroups.get(0).getGroup_id();
            MyApplication.setSelectedGroupItem(currGroupID);
            MyApplication.getInstance().setCurrGroupID(currGroupID);
        }else{//
            if(currDeviceID == -1){
                for(JpjGroup g : jpjGroups){
                    if(currGroupID == g.getGroup_id()){
                        currGroupID = g.getGroup_id();
                        MyApplication.setSelectedGroupItem(currGroupID);
                        return;
                    }
                }
                currGroupID = jpjGroups.get(0).getGroup_id();
                MyApplication.setSelectedGroupItem(currGroupID);
                MyApplication.getInstance().setCurrGroupID(currGroupID);
            }else{
                for(JpjGroup g : jpjGroups){
                    if(currGroupID == g.getGroup_id()){
                        for(JpjDevice d : g.getJpjDevices()){
                            if(currDeviceID == d.getDeviceIndex()){
                                MyApplication.setSelectedChildItem(currGroupID,currDeviceID);
                                return;
                            }
                        }
                        currGroupID = g.getGroup_id();
                        MyApplication.setSelectedGroupItem(currGroupID);
                        MyApplication.getInstance().setCurrDeviceID(-1);
                        return;
                    }
                }
                currGroupID = jpjGroups.get(0).getGroup_id();
                MyApplication.setSelectedGroupItem(currGroupID);
                MyApplication.getInstance().setCurrGroupID(currGroupID);
            }
        }
    }

}
