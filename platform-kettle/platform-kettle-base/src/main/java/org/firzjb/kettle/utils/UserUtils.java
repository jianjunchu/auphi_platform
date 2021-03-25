package org.firzjb.kettle.utils;

import java.io.File;

import org.firzjb.base.common.UserUtil;
import org.firzjb.base.model.response.CurrentUserResponse;

public class UserUtils {

	public static String decodePath(CurrentUserResponse user, String path) {
		String userDir = org.firzjb.base.common.Const.getUserDir(user.getOrganizerId());

		//如果路径本身就是以用户目录开头
		if(path.startsWith(userDir))
			return path;

		//如果本身是绝对路径
		File oriFile = new File(path);
		if(oriFile.isAbsolute())
			return path;

		if(path.startsWith(File.separator))
			path = path.substring(1);

		File dir = new File(userDir);
		if(!dir.isDirectory()) {
			dir.mkdirs();
		}

		File file = new File(dir, path);
		return file.getAbsolutePath();
	}

	public static String encodePath(CurrentUserResponse user, String path) {
		String userDir = org.firzjb.base.common.Const.getUserDir(user.getOrganizerId());

		if(path.startsWith(userDir)) {
			String path2 = path.substring(userDir.length());
			if(path2.startsWith(File.separator))
				return path2.substring(File.separator.length());
		}

		return path;
	}

	public static void main(String[] args) {
		String str = "D:\\data\\1\\1111\\2222";
		String str2 = "D:\\data\\1";

		String path2 = str.substring(str2.length());
		if(path2.startsWith(File.separator))
			path2 = path2.substring(File.separator.length());
		System.out.println(path2);
	}

}
