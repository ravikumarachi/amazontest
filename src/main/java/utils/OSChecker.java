package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSChecker {
	private static final Logger Log = LoggerFactory.getLogger(OSChecker.class.getName());
	public static final String WINDOWS = "windows";
	public static final String MAC = "mac";

	public static String getOS() {
		String os = null;

		if (isWindows()) {
			os = WINDOWS;
		} else if (isMac()) {
			os = MAC;
		} else {
			Log.info("Unable to establish OS from OSChecker.  Is not MAC or Windows");
		}
		
		return os;
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("mac") >= 0);
	}

	public static void main(String[] args) {
		System.out.println(getOS());
	}
}
