package org.java.io.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class Utils {

	public static final String LIMIT = "$_";

	public static byte[] getLimit() {
		return "$_".getBytes();
	}

	public static String getContent(final String body) {
		String str = StringUtils.deleteWhitespace(body);

		String currentTime = "QUERY_TIME_ORDER".equalsIgnoreCase(str) ? new Date(System.nanoTime()).toString()
				: "BAD_ORDER";
		return currentTime;
	}

	public static String dateFormat(String pattern) {

		return DateFormatUtils.format(new Date(), pattern);

	}

}
