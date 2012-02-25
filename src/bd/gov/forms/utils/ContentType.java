package bd.gov.forms.utils;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class ContentType {
	private static Map<String, String> d = new HashMap<String, String>();
	private static ContentType contentType;

	private ContentType() {
		d.put(".bmp", "image/bmp");
		d.put(".gif", "image/gif");
		d.put(".jpeg", "image/jpeg");
		d.put(".jpg", "image/jpeg");
		d.put(".png", "image/png");
		d.put(".tif", "image/tiff");
		d.put(".tiff", "image/tiff");

		d.put(".doc", "application/msword");
		d.put(".docx",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		d.put(".pdf", "application/pdf");

		d.put(".ppt", "application/vnd.ms-powerpoint");
		d.put(".pptx",
				"application/vnd.openxmlformats-officedocument.presentationml.presentation");

		d.put(".xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		d.put(".xls", "application/vnd.ms-excel");
		d.put(".csv", "text/csv");
		d.put(".xml", "text/xml");
		d.put(".txt", "text/plain");

		d.put(".zip", "application/zip");

		d.put(".ogg", "application/ogg");
		d.put(".mp3", "audio/mpeg");
		d.put(".wma", "audio/x-ms-wma");
		d.put(".wav", "audio/x-wav");

		d.put(".wmv", "audio/x-ms-wmv");
		d.put(".swf", "application/x-shockwave-flash");
		d.put(".avi", "video/avi");
		d.put(".mp4", "video/mp4");
		d.put(".mpeg", "video/mpeg");
		d.put(".mpg", "video/mpeg");
		d.put(".qt", "video/quicktime");
	}

	/**
	 * Method getInstance.
	 * @return ContentType
	 */
	public static ContentType getInstance() {
		if (contentType == null) {
			return new ContentType();
		}
		return contentType;
	}

	/**
	 * Method getContentType.
	 * @param key String
	 * @return String
	 */
	public String getContentType(String key) {
		return d.get(key.toLowerCase());
	}
}
