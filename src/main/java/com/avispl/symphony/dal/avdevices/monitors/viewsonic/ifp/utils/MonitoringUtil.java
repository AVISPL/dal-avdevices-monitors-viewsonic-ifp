/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseProperty;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.AdapterMetadata;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * Utility class providing helper methods for monitoring property.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MonitoringUtil {
	/**
	 * Generates a map of property names and their corresponding values.
	 * <p>
	 * Each property name can be optionally prefixed with a group name using a predefined format.
	 * The values are derived using the provided mapping function, with {@link Constant#NOT_AVAILABLE} as a fallback for null results.
	 * </p>
	 *
	 * @param <T> the enum type that extends {@link BaseProperty}
	 * @param properties the array of enum constants to be processed; if null, an empty map is returned
	 * @param groupName optional group name used to prefix each property's name; can be null
	 * @param mapper a function that maps each property to its corresponding string value;
	 * if null or if the result is null, {@link Constant#NOT_AVAILABLE} is used as the value
	 * @return a map where keys are (optionally grouped) property names and values are mapped strings or {@link Constant#NOT_AVAILABLE}
	 */
	public static <T extends Enum<T> & BaseProperty> Map<String, String> generateProperties(T[] properties, String groupName, Function<T, String> mapper) {
		if (properties == null || mapper == null) {
			return Collections.emptyMap();
		}
		return Arrays.stream(properties).collect(Collectors.toMap(
				property -> Objects.isNull(groupName) ? property.getName() : String.format(Constant.PROPERTY_FORMAT, groupName, property.getName()),
				property -> Optional.ofNullable(mapper.apply(property)).orElse(Constant.NOT_AVAILABLE)
		));
	}

	/**
	 * Generates adapter metadata map from version properties. Returns empty property if null or all values unavailable.
	 *
	 * @param versionProperties adapter version and build information
	 * @return the Adapter Metadata map
	 */
	public static String mapToAdapterMetadata(Properties versionProperties, AdapterMetadata adapterMetadata) {
		if (versionProperties == null) {
			return null;
		}
		return switch (adapterMetadata) {
			case ADAPTER_UPTIME -> mapToUptime(versionProperties.getProperty(adapterMetadata.getProperty()));
			case ADAPTER_UPTIME_MIN -> mapToUptimeMin(versionProperties.getProperty(adapterMetadata.getProperty()));
			default -> mapToValue(versionProperties.getProperty(adapterMetadata.getProperty()));
		};
	}

	/**
	 * Maps the given value to a formatted string using title case for normal text.
	 * <p>
	 * Delegates to {@link #mapToValue(Object, boolean)} with {@code isTitleCase = true}.
	 * </p>
	 *
	 * @param value the input value to map
	 * @return the mapped string, or {@code Constant.NOT_AVAILABLE} if unavailable
	 */
	private static String mapToValue(Object value) {
		return mapToValue(value, true);
	}

	/**
	 * Maps the given value to a formatted string based on its type:
	 * <ul>
	 *   <li>For non-empty strings:
	 *     <ul>
	 *       <li>Returns "true" / "false" in lowercase if the value represents a boolean.</li>
	 *       <li>Returns title-cased or raw text depending on {@code isTitleCase}.</li>
	 *     </ul>
	 *   </li>
	 *   <li>For {@link Boolean} or {@link Integer}, returns their string value.</li>
	 *   <li>Returns {@code null} or unsupported types.</li>
	 * </ul>
	 *
	 * @param value the value to map
	 * @param isTitleCase whether normal string values should be converted to title case
	 * @return the mapped string, or {@code null} if unavailable
	 */
	private static String mapToValue(Object value, boolean isTitleCase) {
		if (value == null) {
			return null;
		}
		if (value instanceof String str) {
			if (StringUtils.isNullOrEmpty(str)) {
				return null;
			}
			if (isBooleanValue(str)) {
				return str.toLowerCase();
			}
			return isTitleCase ? toTitleCase(str) : str;
		}
		if (value instanceof Boolean || value instanceof Integer) {
			return value.toString();
		}

		return null;
	}

	/**
	 * Capitalizes the first character of the input string.
	 * <p>
	 * If the input is {@code null}, empty, or the literal string {@code "null"}, this method returns {@code null}.
	 * If the input is {@code "true"} or {@code "false"}, the method returns the input unchanged.
	 * Otherwise, it returns the input string with its first character converted to uppercase.
	 * </p>
	 *
	 * @param value the input string to convert
	 * @return a string with the first character capitalized, or {@code null} if the input is invalid
	 */
	private static String toTitleCase(String value) {
		if (StringUtils.isNullOrEmpty(value) || value.equals("null")) {
			return null;
		}
		if (isBooleanValue(value)) {
			return value;
		}

		return Character.toUpperCase(value.charAt(0)) + value.substring(1);
	}

	/**
	 * Returns the elapsed uptime between the current system time and the given timestamp in milliseconds.
	 * <p>
	 * The input timestamp represents the start time in milliseconds (typically from {@link System#currentTimeMillis()}).
	 * The returned string represents the absolute duration in the format:
	 * "X day(s) Y hour(s) Z minute(s) W second(s)", omitting any zero-value units except seconds.
	 *
	 * @param uptime the start time in milliseconds as a string (e.g., "1717581000000")
	 * @return a formatted duration string like "2 day(s) 3 hour(s) 15 minute(s) 42 second(s)", or null if parsing fails
	 */
	private static String mapToUptime(String uptime) {
		try {
			if (StringUtils.isNullOrEmpty(uptime)) {
				return null;
			}

			long uptimeSecond = (System.currentTimeMillis() - Long.parseLong(uptime)) / 1000;
			long seconds = uptimeSecond % 60;
			long minutes = uptimeSecond % 3600 / 60;
			long hours = uptimeSecond % 86400 / 3600;
			long days = uptimeSecond / 86400;
			StringBuilder rs = new StringBuilder();
			if (days > 0) {
				rs.append(days).append(" day(s) ");
			}
			if (hours > 0) {
				rs.append(hours).append(" hour(s) ");
			}
			if (minutes > 0) {
				rs.append(minutes).append(" minute(s) ");
			}
			rs.append(seconds).append(" second(s)");

			return rs.toString().trim();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns the elapsed uptime in **whole minutes** between the current system time and the given timestamp in milliseconds.
	 * <p>
	 * The input timestamp represents the start time in milliseconds (typically from {@link System#currentTimeMillis()}).
	 * The returned string is the total number of minutes that have elapsed, excluding seconds.
	 *
	 * @param uptime the start time in milliseconds as a string (e.g., "1717581000000")
	 * @return a string representing the total number of elapsed minutes (e.g., "125"), or null if parsing fails
	 */
	private static String mapToUptimeMin(String uptime) {
		try {
			if (StringUtils.isNullOrEmpty(uptime)) {
				return null;
			}

			long uptimeSecond = (System.currentTimeMillis() - Long.parseLong(uptime)) / 1000;
			long minutes = uptimeSecond / 60;

			return String.valueOf(minutes);
		} catch (Exception e) {
			return null;
		}
	}

	private static boolean isBooleanValue(String value) {
		return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
	}
}
