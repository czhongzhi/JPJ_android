package com.chat.dbhelper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.chat.dbentity.DBConstant;
import com.chat.entity.JpjDevice;
import com.chat.entity.JpjGroup;
import com.chat.entity.JpjUser;
import com.runbo.jpj.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatpocService {

    private final String TAG = "ChatpocService";
    @SuppressWarnings("unused")
    private Context mContext;

    public ChatpocService(Context context) {
        mContext = context;
    }

    /**
     * 更新或添加监拍机组信息
     */
    public void updataJpjGroup(JpjGroup group) {
        LogUtil.i(TAG + " updataJpjGroup");
        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjGroup.center_url,
                null,
                DBConstant.JpjGroup.group_id + "=?",
                new String[]{group.getGroup_id() + ""}, null);

        ContentValues values = new ContentValues();
        values.put(DBConstant.JpjGroup.group_id,group.getGroup_id());
        values.put(DBConstant.JpjGroup.group_name,group.getGroup_name());
        values.put(DBConstant.JpjGroup.owner_id,group.getOwner_id());
        values.put(DBConstant.JpjGroup.create_date,group.getCreate_date());
        values.put(DBConstant.JpjGroup.cmpid,group.getCmpid());
        values.put(DBConstant.JpjGroup.busiess_id,group.getBusiess_id());

        if (cursor == null || cursor.getCount() <= 0){//插入
            mContext.getContentResolver().insert(DBConstant.JpjGroup.center_url, values);
        }else{//更新
			mContext.getContentResolver().update(
					DBConstant.JpjGroup.center_url,
					values,
					DBConstant.JpjGroup.group_id + "=?",
					new String[] {group.getGroup_id()+""});
        }
        if(cursor != null){
            cursor.close();
        }
    }

    /**
     * 查询组信息
     * @param group_id
     * @return
     */
    public JpjGroup queryJpjGroup(int group_id){
        LogUtil.i(TAG + " queryJpjGroup");

        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjGroup.center_url, null,
                DBConstant.JpjGroup.group_id + "=?",
                new String[] { group_id + "" }, null);
        JpjGroup group = null;

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToNext()){
            group = new JpjGroup();
            group.setGroup_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.group_id)));
            group.setGroup_name(cursor.getString(cursor.getColumnIndex(DBConstant.JpjGroup.group_name)));
            group.setOwner_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.owner_id)));
            group.setCreate_date(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.create_date)));
            group.setCmpid(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.cmpid)));
            group.setBusiess_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.busiess_id)));
            group.setJpjDevices(queryJpjDevices(group.getGroup_id()));
        }
        if(cursor != null){
            cursor.close();
        }
        return group;
    }

    public List<JpjGroup> queryJpjGroups(){
        LogUtil.i(TAG + " queryJpjGroups");
        List<JpjGroup> groups = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjGroup.center_url, null,
                null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                JpjGroup group = new JpjGroup();
                group.setGroup_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.group_id)));
                group.setGroup_name(cursor.getString(cursor.getColumnIndex(DBConstant.JpjGroup.group_name)));
                group.setOwner_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.owner_id)));
                group.setCreate_date(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.create_date)));
                group.setCmpid(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.cmpid)));
                group.setBusiess_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.busiess_id)));
                group.setJpjDevices(queryJpjDevices(group.getGroup_id()));
                groups.add(group);
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return groups;
    }

    public List<String> queryJpjGidList(){
        LogUtil.i(TAG + " queryJpjGidList");
        List<String> list = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjGroup.center_url, null,
                null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                list.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjGroup.group_id))));
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    public void deleteInvalidData(String group_id){
        mContext.getContentResolver().delete(DBConstant.JpjGroup.center_url,
                DBConstant.JpjGroup.group_id+ "=?",
                new String[] { group_id});
    }

    public List<JpjDevice> queryJpjDevices(int group_id){
        LogUtil.i(TAG + " queryJpjDevices");
        List<JpjDevice> devices = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjDevice.center_url, null,
                DBConstant.JpjDevice.group_id + "=?",
                new String[] { group_id + "" }, null);
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                devices.add(getJpjDevice(cursor));
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return devices;
    }

    /**
     * 查询设备信息
     * @param deviceIndex
     * @return
     */
    public JpjDevice queryJpjDevice(int deviceIndex){
        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjDevice.center_url, null,
                DBConstant.JpjDevice.deviceIndex + "=?",
                new String[] { deviceIndex + "" }, null);
        JpjDevice device = null;

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToNext()){
            device = getJpjDevice(cursor);
        }
        if(cursor != null){
            cursor.close();
        }
        return device;
    }

    public JpjDevice getJpjDevice(Cursor cursor){
        JpjDevice device = new JpjDevice();
        device.setGroup_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.group_id)));
        device.setDeviceIndex(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.deviceIndex)));
        device.setDeviceID(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceID)));
        device.setUser_name(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.user_name)));
        device.setAddress(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.address)));
        device.setCmpid(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.cmpid)));
        device.setBusiess_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.busiess_id)));
        device.setDeviceType(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.deviceType)));
        device.setDeviceManufactorName(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceManufactorName)));
        device.setDeviceModel(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceModel)));
        device.setDeviceProductionDate(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceProductionDate)));
        device.setDeviceInstallDate(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceInstallDate)));
        device.setDeviceOrientation(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceOrientation)));
        device.setDeviceTele(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceTele)));
        device.setDeviceMeid(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceMeid)));
        device.setDeviceNetType(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.deviceNetType)));
        device.setDeviceRunState(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.deviceRunState)));
        device.setDeviceShieldState(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.deviceShieldState)));
        device.setDeviceDangerID(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.deviceDangerID)));
        device.setLineID(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.lineID)));
        device.setTowerID(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjDevice.towerID)));
        device.setAccount_type(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.account_type)));
        device.setIsjpjadmin(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.isjpjadmin)));
        //经纬度
        double deviceLng = Double.valueOf(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceLng)));
        double deviceLat = Double.valueOf(cursor.getString(cursor.getColumnIndex(DBConstant.JpjDevice.deviceLat)));
        device.setDeviceLng(deviceLng);
        device.setDeviceLat(deviceLat);
        return device;
    }

    /**
     * 更新或添加监拍机设备信息
     */
    public void updataJpjDevice(JpjDevice device){
        LogUtil.i(TAG + " updataJpjDevice");
        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjDevice.center_url,
                null,
                DBConstant.JpjDevice.deviceIndex + "=?",
                new String[]{device.getDeviceIndex() + ""}, null);

        ContentValues values = new ContentValues();
        values.put(DBConstant.JpjDevice.group_id,device.getGroup_id());
        values.put(DBConstant.JpjDevice.deviceIndex,device.getDeviceIndex());
        values.put(DBConstant.JpjDevice.deviceID,device.getDeviceID());
        values.put(DBConstant.JpjDevice.user_name,device.getUser_name());
        values.put(DBConstant.JpjDevice.address,device.getAddress());
        values.put(DBConstant.JpjDevice.cmpid,device.getCmpid());
        values.put(DBConstant.JpjDevice.busiess_id,device.getBusiess_id());
        values.put(DBConstant.JpjDevice.deviceType,device.getDeviceType());
        values.put(DBConstant.JpjDevice.deviceManufactorName,device.getDeviceManufactorName());
        values.put(DBConstant.JpjDevice.deviceModel,device.getDeviceModel());
        values.put(DBConstant.JpjDevice.deviceProductionDate,device.getDeviceProductionDate());
        values.put(DBConstant.JpjDevice.deviceInstallDate,device.getDeviceInstallDate());
        values.put(DBConstant.JpjDevice.deviceOrientation,device.getDeviceOrientation());
        values.put(DBConstant.JpjDevice.deviceTele,device.getDeviceTele());
        values.put(DBConstant.JpjDevice.deviceMeid,device.getDeviceMeid());
        values.put(DBConstant.JpjDevice.deviceNetType,device.getDeviceNetType());
        values.put(DBConstant.JpjDevice.deviceRunState,device.getDeviceRunState());
        values.put(DBConstant.JpjDevice.deviceShieldState,device.getDeviceShieldState());
        values.put(DBConstant.JpjDevice.deviceDangerID,device.getDeviceDangerID());
        values.put(DBConstant.JpjDevice.deviceLng,device.getDeviceLng());
        values.put(DBConstant.JpjDevice.deviceLat,device.getDeviceLat());
        values.put(DBConstant.JpjDevice.lineID,device.getLineID());
        values.put(DBConstant.JpjDevice.towerID,device.getTowerID());
        values.put(DBConstant.JpjDevice.account_type,device.getAccount_type());
        values.put(DBConstant.JpjDevice.isjpjadmin,device.getIsjpjadmin());

        if (cursor == null || cursor.getCount() <= 0){//插入
            mContext.getContentResolver().insert(DBConstant.JpjDevice.center_url, values);
        }else{//更新
            mContext.getContentResolver().update(
                    DBConstant.JpjDevice.center_url,
                    values,
                    DBConstant.JpjDevice.deviceIndex + "=?",
                    new String[] {device.getDeviceIndex()+""});
        }
        if(cursor != null){
            cursor.close();
        }
    }


    /**
     * 更新用户信息
     */
    public void updataJpjUser(JpjUser user) {
        LogUtil.i(TAG + " updataJpjUser");
        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjUser.center_url,
                null,
                DBConstant.JpjUser.userid + "=?",
                new String[]{user.getUserid() + ""}, null);

        ContentValues values = new ContentValues();
        values.put(DBConstant.JpjUser.userid,user.getUserid());
        values.put(DBConstant.JpjUser.password,user.getPassword());
        values.put(DBConstant.JpjUser.username,user.getUsername());
        values.put(DBConstant.JpjUser.headerUrl,user.getHeaderUrl());
        values.put(DBConstant.JpjUser.phone,user.getPhone());
        values.put(DBConstant.JpjUser.sex,user.getSex());
        values.put(DBConstant.JpjUser.area,user.getArea());
        values.put(DBConstant.JpjUser.busiess_id,user.getBusiess_id());

        if (cursor == null || cursor.getCount() <= 0){//插入
            mContext.getContentResolver().insert(DBConstant.JpjUser.center_url, values);
        }else{//更新
            mContext.getContentResolver().update(
                    DBConstant.JpjUser.center_url,
                    values,
                    DBConstant.JpjUser.userid + "=?",
                    new String[] {user.getUserid()+""});
        }
        if(cursor != null){
            cursor.close();
        }
    }

    /**
     * 查询用户信息
     * @return
     */
    public JpjUser queryJpjUser(String userid){
        LogUtil.i(TAG + " queryJpjUser");

        Cursor cursor = mContext.getContentResolver().query(
                DBConstant.JpjUser.center_url, null,
                DBConstant.JpjUser.userid + "=?",
                new String[] { userid + "" }, null);
        JpjUser user = null;

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToNext()){
            user = new JpjUser();
            user.setUserid(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjUser.userid)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(DBConstant.JpjUser.password)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(DBConstant.JpjUser.username)));
            user.setHeaderUrl(cursor.getString(cursor.getColumnIndex(DBConstant.JpjUser.headerUrl)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(DBConstant.JpjUser.phone)));
            user.setSex(cursor.getString(cursor.getColumnIndex(DBConstant.JpjUser.sex)));
            user.setArea(cursor.getString(cursor.getColumnIndex(DBConstant.JpjUser.area)));
            user.setBusiess_id(cursor.getInt(cursor.getColumnIndex(DBConstant.JpjUser.busiess_id)));
        }
        if(cursor != null){
            cursor.close();
        }
        return user;
    }

