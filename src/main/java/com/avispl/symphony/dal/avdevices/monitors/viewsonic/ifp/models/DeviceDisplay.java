/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Represents detailed display setting about a device.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class DeviceDisplay {
	String backlightStatus;
	String backlight;
	String bluelightFilter;
	String brightness;
	String color;
	String colorMode;
	String contrast;
	String tint;
	String sharpness;
}
