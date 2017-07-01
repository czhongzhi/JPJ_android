package com.runbo.jpj.fragment;


import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.chat.entity.JpjDevice;
import com.chat.entity.JpjGroup;
import com.runbo.jpj.R;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.ui.MinaActivity;
import com.runbo.jpj.util.LogUtil;
import com.runbo.jpj.util.ToastUtil;

import java.util.List;


/**
 * 地图
 */
public class MapFragment extends Fragment {

    private MapView mapView;
    private BaiduMap mBaiduMap;

    private LayoutInflater mInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mInflater = inflater;
        dealMap(mapView);
        return view;
    }

    private void dealMap(MapView mapView) {
        mBaiduMap = mapView.getMap();

        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        JpjGroup group = MyApplication.currJpjGroup;
        JpjDevice device = MyApplication.currJpjDevice;

        if(device == null){
            if(group == null){
            }else{
                List<JpjDevice> devices = group.getJpjDevices();
                for(JpjDevice d : devices){
                    addPoint(d,true);
                }
            }
        }else{
            addPoint(device,true);
        }

//        addPoint(22.513684,114.049199,"监拍机001",true);
//        addPoint(22.513684,114.059199,"监拍机002",false);
//        addPoint(22.513684,114.079199,"监拍机003",false);
//        addPoint(22.513684,114.089199,"监拍机004",false);
//        addPoint(22.523684,114.049199,"监拍机005",false);
    }

    /**
     *添加Maker坐标点
     * @param device
     * @param isCentre
     */
    private void addPoint(JpjDevice device,boolean isCentre){
        //定义Maker坐标点
        LatLng point = new LatLng(device.getDeviceLat(), device.getDeviceLng());
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.jpj_erase);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
        Bundle bundle = new Bundle();
        bundle.putSerializable("device",device);
        marker.setExtraInfo(bundle);

        if(isCentre){
            //设定中心点坐标
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(point)
                    .zoom(15)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }

        //构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0x00000000)
                .fontSize(14)
                .fontColor(getResources().getColor(R.color.divider_font_red))
                .text(device.getUser_name())
                .rotate(0)
                .position(point);
        //在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);

        mBaiduMap.setOnMarkerClickListener(new MarkerClick());
    }

    private class MarkerClick implements BaiduMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            JpjDevice device = (JpjDevice) marker.getExtraInfo().get("device");

            InfoWindow infoWindow = null;

            View view = mInflater.inflate(R.layout.item_marker_win,null);

            ((TextView)view.findViewById(R.id.id_marker_name)).setText("名称："+device.getUser_name());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ToastUtil.show(getContext(),"infowindow click");
//                    Intent intent = new Intent(MyApplication.mContext, MinaActivity.class);
//                    startActivity(intent);
                }
            });
            final LatLng ll = marker.getPosition();
            //将marker所在的经纬度的信息转化成屏幕上的坐标
            Point p = mBaiduMap.getProjection().toScreenLocation(ll);
            p.y -= 50;
            LatLng llInof = mBaiduMap.getProjection().fromScreenLocation(p);
            //初始化infoWindow，最后那个参数表示显示的位置相对于覆盖物的竖直偏移量
            infoWindow = new InfoWindow(view,llInof,-1);
            mBaiduMap.showInfoWindow(infoWindow);
            //让地图以被点击的覆盖物为中心
            //MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
            //mBaiduMap.setMapStatus(status);
            return true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
