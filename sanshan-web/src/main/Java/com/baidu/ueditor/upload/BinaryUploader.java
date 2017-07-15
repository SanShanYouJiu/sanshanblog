package com.baidu.ueditor.upload;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 注意 此处是将文件保存在本地的Ueditor/upload文件夹下
 *
 * 被Spring MVC包装过的Request 自定义实现
 */
@Slf4j
public class BinaryUploader {

	public static final State save(HttpServletRequest request,
								   Map<String, Object> conf) {
		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;
		//因为Spring内置的上传处理会装饰Request
		DefaultMultipartHttpServletRequest multipartHttpServletRequest= (DefaultMultipartHttpServletRequest) request;

		MultipartFile multipartFile=multipartHttpServletRequest.getFile("upfile");
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		//ServletFileUpload upload = new ServletFileUpload(
		//		new DiskFileItemFactory());

		if ( isAjaxUpload ) {
			//如果是Ajax访问的话 就怎么怎么 但是我前端是promise
			//upload.setHeaderEncoding( "UTF-8" );
		}

		try {
			//FileItemIterator iterator = upload.getItemIterator(request);
			//while (iterator.hasNext()) {
			//	fileStream = iterator.next();
            //
				//if (!fileStream.isFormField())
				//	break;
				//fileStream = null;
			//}

			//if (fileStream == null) {
			//	return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			//}
			if (multipartFile==null){
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("savePath");
			String originFileName = multipartFile.getOriginalFilename();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

			String physicalPath = (String) conf.get("rootPath") + savePath;

			InputStream is = multipartFile.getInputStream();
			//新实现
			State storageState = StorageManager.saveFileByInputStream(is, savePath,suffix, maxSize);
			is.close();

			if (storageState.isSuccess()) {
				storageState.putInfo("url", PathFormat.format(savePath));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}

			return storageState;
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}


}
