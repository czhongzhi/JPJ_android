package com.runbo.jpj.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class FileUtil {
	
	private static File updateDir = null;
	private static File updateFile = null;

	/***
	 * 创建文件
	 */
	public static File createFile(Context context, String name) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			if(updateFile != null){
				return updateFile;
			}
			
			updateDir = new File(Environment.getExternalStorageDirectory().getPath());
			updateFile = new File(updateDir + "/" + name + ".apk");

			if (!updateDir.exists()) {
				updateDir.mkdirs();
			}
			if (!updateFile.exists()) {
				try {
					updateFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
		return updateFile;
	}
	
	
	/**
	 * 文件重命名
	 * @param file 要重命名的文件
	 * @param newName 命名的字!
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-3-4<br />
	 * 修改时间:<br />
	 */
	public static boolean reNameFile(File file, String newName){
		return file.renameTo(new File(file.getParentFile(), newName));
	}
	
	/**
	 * SD卡可用容量
	 * @return 字节数
	 *  -1  SD card 读取空间错误!
	 * 作者:fighter <br />
	 * 创建时间:2013-3-4<br />
	 * 修改时间:<br />
	 */
	public static long SDCardAvailable(){
		try {
			StatFs statFs = new StatFs(getExternalDirectory());
			return (long)statFs.getBlockSize() * (long)statFs.getAvailableBlocks();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * SD卡容量是否还有可用容量 ( 基数为 40MB )
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-4-16<br />
	 * 修改时间:<br />
	 */
	public static boolean isSDCardAvailable(){
		long volume = SDCardAvailable();
		long mb = 1024 * 1024 * 40;
		if(volume > mb){
			return true;
		}else{
			return false;
		}
	}
	
	public static String getExternalDirectory(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	} 
	
	/**
	 * SD卡是否可用
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-5-6<br />
	 * 修改时间:<br />
	 */
	public static boolean isMounted(){
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	/**
	 * 判断两个字符串大小
	 * */
	public static boolean isSort(String str1,String str2){
		if(str1.hashCode()<=str2.hashCode()){
			return true;
		}else{
		return false;
		}
	}
}
