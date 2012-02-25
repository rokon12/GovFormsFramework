package bd.gov.forms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import bd.gov.forms.domain.ListData;

public class ListUtil {
	/**
	 * Method getList.
	 * 
	 * @param type
	 *            String
	 * @return List
	 * @throws Exception
	 */
	public static List getList(String type, ListData data) throws Exception {
		List<String> list = new ArrayList<String>();

		if ("select".equals(type)) {
			list.add("");
		}

		if (data.getValues() != null) {
			list.addAll(getNewlineDelimitedValuesAsList(data.getValues()));
		}

		return list;
	}

	/**
	 * Method getNewlineDelimitedValuesAsList.
	 * 
	 * @param values
	 *            String
	 * @return List<String>
	 * @throws IOException
	 */
	private static List<String> getNewlineDelimitedValuesAsList(String values)
			throws IOException {
		List<String> list = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new StringReader(values));

		String str;
		while ((str = reader.readLine()) != null) {
			str = str.trim();
			if (str.length() > 0) {
				list.add(str);
			}
		}

		return list;
	}
}
