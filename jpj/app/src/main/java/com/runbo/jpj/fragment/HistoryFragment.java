package com.runbo.jpj.fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
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
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.ui.PicDetailActivity;
import com.runbo.jpj.util.LogUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 历史查询
 */
public class HistoryFragment extends Fragment implements View.OnClickListener {
    private TextView startDay, endDay;
    public static final DateFormat Day_Format = new SimpleDateFormat("yyyy-MM-dd");

    private PtrClassicFrameLayout mPtrFrame;
    private GridView mGridView;
    private BrowseAdapter adapter;
    private List<JpjPicRecord> datas = new ArrayList<>();

    private static final int PAGE_NUM = 10;//每次加载个数
    private static int page_index = 0;
    private boolean isLoading = false;
    private long sday_long;
    private long eday_long;


    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        startDay = (TextView) view.findViewById(R.id.id_start_day);
        endDay = (TextView) view.findViewById(R.id.id_end_day);

        startDay.setOnClickListener(this);
        endDay.setOnClickListener(this);
        view.findViewById(R.id.button).setOnClickListener(this);

        Date date = new Date();
        String sday = Day_Format.format(date);
        startDay.setText(sday);

        String eday = getSpecifiedDayBefore(sday);
        endDay.setText(eday);

        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_framelayout_browse);
        mGridView = (GridView) view.findViewById(R.id.id_browse_gv);
        adapter = new BrowseAdapter(getContext(),datas);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(itemClick);
        mGridView.setOnItemLongClickListener(itemLongClick);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                },1300);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,content,header);
            }
        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    if(view.getLastVisiblePosition() == view.getCount() - 1){
                        if(!isLoading){
                            page_index += PAGE_NUM;
                            loadData(sday_long,eday_long);
                            LogUtil.e("page_index -- " + page_index);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getContext(),"点击看大图",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(),PicDetailActivity.class);
            JpjPicRecord pic = (JpjPicRecord) adapter.getItem(position);
            intent.putExtra("JpjPicRecord",pic);
            startActivity(intent);
        }
    };

    private AdapterView.OnItemLongClickListener itemLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //showPhotographDialog(position,id);
            return true;
        }
    };

    @Override
    public void onClick(View v) {
        Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.id_start_day:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String day = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        startDay.setText(day);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.id_end_day:
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String day = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        endDay.setText(day);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)-1);
                datePickerDialog2.show();
                break;
            case R.id.button:
                try {
                    String sday = startDay.getText().toString().trim();
                    String eday = endDay.getText().toString().trim();
                    sday_long = Day_Format.parse(sday).getTime()/1000 + (25*60*60-1);
                    eday_long = Day_Format.parse(eday).getTime()/1000;

                    if(sday_long < eday_long){
                        Toast.makeText(getContext(),"时间选择不规范",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    page_index = 0;
                    loadData(sday_long,eday_long);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 加载数据
     * @param sday_long
     * @param eday_long
     */
    private void loadData(long sday_long,long eday_long){
        //查询操作
        JpjGroup group = MyApplication.currJpjGroup;
        JpjDevice device = MyApplication.currJpjDevice;
        if(device == null){
            if(group == null){
            }else{
                getJpjPicforNet(group.getGroup_id()+"","group",sday_long,eday_long,page_index,PAGE_NUM);
            }
        }else{
            getJpjPicforNet(device.getDeviceID(),"device",sday_long,eday_long,page_index,PAGE_NUM);
        }
    }

    private void getJpjPicforNet(String id,String type,long sday,long eday,int page_index,int page_num) {
        isLoading = true;
        MyApplication.mContext.sendBroadcast(new Intent(Constants.BroadCastAction.BROADCAST_DIALOG_SHOW));
        MyApplication.getInstance().getApi().queryPicHistoryforJpj(id,type,sday,eday,page_index,page_num,new PicCallBack());
    }

    private class PicCallBack extends PanLvApi.ClientAjaxCallback{
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);
            isLoading = false;
            MyApplication.mContext.sendBroadcast(new Intent(Constants.BroadCastAction.BROADCAST_DIALOG_CANCEL));

            String data = ApiErrorCode.getData(t);
            if(data == null){
                return;
            }

            if(page_index == 0){
                datas.clear();
            }
            List<JpjPicRecord> pics = JSON.parseArray(data,JpjPicRecord.class);
            datas.addAll(pics);

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
            isLoading = false;
            MyApplication.mContext.sendBroadcast(new Intent(Constants.BroadCastAction.BROADCAST_DIALOG_CANCEL));
        }
    }

    /**
     * 获得指定日期的前一天
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = Day_Format.format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = Day_Format.format(c.getTime());
        return dayAfter;
    }
}
