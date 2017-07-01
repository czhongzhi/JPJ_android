package com.chat.dbhelper;

import com.chat.dbentity.DBConstant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String TAG = "DBOpenHelper"; 

	private static final String DATABASE_NAME = "jpjchat.db";

	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, factory, version);
	}
	
	/* (non-Javadoc)
	 * 数据库创建
	 * 只有在首次或清除数据后首次启动才调用
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		createJpjGroup(db);
		createJpjDevice(db);
		createJpjUser(db);

//		createchatMessageTab(db);
//		createDownMessageTab(db);
//		createsoundMessageTab(db);
//		createCallMessageTab(db);
//		createGroups(db);
//		createGroAndChe(db);
//		createCustomerVo(db);
//		create_ptt_group_user(db);
//		create_ptt_group_temp(db);
//		createaloneChatMessage(db);
//		create_gpslocation(db);
//		createUserInfo(db);
//		createRepeaterInfo(db);
	}
	
	/* (non-Javadoc)
	 * 数据库升级 newVersion > oldVersion
	 * 数据库版本不一样的时候升级数据库
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG,"oldVersion :"+oldVersion+" newVersion :"+newVersion);
		if (newVersion > oldVersion) {
			

			
		}
	}
	
	// 
	/* (non-Javadoc)
	 * 数据库版本回退，newVersion < oldVersion
	 * 因为我们无法预知未来版本的表结构，向下兼容时最稳妥的方法就是将该版本自己需要的表重构一次
	 * @see android.database.sqlite.SQLiteOpenHelper#onDowngrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onDowngrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_jpjGroup);
		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_jpjDevice);
		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_jpjUser);
		createJpjGroup(db);
		createJpjDevice(db);
		createJpjUser(db);
		
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_downMessage);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_soundMessage);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_callMessage);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_Groups);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_CustomerVo);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_ppt_group_user);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_Groups_temp);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_AloneChatMessage);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.gpslocation);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_UserInfo);
//		db.execSQL("DROP TABLE IF EXISTS " + DBConstant.Tab_RepeaterInfo);
//
//		createchatMessageTab(db);
//		createDownMessageTab(db);
//		createsoundMessageTab(db);
//		createGroups(db);
//		createGroAndChe(db);
//		createCustomerVo(db);
//		create_ptt_group_user(db);
//		createaloneChatMessage(db);
//		create_ptt_group_temp(db);
//		create_gpslocation(db);
//		createUserInfo(db);
//		createRepeaterInfo(db);
		}

//	public void create_gpslocation(SQLiteDatabase db) {
//
//		db.execSQL("create table if not exists "
//				+ DBConstant.gpslocation
//				+ "("
//				+ "_id Integer primary key autoincrement,loactionname varchar(50),"
//				+ "error varchar(20),latitude varchar(50),radius varchar(50),addr varchar(255),operationers int,locationtime varchar(50),lontitude varchar(50))");
//	}
//
//	public void createchatMessageTab(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_chatMessage
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "msgtype varchar(20), groupid varchar(20),userid varchar(20),recvuserid varchar(20),"
//				+ "charsetname varchar(20),msgcontent varchar(200),"
//				+ "datetime varchar(50),display Integer,filemd5 varchar(50),fileid varchar(20),"
//				+ "filename varchar(50),filetype varchar(20),filepath varchar(100),filesize varchar(20),filestate varchar(20),filepercent varchar(20))");
//
//	}
//
//	public void createDownMessageTab(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_downMessage
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "msgtype varchar(20),userid varchar(20),groupid varchar(20),"
//				+ "threadid int,downlength int,state varchar(20),"
//				+ "downurl varchar(200))");
//	}
//
//	public void createsoundMessageTab(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_soundMessage
//				+ "("
//				+ "_id Integer primary key autoincrement,msgtype varchar(20),"
//				+ "calling_userid varchar(20), Called_userid varchar(20),groupid varchar(20),"
//				+ "startdatetime varchar(20),enddatetime varchar(20))");
//
//	}
//
//	public void createCallMessageTab(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_callMessage
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "msgtype varchar(20), calling_userid varchar(20),called_userid varchar(20),"
//				+ "groupid varchar(20),isanswer integer,"
//				+ "startdatetime varchar(50),enddatetime varchar(50))");
//	}
//
//	public void createaloneChatMessage(SQLiteDatabase db) {
//
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_AloneChatMessage
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "msgtype varchar(20), groupid varchar(20),userid varchar(20),recvuserid varchar(20),"
//				+ "charsetname varchar(20),msgcontent varchar(200),"
//				+ "datetime varchar(50),display Integer)");
//	}
//
//	public void createGroups(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_Groups
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "group_id varchar(20), group_name varchar(50),user_id varchar(20),"
//				+ "owner_id varchar(20),create_date varchar(50))");
//
//	}
//
//	public void createGroAndChe(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_GroupsChe
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "group_id varchar(20),"
//				+ "channel_num varchar(20))");
//
//	}
//
//	public void createCustomerVo(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_CustomerVo
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "user_id int, password varchar(50),phone varchar(20),"
//				+ "user_name varchar(20),grade Integer,logon Integer,"
//				+ "is_chat Integer,gender Integer,register_date varchar(50),group_id int,isaccount int)");
//
//	}
//
//	public void create_ptt_group_user(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_ppt_group_user + "("
//				+ "_id Integer primary key autoincrement,"
//				+ "group_id varchar(20), user_id int)");
//
//	}
//
//	public void create_ptt_group_temp(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_Groups_temp
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "group_id varchar(20), group_name varchar(50),user_id varchar(20),"
//				+ "owner_id varchar(20),create_date varchar(50))");
//
//	}
//
//	public void createUserInfo(SQLiteDatabase db) {
//
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_UserInfo
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "user_id int, password varchar(50),phone varchar(20),"
//				+ "user_name varchar(20),account varchar(50),gender varchar(20),"
//				+ "region varchar(20),file_MD5 varchar(50),file_name varchar(50))");
//	}
//
//	public void createRepeaterInfo(SQLiteDatabase db) {
//
//		db.execSQL("CREATE TABLE IF NOT EXISTS "
//				+ DBConstant.Tab_RepeaterInfo
//				+ "("
//				+ "_id Integer primary key autoincrement,"
//				+ "groupid varchar(20),user_id varchar(20),rx varchar(20),tx varchar(20),"
//				+ "modestate varchar(20),modestateptt varchar(20),modestatepoc varchar(20))");
//	}

	public void createJpjGroup(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.Tab_jpjGroup
				+ "("
				+ "_id Integer primary key autoincrement,"
				+ "group_id int,group_name varchar(50),owner_id int,create_date int,"
				+ "cmpid int,busiess_id int)");
	}

	public void createJpjDevice(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.Tab_jpjDevice
				+ "("
				+ "_id Integer primary key autoincrement,"
				+ "group_id int, deviceIndex int,deviceID varchar(20),"
				+ "user_name varchar(50),address varchar(100),cmpid int,"
				+ "busiess_id int,deviceType int,deviceManufactorName varchar(50),"
				+ "deviceModel varchar(50),deviceProductionDate varchar(50),deviceInstallDate varchar(50),"
				+ "deviceOrientation varchar(50),deviceTele varchar(20),deviceMeid varchar(50),"
				+ "deviceNetType int,deviceRunState int,deviceShieldState int,"
				+ "deviceDangerID int,deviceLng varchar(50),deviceLat varchar(50),"
				+ "lineID int, towerID int,account_type varchar(20),"
				+ "isjpjadmin varchar(50))");
	}

	public void createJpjUser(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.Tab_jpjUser
				+ "("
				+ "_id Integer primary key autoincrement,"
				+ "userid int,password varchar(128),username varchar(50),headerUrl varchar(255),phone varchar(20),"
				+ "sex varchar(2),area varchar(255),busiess_id int)");
	}

}
