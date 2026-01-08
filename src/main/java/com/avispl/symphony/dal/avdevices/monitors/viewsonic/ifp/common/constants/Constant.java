/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class that defines constant values used across the application.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {
	//	Formats
	public static final String PROPERTY_FORMAT = "%s#%s";
	public static final String NUMBER_FORMAT = "\\d+";
	public static final String TWO_DIGIT_NUMBER_FORMAT = "%02d";
	public static final String THREE_DIGIT_NUMBER_FORMAT = "%03d";
	public static final String MAC_PAIR_REGEX = "..(?!$)";
	public static final String MAC_SEPARATOR_REPLACEMENT = "$0:";

	//	Special characters
	public static final String CR = "\r";
	public static final String EMPTY = "";
	public static final String HASH = "#";

	//	Values
	public static final String NOT_AVAILABLE = "N/A";
	public static final String COMMAND_LENGTH = "8";
	public static final String GET_COMMAND_TYPE = "g";
	public static final String SET_COMMAND_TYPE = "s";
	public static final String GET_RESPONSE_HEADER_1 = "201";
	public static final String GET_RESPONSE_HEADER_2 = "801";
	public static final String SET_RESPONSE_HEADER = "401";
	public static final char NEGATIVE_ACK = '-';
	public static final String OFF = "Off";
	public static final String STANDBY = "Standby";
	public static final String ON = "On";
	public static final float PERCENTAGE_MIN = 0f;
	public static final float PERCENTAGE_MAX = 100f;

	//	Groups
	public static final String GENERAL = "General";
	public static final String ADAPTER_METADATA_GROUP = "AdapterMetadata";
	public static final String GENERAL_SETTING_GROUP = "GeneralSettings";
	public static final String DISPLAY_GROUP = "Display";

	//	Fail messages
	public static final String READ_PROPERTIES_FILE_FAILED = "Failed to load version properties file.";
	public static final String SET_DEVICE_ID_FAILED = "Invalid deviceId property. Please provide a value from 01 to 98.";
}
