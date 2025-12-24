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

	//	Special characters
	public static final String CR = "\r";

	//	Values
	public static final String NOT_AVAILABLE = "N/A";

	//	Groups
	public static final String ADAPTER_METADATA_GROUP = "AdapterMetadata";

	//	Fail messages
	public static final String READ_PROPERTIES_FILE_FAILED = "Failed to load version properties file.";
}
