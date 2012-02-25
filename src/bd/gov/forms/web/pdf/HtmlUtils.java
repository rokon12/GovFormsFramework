package bd.gov.forms.web.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 */
public class HtmlUtils {
	private static final Logger log = Logger.getLogger(HtmlUtils.class);

	/**
	 * Removes html tag from the input text. Tag to be removed is defined by
	 * start and end tag type and an optional pattern. Example usage - (data,
	 * "<div", "</div>", "<div id=\"navbar\""); This will remove the div that
	 * starts with id="navbar".
	 * 
	 * @param text
	 * @param startTag
	 * @param endTag
	 * @param pattern
	
	 * @return String
	 */
	public static String removeTag(String text, String startTag, String endTag,
			String pattern) {
		int startIndex = 0;
		int endIndex = -1;
		if (pattern != null) {
			log.debug("looking for pattern - " + pattern);
			startIndex = text.indexOf(pattern);
		} else {
			startIndex = text.indexOf(startTag);
		}

		endIndex = text.indexOf(endTag, startIndex);

		if ((startIndex == -1) || (endIndex == -1)) {
			return text;
		}

		if (startIndex > endIndex) {
			log.warn("Pattern mismatched!");
			return text;
		}

		int tempStartIndex = startIndex;

		String temp = text.substring(tempStartIndex + startTag.length(),
				endIndex + endTag.length());
		int i = 0;
		while (temp.contains(startTag)) {

			tempStartIndex = tempStartIndex
					+ temp.indexOf(startTag, startTag.length())
					+ startTag.length();
			endIndex = text.indexOf(endTag, endIndex + 1);

			if (tempStartIndex > endIndex) {
				log.warn("Failed to find specified tag. Returning...");
				return text;
			}
			temp = text.substring(tempStartIndex + startTag.length(), endIndex
					+ endTag.length());
		}

		String toRemove = text
				.substring(startIndex, endIndex + endTag.length());

		return text.replace(toRemove, " ");

	}

	/**
	 * Method removeAllTags.
	 * @param text String
	 * @param startTag String
	 * @param endTag String
	 * @param pattern String
	 * @return String
	 */
	public static String removeAllTags(String text, String startTag,
			String endTag, String pattern) {
		String output = "";
		do {
			output = text;
			text = removeTag(text, startTag, endTag, pattern);
		} while (!text.equals(output));

		return text;
	}

	/**
	 * Method replaceAll.
	 * @param text String
	 * @param regex String
	 * @param replaceWith String
	 * @return String
	 */
	public static String replaceAll(String text, String regex,
			String replaceWith) {
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(text);
		StringBuffer buffer = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(buffer, replaceWith);
		}
		m.appendTail(buffer);

		return buffer.toString();
	}

	/**
	 * Method getGroupTextList.
	 * @param text String
	 * @param regex String
	 * @param groupNumber int
	 * @return List<String>
	 */
	public static List<String> getGroupTextList(String text, String regex,
			int groupNumber) {
		log.debug("getGroupTextList-e:");

		List<String> groupTextList = new ArrayList();
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(text);
		while (m.find()) {
			groupTextList.add(m.group(groupNumber));
		}

		return groupTextList;
	}

	/**
	 * Method getGroupText.
	 * @param text String
	 * @param regex String
	 * @param groupNumber int
	 * @return String
	 */
	public static String getGroupText(String text, String regex, int groupNumber) {
		log.debug("getGroupText-e:");

		List<String> groupTextList = getGroupTextList(text, regex, groupNumber);
		if (groupTextList.size() > 0) {
			return groupTextList.get(0);
		}
		return "";
	}

	/**
	 * Method insertText.
	 * @param text String
	 * @param string String
	 * @param startTag String
	 * @param endTag String
	 * @return String
	 */
	public static String insertText(String text, String string,
			String startTag, String endTag) {

		if ((text.contains(startTag) && text.contains(endTag))) {
			int index = text.indexOf(startTag) + startTag.length();
			String firstPart = text.substring(0, index);
			String lastPart = text.substring(index, text.length());
			text = firstPart + "\n" + string + lastPart;
		}
		return text;
	}
}