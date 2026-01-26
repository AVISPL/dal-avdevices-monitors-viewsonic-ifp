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
	public static final String TWO_DIGIT_NUMBER_FORMAT = "%02d";
	public static final String THREE_DIGIT_NUMBER_FORMAT = "%03d";
	public static final String MAC_PAIR_REGEX = "..(?!$)";
	public static final String MAC_SEPARATOR_REPLACEMENT = "$0:";

	//	Special characters
	public static final String CR = "\r";
	public static final String EMPTY = "";
	public static final String HASH = "#";
	public static final String COMMA = ",";
	public static final String COMMA_SPACE = ", ";

	//	Values
	public static final String NOT_AVAILABLE = "N/A";
	public static final String ACK_RESPONSE_LENGTH = "8";
	public static final String STATUS_RESPONSE_LENGTH = "4";
	public static final String GET_COMMAND_TYPE = "g";
	public static final String SET_COMMAND_TYPE = "s";
	public static final String ACK_RESPONSE_HEADER_1 = "2";
	public static final String ACK_RESPONSE_HEADER_2 = "8";
	public static final char NEGATIVE_ACK = '-';
	public static final String OFF = "Off";
	public static final String STANDBY = "Standby";
	public static final String ON = "On";
	public static final float PERCENTAGE_MIN = 0f;
	public static final float PERCENTAGE_MAX = 100f;
	public static final String ALL = "All";

	//	Groups
	public static final String GENERAL_GROUP = "General";
	public static final String ADAPTER_METADATA_GROUP = "AdapterMetadata";
	public static final String SETTING_GROUP = "Settings";
	public static final String DISPLAY_GROUP = "Display";

	//	Warning messages
	public static final String CONTROLLABLE_PROPS_EMPTY_WARNING = "ControllableProperties list is null or empty, skipping control operation";
	public static final String INVALID_VALUE_WARNING = "The value is invalid(%s), returning null.";
	public static final String NO_VALID_DISPLAY_PROPERTY_GROUPS_WARNING = "No valid display property groups found from input: '%s'";

	//	Fail messages
	public static final String READ_PROPERTIES_FILE_FAILED = "Failed to load version properties file.";
	public static final String SET_DEVICE_ID_FAILED = "Invalid deviceId property. Please provide a value from 01 to 98.";
	public static final String FETCH_DATA_FAILED = "Device monitoring cannot proceed, the required data could not be fetched from the %s command.";
	public static final String CONTROL_PROPERTY_FAILED = "Unable to perform this action at the moment. Please try again later.";
}
