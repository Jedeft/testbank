package com.ncu.testbank.base.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

/**
 * 七牛图片工具类
 * 
 * @author Jedeft
 * @date 2016年2月4日 下午7:58:10
 */
public class QiniuImgUtil {
	private static UploadManager uploadManager;

	static {
		uploadManager = new UploadManager();
	}

	/**
	 * 通过文件存储图片
	 * 
	 * @param space
	 *            图片存储空间名
	 * @param key
	 *            图片Key值(必须采用UTF-8编码)
	 * @param file
	 *            文件名
	 * @return 图片查询Key值
	 * @throws QiniuException
	 */
	public static String uploadImg(String space, String key, File file) throws QiniuException {
		Auth auth = initAuth();
		if (uploadManager == null) {
			uploadManager = new UploadManager();
		}
		String uploadToken = auth.uploadToken(space, key);
		Response res = uploadManager.put(file, key, uploadToken);
		return res.jsonToMap().get("key").toString();
	}

	/**
	 * 通过字节存储图片
	 * 
	 * @param space
	 *            图片存储空间名
	 * @param key
	 *            图片Key值(必须采用UTF-8编码)
	 * @param data
	 *            文件字节数组
	 * @return 图片查询Key值
	 * @throws QiniuException
	 */
	public static String uploadImg(String space, String key, byte[] data) throws QiniuException {
		Auth auth = initAuth();
		if (uploadManager == null) {
			uploadManager = new UploadManager();
		}
		String uploadToken = auth.uploadToken(space, key);
		Response res = uploadManager.put(data, key, uploadToken);
		return res.jsonToMap().get("key").toString();
	}

	/**
	 * 获取私有资源下载Token
	 * 
	 * @param url
	 *            资源地址
	 * @return 私有资源Token值
	 */
	public static String obtainDownloadToken(String url) {
		Auth auth = initAuth();
		return auth.privateDownloadUrl(url);
	}

	/**
	 * 获取文件详细信息
	 * 
	 * @param space
	 *            图片存储空间名
	 * @param key
	 *            图片Key值(必须采用UTF-8编码)
	 * @return file文件详细信息
	 * @throws QiniuException
	 */
	public static FileInfo getFileInfo(String space, String key) throws QiniuException {
		Auth auth = initAuth();
		BucketManager bucketManager = new BucketManager(auth);
		return bucketManager.stat(space, key);
	}

	/**
	 * 删除文件
	 * 
	 * @param space
	 *            图片存储空间名
	 * @param key
	 *            图片Key值(必须采用UTF-8编码)
	 * @throws QiniuException
	 */
	public static void delete(String space, String key) throws QiniuException {
		Auth auth = initAuth();
		BucketManager bucketManager = new BucketManager(auth);
		bucketManager.delete(space, key);
	}

	/**
	 * 修改文件key值
	 * 
	 * @param space
	 *            图片存储空间名
	 * @param oldKey
	 *            图片当前Key值(必须采用UTF-8编码)
	 * @param newKey
	 *            图片修改后Key值(必须采用UTF-8编码)
	 * @throws QiniuException
	 */
	public static void rename(String space, String oldKey, String newKey) throws QiniuException {
		Auth auth = initAuth();
		BucketManager bucketManager = new BucketManager(auth);
		bucketManager.rename(space, oldKey, newKey);
	}

	/**
	 * 移动文件
	 * 
	 * @param space
	 *            当前图片存储空间名
	 * @param key
	 *            图片当前Key值(必须采用UTF-8编码)
	 * @param targetSpace
	 *            移动目标图片存储空间名
	 * @param targetKey
	 *            移动目标图片Key值(必须采用UTF-8编码)
	 * @throws QiniuException
	 */
	public static void move(String space, String key, String targetSpace, String targetKey) throws QiniuException {
		Auth auth = initAuth();
		BucketManager bucketManager = new BucketManager(auth);
		bucketManager.move(space, key, targetSpace, targetKey);
	}

	/**
	 * 复制文件
	 * 
	 * @param space
	 *            当前图片存储空间名
	 * @param key
	 *            图片当前Key值(必须采用UTF-8编码)
	 * @param targetSpace
	 *            复制目标图片存储空间名
	 * @param targetKey
	 *            复制目标图片Key值(必须采用UTF-8编码)
	 * @throws QiniuException
	 */
	public static void copy(String space, String key, String targetSpace, String targetKey) throws QiniuException {
		Auth auth = initAuth();
		BucketManager bucketManager = new BucketManager(auth);
		bucketManager.copy(space, key, targetSpace, targetKey);
	}

	private static Auth initAuth() {
		Properties properties = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("qiniu.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String ACCESS_KEY = properties.getProperty("AK");
		String SECRET_KEY = properties.getProperty("SK");

		Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
		return auth;
	}
}
