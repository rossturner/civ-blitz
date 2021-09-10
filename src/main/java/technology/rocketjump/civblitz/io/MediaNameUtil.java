package technology.rocketjump.civblitz.io;

import java.net.URLDecoder;
import java.nio.charset.Charset;

public class MediaNameUtil {

	public static String simplify(String mediaFilename) {
		return urlDecode(mediaFilename).replace("_(Civ6)", "").replaceAll("[^A-Za-z0-9\\._]","");
	}

	private static String urlDecode(String filename) {
		return URLDecoder.decode(filename, Charset.defaultCharset());
	}

}
