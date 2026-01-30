/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.Logger;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * Utility class providing helper methods.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Util {
	private static final Logger LOG = Logger.ofClass(Util.class);

	public static boolean isBoolean(String value) {
		return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
	}

	public static boolean isInt(String value) {
		if (StringUtils.isNullOrEmpty(value, true)) {
			return false;
		}
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isNonInt(String value) {
		return !isInt(value);
	}

	public static int toInt(String value) {
		if (StringUtils.isNullOrEmpty(value, true)) {
			return -1;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			LOG.error("Failed to parse Integer from value '%s'".formatted(value), e);
			return -1;
		}
	}

	public static float toFloat(String value) {
		if (StringUtils.isNullOrEmpty(value, true)) {
			return -1;
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			LOG.error("Failed to parse Integer from value '%s'".formatted(value), e);
			return -1;
		}
	}
}
