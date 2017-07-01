package com.chat.dbhelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.chat.dbentity.DBConstant;

public class ChatProvider extends ContentProvider {

    public static final UriMatcher uriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
//    public static final int chatMessages = 1;
//    public static final int chatMessage = 2;
//    public static final int soundMessages = 3;
//    public static final int soundMessage = 4;
//    public static final int callMessages = 5;
//    public static final int callMessage = 6;
//    public static final int alonechatMessages = 7;
//    public static final int alonechatMessage = 8;
//    public static final int groupss = 9;
//    public static final int groups = 10;
//    public static final int customervos = 11;
//    public static final int customervo = 12;
//    public static final int groupss_temp = 13;
//    public static final int groups_temp = 14;
//    public static final int gpslocations = 15;
//    public static final int gpslocation = 16;
//    public static final int downMessages = 17;
//    public static final int downMessage = 18;
//    public static final int userinfos = 19;
//    public static final int userinfo = 20;
//    public static final int repeaterinfos = 21;
//    public static final int repeaterinfo = 22;
//    public static final int groupchannel = 23;
//    public static final int groupchannels = 24;
    public static final int jpjgroups = 25;
    public static final int jpjgroup = 26;
    public static final int jpjdevices = 27;
    public static final int jpjdevice = 28;
    public static final int jpjusers = 29;
    public static final int jpjuser = 30;

    static {
//        uriMatcher.addURI(DBConstant.AUTOR, "chatmessage", chatMessages);
//        uriMatcher.addURI(DBConstant.AUTOR, "chatmessage/#", chatMessage);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "downMessage", downMessages);
//        uriMatcher.addURI(DBConstant.AUTOR, "downMessage/#", downMessage);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "soundmessage", soundMessages);
//        uriMatcher.addURI(DBConstant.AUTOR, "soundmessage/#", soundMessage);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "callMessage", callMessages);
//        uriMatcher.addURI(DBConstant.AUTOR, "callMessage/#", callMessage);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "aloneMessage", alonechatMessages);
//        uriMatcher.addURI(DBConstant.AUTOR, "aloneMessage/#", alonechatMessage);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "groups", groupss);
//        uriMatcher.addURI(DBConstant.AUTOR, "groups/#", groups);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "groupchannel", groupchannels);
//        uriMatcher.addURI(DBConstant.AUTOR, "groupchannel/#", groupchannel);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "customervo", customervos);
//        uriMatcher.addURI(DBConstant.AUTOR, "customervo/#", customervo);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "groupss_temp", groupss_temp);
//        uriMatcher.addURI(DBConstant.AUTOR, "groupss_temp/#", groups_temp);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "gpslocation", gpslocations);
//        uriMatcher.addURI(DBConstant.AUTOR, "gpslocation/#", gpslocation);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "userinfo", userinfos);
//        uriMatcher.addURI(DBConstant.AUTOR, "userinfo/#", userinfo);
//
//        uriMatcher.addURI(DBConstant.AUTOR, "repeaterinfo", repeaterinfos);
//        uriMatcher.addURI(DBConstant.AUTOR, "repeaterinfo/#", repeaterinfo);

        uriMatcher.addURI(DBConstant.AUTOR, "jpjgroup", jpjgroups);
        uriMatcher.addURI(DBConstant.AUTOR, "jpjgroup/#", jpjgroup);

        uriMatcher.addURI(DBConstant.AUTOR, "jpjdevice", jpjdevices);
        uriMatcher.addURI(DBConstant.AUTOR, "jpjdevice/#", jpjdevice);

        uriMatcher.addURI(DBConstant.AUTOR, "jpjuser", jpjusers);
        uriMatcher.addURI(DBConstant.AUTOR, "jpjuser/#", jpjuser);

    }

    private DBOpenHelper dbhelper = null;

