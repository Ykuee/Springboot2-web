package com.ykuee.datamaintenance.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.ykuee.datamaintenance.common.base.exception.BusinessException;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 文件上传工具类
 * 
 * @author fuce
 * @date: 2018年9月22日 下午10:33:23
 */
public class FileUploadUtils {

	private FileUploadUtils() {
	}

	/**
	 * 默认上传的地址
	 */
	private static String defaultBaseDir;

	/**
	 * 默认的文件名最大长度
	 */
	public static final int DEFAULT_FILE_NAME_LENGTH = 100;
	/**
	 * 默认文件类型jpg
	 */
	public static final String IMAGE_JPG_EXTENSION = "jpg";

	public static final String POINT = ".";

	private static final int DEFAULT_MAX_SIZE = 5;

	private static int counter = 0;

	@Value(value = "${upload.defUploadPath}")
	public static void setDefaultBaseDir(String defaultBaseDir) {
		FileUploadUtils.defaultBaseDir = defaultBaseDir;
	}

	public static String getDefaultBaseDir() {
		return defaultBaseDir;
	}

	@Value(value = "${upload.defMaxSize}")
	public static int getDefaultMaxSize() {
		return DEFAULT_MAX_SIZE;
	}

	/**
	 * 以默认配置进行文件上传
	 *
	 * @param file 上传的文件
	 * @return 文件名称
	 * @throws Exception
	 */
	public static final String upload(MultipartFile file) throws IOException {
		try {
			return upload(getDefaultBaseDir(), file);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public static final String upload(String baseDir, MultipartFile file) throws IOException {
		try {
			return upload(baseDir, file, null, null);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public static final String upload(String baseDir, MultipartFile file, String fileName) throws IOException {
		try {
			return upload(baseDir, file, fileName, null);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * Title: upload
	 * </p>
	 * <p>
	 * Description: 上传文件
	 * </p>
	 * 
	 * @author Ykuee
	 * @date 2021-4-20
	 * @param baseDir
	 * @param file
	 * @param maxSize 单位 MB
	 * @return
	 * @throws IOException
	 */
	public static final String upload(String baseDir, MultipartFile file, Long maxSize) throws IOException {
		try {
			return upload(baseDir, file, null, maxSize);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * 文件上传
	 *
	 * @param baseDir                   相对应用的基目录
	 * @param file                      上传的文件
	 * @param needDatePathAndRandomName 是否需要日期目录和随机文件名前缀
	 * @param extension                 上传文件类型
	 * @param maxSize                   上传文件最大值 单位 MB 默认5MB
	 * @return 返回上传成功的文件名
	 * @throws IOException 比如读写文件出错时
	 */
	public static final String upload(String baseDir, MultipartFile file, String fileName, Long maxSize)
			throws FileSizeLimitExceededException, IOException {
		if (StrUtil.isBlank(fileName)) {
			fileName = file.getOriginalFilename();
		}
		// 获得文件后缀名称
		String suffixName = "";
		if (fileName.lastIndexOf(".") != -1) {
			suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
		}
		if (StrUtil.isBlank(suffixName)) {
			// 如果没有后缀默认后缀
			suffixName = FileTypeUtil.getType(file.getInputStream());
			if (StrUtil.isBlank(suffixName)) {
				suffixName = IMAGE_JPG_EXTENSION;
			}
			suffixName = POINT + suffixName;
		}

		int fileNamelength = fileName.length();
		if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
			throw new BusinessException("上传文件名过长");
		}

		assertAllowed(file);

		String new_fileName = fileName + suffixName;

		File desc = getAbsoluteFile(baseDir, baseDir + new_fileName);
		file.transferTo(desc);
		return new_fileName;
	}

	public static void main(String[] args) {
		try {
			delSameNameFile("e:/upload/vehiclePartPic/NUDE/", "20210118122319");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	  *<p>Title: delSameNameFile</p>
	  *<p>Description: 删除相同文件名的文件 忽略后缀文件名</p>
	  * @author Ykuee
	  * @date 2021-4-20 
	  * @param uploadDir 路径名 /upload/test/
	  * @param filename 要匹配的文件名abc 不带后缀
	  * @return
	  * @throws IOException
	 */
	public static final void delSameNameFile(String uploadDir, String filename) throws IOException {
		File file = new File(uploadDir);
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				String sameName = "";
				if (StrUtil.isNotBlank(f.getName()) && f.getName().lastIndexOf(".") != -1) {
					sameName = f.getName().substring(0, f.getName().lastIndexOf("."));
				}
				if (sameName.equals(filename)) {
					f.delete();
				}
			}
		}
	}

	private static final File getAbsoluteFile(String uploadDir, String filename) throws IOException {
		File desc = new File(File.separator + filename);

		if (!desc.getParentFile().exists()) {
			desc.getParentFile().mkdirs();
		}
		if (!desc.exists()) {
			desc.createNewFile();
		}
		return desc;
	}

	/**
	 * 文件大小校验
	 *
	 * @param file 上传的文件
	 * @return
	 * @throws FileSizeLimitExceededException 如果超出最大大小
	 */
	public static final void assertAllowed(MultipartFile file) throws FileSizeLimitExceededException {
		assertAllowed(file, null);
	}

	public static final void assertAllowed(MultipartFile file, Long maxSize) throws FileSizeLimitExceededException {
		long size = file.getSize();
		if (maxSize == null || maxSize == 0) {
			maxSize = Long.valueOf(DEFAULT_MAX_SIZE * 1024 * 1024);
		}
		if (maxSize != -1 && size > maxSize) {
			throw new FileSizeLimitExceededException("超过默认大小", size, maxSize);
		}
	}

}
