package com.auphi.ktrl.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.prefs.Preferences;

import de.schlichtherle.license.*;
import org.apache.log4j.Logger;
import org.pentaho.di.repository.kdr.delegates.KettleDatabaseRepositoryUserDelegate;

public class VerifyLicense {
	private static Logger logger = Logger.getLogger(VerifyLicense.class);

	KeyStoreParam privateKeyStoreParam = new KeyStoreParam() {
		public InputStream getStream() throws IOException {
			final String resourceName = "/publicCerts.store";
			final InputStream in = getClass().getResourceAsStream(resourceName);
			if (in == null)
				throw new FileNotFoundException(resourceName);
			return in;
		}

		public String getAlias() {
			return "publiccert1";
		}

		public String getStorePwd() {
			return "k1ngbas3";
		}

		public String getKeyPwd() {
			return null;
		}
	};

	CipherParam cipherParam = new CipherParam() {
		public String getKeyPwd() {
			return "k1ngbas3";
		}
	};

	LicenseParam licenseParam = new LicenseParam() {

		public String getSubject() {
			return "KettlePlatform";
		}

		public Preferences getPreferences() {
			return Preferences.userNodeForPackage(VerifyLicense.class);
		}

		public KeyStoreParam getKeyStoreParam() {
			return privateKeyStoreParam;
		}

		public CipherParam getCipherParam() {
			return cipherParam;
		}
	};

	public void install() throws Exception {
		LicenseManager lm = new LicenseManager(licenseParam);
		File licenseFile = new File("KettlePlatform.lic");
		lm.install(licenseFile);
	}

	public void install(String fileName) throws Exception {

		LicenseManager lm = new LicenseManager(licenseParam);
		File licenseFile;
		if(fileName==null || fileName.length()==0){
			licenseFile= new File("KettlePlatform.lic");
		}

		else{
			licenseFile= new File(fileName);
		}


		lm.install(licenseFile);
		LicenseContent lc = lm.verify();
		System.out.println("License Valid Until:"+StringUtil.DateToString(lc.getNotAfter(),"yyyy-MM-dd HH:mm:ss") );
		//时间小于1年不验证 Mac 地址
		if( (lc.getNotAfter().getTime()- System.currentTimeMillis()) > 1000L*60*60*24*365) {
			ArrayList<String> list = getAllMacs();
			boolean found = false;

			System.out.println("License MAC:"+lc.getInfo());
			for (String e : list) {
				System.out.println("MAC:"+e);
				if (e.equalsIgnoreCase(lc.getInfo())){
					found = true;
				}
			}
			if (!found) {
				throw new Exception("No Valid MAC found in Mac List");
			}
		}
	}

	public static ArrayList getAllMacs() {
		ArrayList list = new ArrayList();
		try {

			Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
			while (enumeration.hasMoreElements()) {
				StringBuffer stringBuffer = new StringBuffer();
				NetworkInterface networkInterface = enumeration.nextElement();
				if (networkInterface != null) {
					byte[] bytes = networkInterface.getHardwareAddress();
					if (bytes != null) {
						for (int i = 0; i < bytes.length; i++) {
//							if (i != 0) {
//								stringBuffer.append("-");
//							}
							int tmp = bytes[i] & 0xff; // 字节转换为整数
							String str = Integer.toHexString(tmp);
							if (str.length() == 1) {
								stringBuffer.append("0" + str);
							} else {
								stringBuffer.append(str);
							}
						}
						String mac = stringBuffer.toString().toUpperCase();
						list.add(mac);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {

		System.out.println(1000L*60*60*24*365*5);
		VerifyLicense g = new VerifyLicense();
		try {
			g.install();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}
}