    @Override
    public boolean onCreate() {
        dbhelper = new DBOpenHelper(this.getContext(), null, null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
//            case chatMessages:
//                cursor = db.query(DBConstant.Tab_chatMessage, projection, selection,
//                        selectionArgs, null, null, sortOrder, null);
//                break;
//            case downMessages:
//                cursor = db.query(DBConstant.Tab_downMessage, projection, selection,
//                        selectionArgs, null, null, sortOrder, null);
//                break;
//            case soundMessages:
//                cursor = db.query(DBConstant.Tab_soundMessage, projection,
//                        selection, selectionArgs, null, null, sortOrder, null);
//                break;
//            case callMessages:
//                cursor = db.query(DBConstant.Tab_callMessage, projection,
//                        selection, selectionArgs, null, null, sortOrder, null);
//                break;
//            case alonechatMessages:
//                cursor = db.query(DBConstant.Tab_AloneChatMessage, projection,
//                        selection, selectionArgs, null, null, sortOrder, null);
//                break;
//            case groupss:
//                cursor = db.query(DBConstant.Tab_Groups, projection, selection,
//                        selectionArgs, null, null, sortOrder, null);
//                break;
//            case groupchannels:
//                cursor = db.query(DBConstant.Tab_GroupsChe, projection, selection,
//                        selectionArgs, null, null, sortOrder, null);
//                break;
//            case customervos:
//                cursor = db.query(DBConstant.Tab_CustomerVo, projection, selection,
//                        selectionArgs, null, null, DBConstant.CustomerVo.logon
//                                + " desc," + DBConstant.CustomerVo.phone + " asc," + DBConstant.CustomerVo.isaccount, null);
//                break;
//            case groupss_temp:
//                cursor = db.query(DBConstant.Tab_Groups_temp, projection,
//                        selection, selectionArgs, null, null, sortOrder, null);
//                break;
//            case gpslocations:
//                cursor = db.query(DBConstant.gpslocation, projection, selection,
//                        selectionArgs, null, null, sortOrder, null);
//                break;
//            case userinfos:
//                cursor = db.query(DBConstant.Tab_UserInfo, projection, selection,
//                        selectionArgs, null, null, sortOrder, null);
//                break;
//            case repeaterinfos:
//                cursor = db.query(DBConstant.Tab_RepeaterInfo, projection, selection,
//                        selectionArgs, null, null, sortOrder, null);
//                break;
            case jpjgroups:
                cursor = db.query(DBConstant.Tab_jpjGroup, projection, selection,
                        selectionArgs, null, null, sortOrder, null);
                break;
            case jpjdevices:
                cursor = db.query(DBConstant.Tab_jpjDevice, projection, selection,
                        selectionArgs, null, null, sortOrder, null);
                break;
            case jpjusers:
                cursor = db.query(DBConstant.Tab_jpjUser, projection, selection,
                        selectionArgs, null, null, sortOrder, null);
                break;

        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
//            case chatMessages:
//                return DBConstant.ChatMessage.uri;
//            case chatMessage:
//                return DBConstant.ChatMessage.uri + "/#";
//            case downMessages:
//                return DBConstant.DownMessage.uri;
//            case downMessage:
//                return DBConstant.DownMessage.uri + "/#";
//            case soundMessages:
//                return DBConstant.SoundMessage.uri;
//            case soundMessage:
//                return DBConstant.SoundMessage.uri + "/#";
//            case callMessages:
//                return DBConstant.CallMessage.uri;
//            case callMessage:
//                return DBConstant.CallMessage.uri + "/#";
//            case alonechatMessages:
//                return DBConstant.AloneChatMessage.uri;
//            case alonechatMessage:
//                return DBConstant.AloneChatMessage.uri + "/#";
//            case groupss:
//                return DBConstant.Groups.uri;
//            case groups:
//                return DBConstant.Groups.uri + "/#";
//            case groupchannels:
//                return DBConstant.GroupsAndChe.uri;
//            case groupchannel:
//                return DBConstant.GroupsAndChe.uri + "/#";
//            case customervos:
//                return DBConstant.CustomerVo.uri;
//            case customervo:
//                return DBConstant.CustomerVo.uri + "/#";
//            case groupss_temp:
//                return DBConstant.groups_temp.uri;
//            case groups_temp:
//                return DBConstant.groups_temp.uri + "/#";
//            case gpslocation:
//                return DBConstant.GPSlocation.uri + "/#";
//            case gpslocations:
//                return DBConstant.GPSlocation.uri;
//            case userinfo:
//                return DBConstant.UserInfo.uri + "/#";
//            case userinfos:
//                return DBConstant.UserInfo.uri;
//            case repeaterinfo:
//                return DBConstant.RepeaterInfo.uri + "/#";
//            case repeaterinfos:
//                return DBConstant.RepeaterInfo.uri;
            case jpjgroup:
                return DBConstant.JpjGroup.uri + "/#";
            case jpjgroups:
                return DBConstant.JpjGroup.uri;
            case jpjdevice:
                return DBConstant.JpjDevice.uri + "/#";
            case jpjdevices:
                return DBConstant.JpjDevice.uri;
            case jpjuser:
                return DBConstant.JpjUser.uri + "/#";
            case jpjusers:
                return DBConstant.JpjUser.uri;


            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int url = uriMatcher.match(uri);

        switch (url) {
//            case chatMessages:
//                db.insert(DBConstant.Tab_chatMessage, null, values);
//                break;
//            case downMessages:
//                db.insert(DBConstant.Tab_downMessage, null, values);
//                break;
//            case soundMessages:
//                db.insert(DBConstant.Tab_soundMessage, null, values);
//                break;
//            case callMessages:
//                db.insert(DBConstant.Tab_callMessage, null, values);
//                break;
//            case alonechatMessages:
//                db.insert(DBConstant.Tab_AloneChatMessage, null, values);
//                break;
//            case groupss:
//                db.insert(DBConstant.Tab_Groups, null, values);
//                break;
//            case groupchannels:
//                db.insert(DBConstant.Tab_GroupsChe, null, values);
//                break;
//            case customervos:
//                db.insert(DBConstant.Tab_CustomerVo, null, values);
//                break;
//            case groupss_temp:
//                db.insert(DBConstant.Tab_Groups_temp, null, values);
//                break;
//            case gpslocations:
//                db.insert(DBConstant.gpslocation, null, values);
//                break;
//            case userinfos:
//                db.insert(DBConstant.Tab_UserInfo, null, values);
//                break;
//            case repeaterinfos:
//                db.insert(DBConstant.Tab_RepeaterInfo, null, values);
//                break;
            case jpjgroups:
                db.insert(DBConstant.Tab_jpjGroup, null, values);
                break;
            case jpjdevices:
                db.insert(DBConstant.Tab_jpjDevice, null, values);
                break;
            case jpjusers:
                db.insert(DBConstant.Tab_jpjUser, null, values);
                break;
        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int url = uriMatcher.match(uri);
        int count = 0;
        switch (url) {
//            case chatMessages:
//                count = db.delete(DBConstant.Tab_chatMessage, selection,
//                        selectionArgs);
//                break;
//            case downMessages:
//                count = db.delete(DBConstant.Tab_downMessage, selection,
//                        selectionArgs);
//                break;
//            case soundMessages:
//                count = db.delete(DBConstant.Tab_soundMessage, selection,
//                        selectionArgs);
//                break;
//            case callMessages:
//                count = db.delete(DBConstant.Tab_callMessage, selection,
//                        selectionArgs);
//            case alonechatMessages:
//                count = db.delete(DBConstant.Tab_AloneChatMessage, selection,
//                        selectionArgs);
//                break;
//            case groupss:
//                count = db.delete(DBConstant.Tab_Groups, selection, selectionArgs);
//                break;
//            case groupchannels:
//                count = db.delete(DBConstant.Tab_GroupsChe, selection, selectionArgs);
//                break;
//            case customervos:
//                count = db.delete(DBConstant.Tab_CustomerVo, selection,
//                        selectionArgs);
//                break;
//            case groupss_temp:
//                count = db.delete(DBConstant.Tab_Groups_temp, selection,
//                        selectionArgs);
//                break;
//            case gpslocations:
//                count = db.delete(DBConstant.gpslocation, selection, selectionArgs);
//                break;
//            case userinfos:
//                count = db.delete(DBConstant.Tab_UserInfo, selection, selectionArgs);
//                break;
//            case repeaterinfos:
//                count = db.delete(DBConstant.Tab_RepeaterInfo, selection, selectionArgs);
//                break;
            case jpjgroups:
                count = db.delete(DBConstant.Tab_jpjGroup, selection, selectionArgs);
                break;
            case jpjdevices:
                count = db.delete(DBConstant.Tab_jpjDevice, selection, selectionArgs);
                break;
            case jpjusers:
                count = db.delete(DBConstant.Tab_jpjUser, selection, selectionArgs);
                break;

        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int url = uriMatcher.match(uri);
        int count = 0;
        switch (url) {
//            case chatMessages:
//                count = db.update(DBConstant.Tab_chatMessage, values, selection,
//                        selectionArgs);
//                break;
//            case downMessages:
//                count = db.update(DBConstant.Tab_downMessage, values, selection,
//                        selectionArgs);
//                break;
//            case soundMessages:
//                count = db.update(DBConstant.Tab_soundMessage, values, selection,
//                        selectionArgs);
//                break;
//            case callMessages:
//                count = db.update(DBConstant.Tab_callMessage, values, selection,
//                        selectionArgs);
//                break;
//            case alonechatMessages:
//                count = db.update(DBConstant.Tab_AloneChatMessage, values,
//                        selection, selectionArgs);
//                break;
//            case groupss:
//                count = db.update(DBConstant.Tab_Groups, values, selection,
//                        selectionArgs);
//                break;
//            case groupchannels:
//                count = db.update(DBConstant.Tab_GroupsChe, values, selection,
//                        selectionArgs);
//                break;
//            case customervos:
//                count = db.update(DBConstant.Tab_CustomerVo, values, selection,
//                        selectionArgs);
//                break;
//            case groupss_temp:
//                count = db.update(DBConstant.Tab_Groups_temp, values, selection,
//                        selectionArgs);
//                break;
//            case gpslocations:
//                count = db.update(DBConstant.gpslocation, values, selection,
//                        selectionArgs);
//                break;
//            case userinfos:
//                count = db.update(DBConstant.Tab_UserInfo, values, selection,
//                        selectionArgs);
//                break;
//            case repeaterinfos:
//                count = db.update(DBConstant.Tab_RepeaterInfo, values, selection,
//                        selectionArgs);
//                break;
            case jpjgroups:
                count = db.update(DBConstant.Tab_jpjGroup, values, selection,
                        selectionArgs);
                break;
            case jpjdevices:
                count = db.update(DBConstant.Tab_jpjDevice, values, selection,
                        selectionArgs);
                break;
            case jpjusers:
                count = db.update(DBConstant.Tab_jpjUser, values, selection,
                        selectionArgs);
                break;
        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