//	/*
//	 * 添加消息记录
//	 */
//	public void AddChatMessage(ChatMessage chatmsg, String recvuserid) {
//		int count = 0;
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.ChatMessage.center_url,
//				null,
//				DBConstant.ChatMessage.groupid + "=? and "
//						+ DBConstant.ChatMessage.userid + "=? and "
//						+ DBConstant.ChatMessage.datetime + "=?",
//				new String[] { chatmsg.getGroupid(), chatmsg.getUserid(),
//						chatmsg.getDatetime() }, null);
//		Log.d(TAG, "AddChatMessage mcursor :" + mcursor
//				+ " mcursor.getCount() :" + mcursor.getCount());
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			// 插入对应的数据到数据库中
//
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.ChatMessage.charsetname,
//					chatmsg.getCharsetname());
//			values.put(DBConstant.ChatMessage.datetime, chatmsg.getDatetime());
//			values.put(DBConstant.ChatMessage.groupid, chatmsg.getGroupid());
//			values.put(DBConstant.ChatMessage.msgtype, chatmsg.getMsgtype());
//			values.put(DBConstant.ChatMessage.msgcontent,chatmsg.getMsgcontent());
//			if (recvuserid != null) {
//				values.put(DBConstant.ChatMessage.recvuserid, recvuserid);
//			}
//			values.put(DBConstant.ChatMessage.userid, chatmsg.getUserid());
//			values.put(DBConstant.ChatMessage.display, 0);
//
//			values.put(DBConstant.ChatMessage.filename, chatmsg.getFilename());
//			values.put(DBConstant.ChatMessage.filepath, chatmsg.getFilepath());
//			values.put(DBConstant.ChatMessage.filetype, chatmsg.getFiletype());
//			values.put(DBConstant.ChatMessage.filesize, chatmsg.getFilesize()+ "");
//			values.put(DBConstant.ChatMessage.filestate, chatmsg.getState());
//			values.put(DBConstant.ChatMessage.filepercent, chatmsg.getPercent()+ "");
//			values.put(DBConstant.ChatMessage.fileid, chatmsg.getFileID());
//			values.put(DBConstant.ChatMessage.filemd5, chatmsg.getFileMD5());
//
//			mContext.getContentResolver().insert(
//					DBConstant.ChatMessage.center_url, values);
//
//		} else {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.ChatMessage.charsetname,
//					chatmsg.getCharsetname());
//			values.put(DBConstant.ChatMessage.datetime, chatmsg.getDatetime());
//			values.put(DBConstant.ChatMessage.groupid, chatmsg.getGroupid());
//			values.put(DBConstant.ChatMessage.msgtype, chatmsg.getMsgtype());
//			values.put(DBConstant.ChatMessage.msgcontent,chatmsg.getMsgcontent());
//			values.put(DBConstant.ChatMessage.userid, chatmsg.getUserid());
//			if (recvuserid != null) {
//				values.put(DBConstant.ChatMessage.recvuserid, recvuserid);
//			}
//			values.put(DBConstant.ChatMessage.display, 0);
//
//			values.put(DBConstant.ChatMessage.filename, chatmsg.getFilename());
//			values.put(DBConstant.ChatMessage.filepath, chatmsg.getFilepath());
//			values.put(DBConstant.ChatMessage.filetype, chatmsg.getFiletype());
//			values.put(DBConstant.ChatMessage.filesize, chatmsg.getFilesize()+ "");
//			values.put(DBConstant.ChatMessage.filestate, chatmsg.getState());
//			values.put(DBConstant.ChatMessage.filepercent, chatmsg.getPercent()+ "");
//			values.put(DBConstant.ChatMessage.fileid, chatmsg.getFileID());
//			values.put(DBConstant.ChatMessage.filemd5, chatmsg.getFileMD5());
//
//			mContext.getContentResolver().update(
//					DBConstant.ChatMessage.center_url,
//					values,
//					DBConstant.ChatMessage.groupid + "=? and "
//							+ DBConstant.ChatMessage.userid + "=? and "
//							+ DBConstant.ChatMessage.datetime + "=?",
//					new String[] { chatmsg.getGroupid(), chatmsg.getUserid(),
//							chatmsg.getDatetime() });
//		}
//
//		if (mcursor != null) {
//			mcursor.close();
//
//		}
//	}
//
//	/*
//	 * 添加下载记录
//	 */
//	public void AddDownMessage(DownMessage downMessage) {
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.DownMessage.center_url,
//				null,
//				DBConstant.DownMessage.groupid + "=? and "
//						+ DBConstant.DownMessage.userid + "=? and "
//						+ DBConstant.DownMessage.downurl + "=? and "
//						+ DBConstant.DownMessage.threadid + "=?",
//				new String[] { downMessage.getGroupid(),
//						downMessage.getUserid(), downMessage.getDownurl(),
//						downMessage.getThreadid() + "" }, null);
//
//		Log.d(TAG, "AddDownMessage mcursor :" + mcursor
//				+ " mcursor.getCount() :" + mcursor.getCount());
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			// 不存在，则插入对应的数据到数据库中
//
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.DownMessage.msgtype, downMessage.getMsgtype());
//			values.put(DBConstant.DownMessage.userid, downMessage.getUserid());
//			values.put(DBConstant.DownMessage.groupid, downMessage.getGroupid());
//			values.put(DBConstant.DownMessage.threadid,
//					downMessage.getThreadid());
//			values.put(DBConstant.DownMessage.downlength,
//					downMessage.getDownlength());
//			values.put(DBConstant.DownMessage.state, downMessage.getState());
//			values.put(DBConstant.DownMessage.downurl, downMessage.getDownurl());
//
//			mContext.getContentResolver().insert(
//					DBConstant.ChatMessage.center_url, values);
//
//		} else {
//			// 存在，则更新
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.DownMessage.msgtype, downMessage.getMsgtype());
//			values.put(DBConstant.DownMessage.userid, downMessage.getUserid());
//			values.put(DBConstant.DownMessage.groupid, downMessage.getGroupid());
//			values.put(DBConstant.DownMessage.threadid,
//					downMessage.getThreadid());
//			values.put(DBConstant.DownMessage.downlength,
//					downMessage.getDownlength());
//			values.put(DBConstant.DownMessage.state, downMessage.getState());
//			values.put(DBConstant.DownMessage.downurl, downMessage.getDownurl());
//
//			mContext.getContentResolver().update(
//					DBConstant.DownMessage.center_url,
//					values,
//					DBConstant.DownMessage.groupid + "=? and "
//							+ DBConstant.DownMessage.userid + "=? and "
//							+ DBConstant.DownMessage.downurl + "=? "
//							+ DBConstant.DownMessage.threadid + "=?",
//					new String[] { downMessage.getGroupid(),
//							downMessage.getUserid(), downMessage.getDownurl(),
//							downMessage.getThreadid() + "" });
//		}
//		if (mcursor != null) {
//			mcursor.close();
//		}
//	}
//
//	/*
//	 * 更新本地多线程下载记录
//	 */
//	public int UpdateThreadDownMessage(DownMessage downMessage,
//			Map<Integer, Integer> map) {
//		int count = 0;
//		ContentValues values = new ContentValues();
//
//		// 下载完成,删除下载记录
//		if ((Constants.Filestate.Complete).equals(downMessage.getState())) {
//
//			values.put(DBConstant.DownMessage.state, downMessage.getState());
//			count = mContext.getContentResolver()
//					.delete(DBConstant.DownMessage.center_url,
//							DBConstant.DownMessage.userid + "=?  and "
//									+ DBConstant.DownMessage.groupid
//									+ "=? and "
//									+ DBConstant.DownMessage.downurl + "=?",
//							new String[] { downMessage.getUserid(),
//									downMessage.getGroupid(),
//									downMessage.getDownurl() });
//
//		} else {
//			for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//				values.put(DBConstant.DownMessage.threadid, entry.getKey());
//				values.put(DBConstant.DownMessage.downlength, entry.getValue());
//
//				count = mContext.getContentResolver().update(
//						DBConstant.DownMessage.center_url,
//						values,
//						DBConstant.DownMessage.userid + "=?  and "
//								+ DBConstant.DownMessage.groupid + "=? and "
//								+ DBConstant.DownMessage.downurl + "=?",
//						new String[] { downMessage.getUserid(),
//								downMessage.getGroupid(),
//								downMessage.getDownurl() });
//			}
//		}
//		Log.d(TAG, "UpdateThreadDownMessage downMessage.getState() :"
//				+ downMessage.getState() + " count :" + count);
//
//		return count;
//	}
//
//	/**
//	 * 获取每条线程已经下载的文件长度
//	 */
//	@SuppressLint("UseSparseArrays")
//	public Map<Integer, Integer> getDownData(String downurl,
//			DownMessage downMessage) {
//
//		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.DownMessage.center_url,
//				null,
//				DBConstant.DownMessage.groupid + "=? and "
//						+ DBConstant.DownMessage.userid + "=? and "
//						+ DBConstant.DownMessage.downurl + "=?",
//				new String[] { downMessage.getGroupid(),
//						downMessage.getUserid(), downurl }, null);
//
//		Log.d(TAG, "getDownData mcursor :" + mcursor + " mcursor.getCount() :"
//				+ mcursor.getCount());
//		if (mcursor != null && mcursor.getCount() > 0) {
//			while (mcursor.moveToNext()) {
//				data.put(
//						mcursor.getInt(mcursor
//								.getColumnIndex(DBConstant.DownMessage.threadid)),
//						mcursor.getInt(mcursor
//								.getColumnIndex(DBConstant.DownMessage.downlength)));
//			}
//		}
//		mcursor.close();
//		return data;
//	}
//
//	/*
//	 * 查询消息
//	 */
//	public Cursor QueryChatMessage(ChatMessage chatmsg) {
//		int count = 0;
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.ChatMessage.center_url,
//				null,
//				DBConstant.ChatMessage.groupid + "=? and "
//						+ DBConstant.ChatMessage.userid + "=? and "
//						+ DBConstant.ChatMessage.filemd5 + "=?",
//				new String[] { chatmsg.getGroupid(), chatmsg.getUserid(),
//						chatmsg.getFileMD5() }, null);
//		return mcursor;
//	}
//
//
//	/*
//	 * 获取群聊下线人数
//	 */
//	public int getChatofflinemessageCount(String user_id, String group_id) {
//		int count = 0;
//		Cursor cursor = mContext.getContentResolver().query(
//				DBConstant.ChatMessage.center_url,
//				null,
//				DBConstant.ChatMessage.userid + "=?  and "
//						+ DBConstant.ChatMessage.groupid + "=? and "
//						+ DBConstant.ChatMessage.display + "=0", null, null);
//		if (cursor != null) {
//			count = cursor.getCount();
//			cursor.close();
//		}
//		return count;
//	}
//
//	/*
//	 * 获取单聊下线人数
//	 */
//	public int getAloneofflinemessageCount(String user_id, String group_id) {
//
//		int count = 0;
//		Cursor cursor = mContext.getContentResolver().query(
//				DBConstant.AloneChatMessage.center_url,
//				null,
//				DBConstant.AloneChatMessage.userid + "=?  and "
//						+ DBConstant.AloneChatMessage.groupid + "=? and "
//						+ DBConstant.AloneChatMessage.display + "=0", null,
//				null);
//		if (cursor != null) {
//			count = cursor.getCount();
//			cursor.close();
//		}
//		return count;
//	}
//
//	/*
//	 * 更新上传或下载消息进度状态
//	 */
//	public int updateChatMessage(ChatMessage chatmsg, int percent,
//			String fileMD5) {
//		ContentValues values = new ContentValues();
//		if (percent != -1) {
//			values.put(DBConstant.ChatMessage.filepercent, percent);
//		}
//
//		if (chatmsg.getState() != null && !" ".equals(chatmsg.getState())) {
//			values.put(DBConstant.ChatMessage.filestate, chatmsg.getState());
//		}
//		if (chatmsg.getFilepath() != null && !" ".equals(chatmsg.getFilepath())) {
//			values.put(DBConstant.ChatMessage.filepath, chatmsg.getFilepath());
//		}
//		int count = mContext.getContentResolver().update(
//				DBConstant.ChatMessage.center_url,
//				values,
//				DBConstant.ChatMessage.userid + "=?  and "
//						+ DBConstant.ChatMessage.groupid + "=? and "
//						+ DBConstant.ChatMessage.filemd5 + "=? and "
//						+ DBConstant.ChatMessage.datetime + "=?",
//				new String[] { chatmsg.getUserid(), chatmsg.getGroupid(),
//						fileMD5,chatmsg.getDatetime()});
//		return count;
//	}
//
//	/*
//	 * 把群聊消息改为已读
//	 */
//	public int updateDisplayChatMessage(String user_id, String group_id) {
//		ContentValues values = new ContentValues();
//		values.put(DBConstant.ChatMessage.display, 1);
//		int count = mContext.getContentResolver().update(
//				DBConstant.ChatMessage.center_url,
//				values,
//				DBConstant.ChatMessage.userid + "=?  and "
//						+ DBConstant.ChatMessage.groupid + "=? and "
//						+ DBConstant.ChatMessage.display + "=0", null);
//		return count;
//
//	};
//
//	/*
//	 * 把私聊消息改为已读
//	 */
//	public int updateDisplayaloneChatMessage(String user_id, String group_id) {
//		ContentValues values = new ContentValues();
//		values.put(DBConstant.AloneChatMessage.display, 1);
//		int count = mContext.getContentResolver().update(
//				DBConstant.AloneChatMessage.center_url,
//				values,
//				DBConstant.AloneChatMessage.userid + "=?  and "
//						+ DBConstant.AloneChatMessage.groupid + "=? and "
//						+ DBConstant.AloneChatMessage.display + "=0", null);
//		return count;
//
//	};
//
//	public void AddaloneChatMessages(ChatMessage chatmsg, String recvuserid) {
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.AloneChatMessage.center_url,
//				null,
//				DBConstant.AloneChatMessage.groupid + "=? and "
//						+ DBConstant.AloneChatMessage.userid + "=? and "
//						+ DBConstant.AloneChatMessage.datetime + "=? and "
//						+ DBConstant.AloneChatMessage.groupid + "=?",
//				new String[] { chatmsg.getGroupid(), chatmsg.getUserid(),
//						chatmsg.getDatetime(), chatmsg.getGroupid() }, null);
//
//		Log.d(TAG, "AddaloneChatMessages mcursor :" + mcursor
//				+ " mcursor.getCount() :" + mcursor.getCount());
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			// 插入对应的数据到数据库中
//
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.AloneChatMessage.charsetname,
//					chatmsg.getCharsetname());
//			values.put(DBConstant.AloneChatMessage.datetime,
//					chatmsg.getDatetime());
//			values.put(DBConstant.AloneChatMessage.groupid,
//					chatmsg.getGroupid());
//			values.put(DBConstant.AloneChatMessage.msgtype,
//					chatmsg.getMsgtype());
//			values.put(DBConstant.AloneChatMessage.msgcontent,
//					chatmsg.getMsgcontent());
//			values.put(DBConstant.AloneChatMessage.userid, chatmsg.getUserid());
//			if (recvuserid != null) {
//				values.put(DBConstant.AloneChatMessage.recvuserid, recvuserid);
//			}
//			values.put(DBConstant.AloneChatMessage.display, 0);
//			mContext.getContentResolver().insert(
//					DBConstant.AloneChatMessage.center_url, values);
//		} else {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.AloneChatMessage.charsetname,
//					chatmsg.getCharsetname());
//			values.put(DBConstant.AloneChatMessage.datetime,
//					chatmsg.getDatetime());
//			values.put(DBConstant.AloneChatMessage.groupid,
//					chatmsg.getGroupid());
//			values.put(DBConstant.AloneChatMessage.msgtype,
//					chatmsg.getMsgtype());
//			values.put(DBConstant.AloneChatMessage.msgcontent,
//					chatmsg.getMsgcontent());
//			values.put(DBConstant.AloneChatMessage.display, 0);
//			values.put(DBConstant.AloneChatMessage.userid, chatmsg.getUserid());
//			if (recvuserid != null) {
//				values.put(DBConstant.AloneChatMessage.recvuserid, recvuserid);
//			}
//			mContext.getContentResolver().update(
//					DBConstant.AloneChatMessage.center_url,
//					values,
//					DBConstant.AloneChatMessage.groupid + "=? and "
//							+ DBConstant.AloneChatMessage.userid + "=? and "
//							+ DBConstant.AloneChatMessage.datetime + "=?",
//					new String[] { chatmsg.getGroupid(), chatmsg.getUserid(),
//							chatmsg.getDatetime() });
//		}
//
//		if (mcursor != null) {
//			mcursor.close();
//
//		}
//
//	}
//
//	public void addGroups(Groups groups) {
//		SharedPreferences refrence = mContext.getSharedPreferences("test",
//				Activity.MODE_PRIVATE);
//		String user_id = refrence.getString("user_id", "");
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.Groups.center_url,
//				null,
//				DBConstant.Groups.group_id + "=?" + " and "
//						+ DBConstant.Groups.user_id + "=?",
//				new String[] { groups.getGroup_id() + "", user_id + "" }, null);
//
//		Log.d(TAG, "addGroups mcursor :" + mcursor + " mcursor.getCount() :"
//				+ mcursor.getCount());
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.Groups.create_date, groups.getCreate_date());
//			values.put(DBConstant.Groups.group_id, groups.getGroup_id());
//			values.put(DBConstant.Groups.group_name, groups.getGroup_name());
//			values.put(DBConstant.Groups.owner_id, groups.getOwner_id());
//			values.put(DBConstant.Groups.user_id, user_id);
//			mContext.getContentResolver().insert(DBConstant.Groups.center_url,
//					values);
//		} else {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.Groups.create_date, groups.getCreate_date());
//			values.put(DBConstant.Groups.group_id, groups.getGroup_id());
//			values.put(DBConstant.Groups.group_name, groups.getGroup_name());
//			values.put(DBConstant.Groups.owner_id, groups.getOwner_id());
//			values.put(DBConstant.Groups.user_id, user_id);
//			mContext.getContentResolver().update(
//					DBConstant.Groups.center_url,
//					values,
//					DBConstant.Groups.group_id + "=?" + " and "
//							+ DBConstant.Groups.user_id + "=?",
//					new String[] { groups.getGroup_id() + "", user_id + "" });
//		}
//		if (mcursor != null) {
//			mcursor.close();
//
//		}
//
//	}
//
//	public void addGroupAndChe(GroupChannel channel) {
////		SharedPreferences refrence = mContext.getSharedPreferences("test",
////				Activity.MODE_PRIVATE);
////		String user_id = refrence.getString("user_id", "");
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.GroupsAndChe.center_url,
//				null,
//				DBConstant.GroupsAndChe.group_id + "=?",
//				new String[] { channel.getGroup_id()+"" }, null);
//
//		Log.d(TAG, "addGroups mcursor :" + mcursor + " mcursor.getCount() :"
//				+ mcursor.getCount());
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.GroupsAndChe.group_id, channel.getGroup_id());
//			values.put(DBConstant.GroupsAndChe.channel_num, channel.getChannel_number());
//			mContext.getContentResolver().insert(DBConstant.GroupsAndChe.center_url,
//					values);
//		} else {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.GroupsAndChe.group_id, channel.getGroup_id());
//			values.put(DBConstant.GroupsAndChe.channel_num, channel.getChannel_number());
//			mContext.getContentResolver().update(
//					DBConstant.GroupsAndChe.center_url,
//					values,
//					DBConstant.GroupsAndChe.group_id + "=?" + " and "
//							+ DBConstant.GroupsAndChe.channel_num + "=?",
//					new String[] { channel.getGroup_id() + "", channel.getChannel_number() + "" });
//		}
//		if (mcursor != null) {
//			mcursor.close();
//
//		}
//
//	}
//
//	public GroupChannel ChanneltoGroup(int number) {
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.GroupsAndChe.center_url,
//				null,
//				DBConstant.GroupsAndChe.channel_num + "=?",
//				new String[] { number + "" }, null);
//
//
//		GroupChannel groupChannel = null;
//		if (mcursor != null && mcursor.getCount() > 0 && mcursor.moveToNext()) {
//			 groupChannel = new GroupChannel();
//			groupChannel.setChannel_number(Integer.valueOf(mcursor.getString(mcursor
//					.getColumnIndex(DBConstant.GroupsAndChe.channel_num))));
//			groupChannel.setGroup_id(Integer.valueOf(mcursor.getString(mcursor.getColumnIndex(DBConstant.GroupsAndChe.group_id))));
//		}
//		if (mcursor != null) {
//			mcursor.close();
//		}
//		return groupChannel;
//	}
//
//	public CustomerVo getCustomervo(int user_id, int group_id) {
//
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.CustomerVo.center_url,
//				null,
//				DBConstant.CustomerVo.user_id + "=? and "
//						+ DBConstant.CustomerVo.group_id + "=?",
//				new String[] { user_id + "", group_id + "" }, null);
//
//		CustomerVo customervo = new CustomerVo();
//
//		Log.d(TAG, "getCustomervo mcursor :" + mcursor
//				+ " mcursor.getCount() :" + mcursor.getCount());
//		if (mcursor != null && mcursor.getCount() > 0 && mcursor.moveToNext()) {
//			customervo.setGender(mcursor.getInt(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.gender)) + "");
//			customervo.setGrade(mcursor.getInt(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.grade)));
//			customervo.setGroup_id(group_id);
//			customervo.setIs_Chat(mcursor.getInt(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.is_chat)) + "");
//			customervo.setLogon(mcursor.getInt(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.is_chat)));
//			customervo.setPassword(mcursor.getString(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.password)));
//			customervo.setPhone(mcursor.getString(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.phone)));
//			customervo.setRegister_Date(mcursor.getString(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.register_date)));
//			customervo.setUser_id(mcursor.getInt(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.user_id)));
//			customervo.setUser_name(mcursor.getString(mcursor
//					.getColumnIndex(DBConstant.CustomerVo.user_name)));
//
//		}
//		if (mcursor != null) {
//			mcursor.close();
//		}
//		return customervo;
//	}
//
//	public void addcustomervo(CustomerVo customervo, String group_id) {
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.CustomerVo.center_url,
//				null,
//				DBConstant.CustomerVo.user_id + "=? and "
//						+ DBConstant.CustomerVo.group_id + "=?",
//				new String[] { customervo.getUser_id() + "", group_id }, null);
//
//		SharedPreferences refrence = mContext.getSharedPreferences("test",
//				Activity.MODE_PRIVATE);
//		String user_id = refrence.getString("user_id", "");
//
//		Log.d(TAG, "addcustomervo mcursor :" + mcursor
//				+ " mcursor.getCount() :" + mcursor.getCount());
//
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			Logger.i("sort", "-------------insert-user_name 1:");
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.CustomerVo.gender, customervo.getGender());
//			values.put(DBConstant.CustomerVo.grade, customervo.getGrade());
//			values.put(DBConstant.CustomerVo.is_chat, customervo.getIs_Chat());
//			values.put(DBConstant.CustomerVo.logon, customervo.getLogon());
//			values.put(DBConstant.CustomerVo.password, customervo.getPassword());
//			values.put(DBConstant.CustomerVo.phone, customervo.getPhone());
//			values.put(DBConstant.CustomerVo.group_id, group_id);
//			values.put(DBConstant.CustomerVo.register_date,
//					customervo.getRegister_Date());
//			values.put(DBConstant.CustomerVo.user_id, customervo.getUser_id());
//
//			if (customervo.getUser_id() == Integer.parseInt(user_id) == true) {
//				values.put(DBConstant.CustomerVo.isaccount, 1);
//			} else {
//				values.put(DBConstant.CustomerVo.isaccount, 0);
//			}
//
//			values.put(DBConstant.CustomerVo.user_name,
//					customervo.getUser_name());
//
//			mContext.getContentResolver().insert(
//					DBConstant.CustomerVo.center_url, values);
//
//		} else {
//			Logger.i("sort", "-------------insert-user_name 2:");
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.CustomerVo.gender, customervo.getGender());
//			values.put(DBConstant.CustomerVo.grade, customervo.getGrade());
//			values.put(DBConstant.CustomerVo.is_chat, customervo.getIs_Chat());
//			values.put(DBConstant.CustomerVo.logon, customervo.getLogon());
//			values.put(DBConstant.CustomerVo.password, customervo.getPassword());
//			values.put(DBConstant.CustomerVo.phone, customervo.getPhone());
//			values.put(DBConstant.CustomerVo.register_date,
//					customervo.getRegister_Date());
//			values.put(DBConstant.CustomerVo.user_id, customervo.getUser_id());
//			values.put(DBConstant.CustomerVo.user_name,
//					customervo.getUser_name());
//			values.put(DBConstant.CustomerVo.group_id, group_id);
//
//			if (customervo.getUser_id() == Integer.parseInt(user_id) == true) {
//				values.put(DBConstant.CustomerVo.isaccount, 1);
//			} else {
//				values.put(DBConstant.CustomerVo.isaccount, 0);
//			}
//
//			mContext.getContentResolver()
//					.update(DBConstant.CustomerVo.center_url,
//							values,
//							DBConstant.CustomerVo.group_id + "=? and "
//									+ DBConstant.CustomerVo.user_id + "=?",
//							new String[] { group_id + "",
//									customervo.getUser_id() + "" });
//
//		}
//
//	}
//
//	// 添加用户信息
//	public void addUserinfo(CustomerVo customervo, String user_id) {
//
//		Log.i("wxj", "addUserinfo customervo = "+ customervo);
//		Log.i("wxj", "addUserinfo user_id = "+ user_id);
//
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.UserInfo.center_url,
//				null,
//				DBConstant.UserInfo.user_id + "=?",
//				new String[] { user_id},
//				null);
//
//		Log.i("wxj", "111addUserinfo mcursor="+mcursor);
//		Log.i("wxj", "111addUserinfo mcursor.getCount()="+mcursor.getCount());
//
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			Logger.i("sort", "-------------insert-user_name 1:");
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.UserInfo.user_id, user_id);
//			values.put(DBConstant.UserInfo.user_name, customervo.getUser_name());
//			values.put(DBConstant.UserInfo.account, customervo.getPhone());
//			values.put(DBConstant.UserInfo.gender, customervo.getGender());
//			values.put(DBConstant.UserInfo.region, customervo.getRegion());
//			values.put(DBConstant.UserInfo.file_MD5, customervo.getFile_MD5());
//			values.put(DBConstant.UserInfo.file_name, customervo.getFile_name());
//
//			mContext.getContentResolver().insert(
//					DBConstant.UserInfo.center_url, values);
//		} else {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.UserInfo.user_id, user_id);
//			values.put(DBConstant.UserInfo.user_name, customervo.getUser_name());
//			values.put(DBConstant.UserInfo.account, customervo.getPhone());
//			values.put(DBConstant.UserInfo.gender, customervo.getGender());
//			values.put(DBConstant.UserInfo.region, customervo.getRegion());
//			values.put(DBConstant.UserInfo.file_MD5, customervo.getFile_MD5());
//			values.put(DBConstant.UserInfo.file_name, customervo.getFile_name());
//
//			mContext.getContentResolver()
//					.update(DBConstant.UserInfo.center_url,
//							values,
//							DBConstant.UserInfo.user_id + "=?",
//							new String[] { user_id });
//
//		}
//		Log.i("wxj", "222addUserinfo mcursor="+mcursor);
//		if (mcursor != null) {
//			mcursor.close();
//
//		}
//
//	}
//
//	public void addGroups_temp(Groups groups, String user_id) {
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.groups_temp.center_url, null,
//				DBConstant.groups_temp.group_id + "=?",
//				new String[] { groups.getGroup_id() + "" }, null);
//
//		Log.d(TAG, "addGroups_temp mcursor :" + mcursor
//				+ " mcursor.getCount() :" + mcursor.getCount());
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.groups_temp.create_date,
//					groups.getCreate_date());
//			values.put(DBConstant.groups_temp.group_id, groups.getGroup_id());
//			values.put(DBConstant.groups_temp.group_name,
//					groups.getGroup_name());
//			values.put(DBConstant.groups_temp.owner_id, groups.getOwner_id());
//			values.put(DBConstant.groups_temp.user_id, user_id);
//			mContext.getContentResolver().insert(
//					DBConstant.groups_temp.center_url, values);
//		} else {
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.groups_temp.create_date,
//					groups.getCreate_date());
//			values.put(DBConstant.groups_temp.group_id, groups.getGroup_id());
//			values.put(DBConstant.groups_temp.group_name,
//					groups.getGroup_name());
//			values.put(DBConstant.groups_temp.owner_id, groups.getOwner_id());
//			values.put(DBConstant.groups_temp.user_id, user_id);
//			mContext.getContentResolver().update(
//					DBConstant.groups_temp.center_url, values,
//					DBConstant.Groups.group_id + "=?",
//					new String[] { groups.getGroup_id() + "" });
//		}
//		if (mcursor != null) {
//			mcursor.close();
//
//		}
//
//	}
//
//	public Group getGroup(int group_id) {
//
//		Cursor cursor = mContext.getContentResolver().query(
//				DBConstant.Groups.center_url, null,
//				DBConstant.Groups.group_id + "=?",
//				new String[] { group_id + "" }, null);
//		Group group = null;
//
//		Log.d(TAG, "getGroup mcursor :" + cursor + " mcursor.getCount() :"
//				+ cursor.getCount());
//		if (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
//			group = new Group();
//			int id = cursor
//					.getInt(cursor.getColumnIndex(DBConstant.Groups._id));
//			group.setId(id);
//			group.setName(cursor.getString(cursor
//					.getColumnIndex(DBConstant.Groups.group_name)));
//		}
//		if (cursor != null) {
//			cursor.close();
//		}
//		if (group == null) {
//			cursor = mContext.getContentResolver().query(
//					DBConstant.groups_temp.center_url, null,
//					DBConstant.Groups.group_id + "=?",
//					new String[] { group_id + "" }, null);
//			if (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
//				group = new Group();
//				int id = cursor.getInt(cursor
//						.getColumnIndex(DBConstant.groups_temp._id));
//				group.setId(id);
//				group.setName(cursor.getString(cursor
//						.getColumnIndex(DBConstant.Groups.group_name)));
//			}
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//		return group;
//	}
//
//	/*
//	 * 添加中继台权限记录
//	 */
//	public void AddRepeaterMessage(RepeatDataPacket repeatDataPacket) {
//		Log.d(TAG, "AddRepeaterMessage()");
//		int count = 0;
//		Cursor mcursor = mContext.getContentResolver().query(
//				DBConstant.RepeaterInfo.center_url,
//				null,
//				DBConstant.RepeaterInfo.groupid + "=? and "
//						+ DBConstant.RepeaterInfo.user_id + "=? and "
//						+ DBConstant.RepeaterInfo.rx + "=? and "
//						+ DBConstant.RepeaterInfo.tx + "=?",
//				new String[] { repeatDataPacket.getGroupId()+"", repeatDataPacket.getUserId()+"",
//						repeatDataPacket.getRx()+"",repeatDataPacket.getTx()+"" }, null);
//		Log.d(TAG, "AddRepeaterMessage mcursor :" + mcursor+" mcursor.getCount() :"+mcursor.getCount());
//		Log.d(TAG, "repeatDataPacket :"+repeatDataPacket );
//		if (mcursor != null && mcursor.getCount() <= 0) {
//			// 插入对应的数据到数据库中
//			Log.d(TAG, "insert data" );
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.RepeaterInfo.groupid,repeatDataPacket.getGroupId()+"");
//			values.put(DBConstant.RepeaterInfo.user_id, repeatDataPacket.getUserId());
//			values.put(DBConstant.RepeaterInfo.rx, repeatDataPacket.getRx());
//			values.put(DBConstant.RepeaterInfo.tx, repeatDataPacket.getTx());
//			values.put(DBConstant.RepeaterInfo.modestate,repeatDataPacket.getModeState());
//			values.put(DBConstant.RepeaterInfo.modestateptt,repeatDataPacket.getModeStatePtt());
//			values.put(DBConstant.RepeaterInfo.modestatepoc,repeatDataPacket.getModeStatePoc());
//
//			mContext.getContentResolver().insert(
//					DBConstant.RepeaterInfo.center_url, values);
//
//		} else {
//			Log.d(TAG, "updata data" );
//			ContentValues values = new ContentValues();
//			values.put(DBConstant.RepeaterInfo.groupid,repeatDataPacket.getGroupId()+"");
//			values.put(DBConstant.RepeaterInfo.user_id, repeatDataPacket.getUserId());
//			values.put(DBConstant.RepeaterInfo.rx, repeatDataPacket.getRx());
//			values.put(DBConstant.RepeaterInfo.tx, repeatDataPacket.getTx());
//			values.put(DBConstant.RepeaterInfo.modestate,repeatDataPacket.getModeState());
//			values.put(DBConstant.RepeaterInfo.modestateptt,repeatDataPacket.getModeStatePtt());
//			values.put(DBConstant.RepeaterInfo.modestatepoc,repeatDataPacket.getModeStatePoc());
//
//			mContext.getContentResolver().update(
//					DBConstant.RepeaterInfo.center_url,
//					values,
//					DBConstant.RepeaterInfo.groupid + "=? and "
//							+ DBConstant.RepeaterInfo.user_id + "=? and "
//							+ DBConstant.RepeaterInfo.rx + "=? and "
//							+ DBConstant.RepeaterInfo.tx + "=?",
//					new String[] { repeatDataPacket.getGroupId()+"", repeatDataPacket.getUserId()+"",
//							repeatDataPacket.getRx()+"",repeatDataPacket.getTx()+"" });
//		}
//
//		if (mcursor != null) {
//			mcursor.close();
//
//		}
//	}
//
//	/*
//	 * 查询中继台权限记录
//	 */
//	public RepeatDataPacket getRepeatData(RepeatDataPacket repeatDataPacket) {
//		Log.d(TAG, "getRepeatData()");
//		Cursor cursor = mContext.getContentResolver().query(
//				DBConstant.RepeaterInfo.center_url, null,
//				DBConstant.RepeaterInfo.groupid + "=? and "
//						+ DBConstant.RepeaterInfo.user_id + "=? and "
//						+ DBConstant.RepeaterInfo.rx + "=? and "
//						+ DBConstant.RepeaterInfo.tx + "=?",
//						new String[] { repeatDataPacket.getGroupId()+"", repeatDataPacket.getUserId()+"",
//						repeatDataPacket.getRx()+"",repeatDataPacket.getTx()+"" }, null);
//
//		Log.d(TAG, "getRepeatData mcursor :" + cursor+" cursor.getCount() :"+cursor.getCount());
//		Log.d(TAG, "repeatDataPacket :"+repeatDataPacket );
//		if (cursor != null && cursor.getCount() > 0 && cursor.moveToNext()) {
//			Log.d(TAG, "RepeatData data is saved !!");
//			repeatDataPacket.setModeState(cursor.getString(cursor.getColumnIndex(DBConstant.RepeaterInfo.modestate)));
//			repeatDataPacket.setModeStatePtt(cursor.getString(cursor.getColumnIndex(DBConstant.RepeaterInfo.modestateptt)));
//			repeatDataPacket.setModeStatePoc(cursor.getString(cursor.getColumnIndex(DBConstant.RepeaterInfo.modestatepoc)));
//		}
//		if (cursor != null) {
//			cursor.close();
//		}
//		return repeatDataPacket;
//	}
}
