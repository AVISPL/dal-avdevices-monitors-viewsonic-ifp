/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseProperty;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;

/**
 * Represents display setting properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Display implements BaseProperty {
	BACKLIGHT_STATUS("BacklightStatus"),
	BACKLIGHT("Backlight(%)"),
	//	BLUE_LIGHT_FILTER("BlueLightFilter(%)"),
	BRIGHTNESS("Brightness(%)"),
	COLOR("Color(%)"),
	//	COLOR_MODE("ColorMode"),
	CONTRAST("Contrast(%)"),
	//	HUE("Hue(%)"),
	//	SHARPNESS("Sharpness(%)")
	;

	String name;

	public String getPropertyName() {
		return Constant.DISPLAY_GROUP + Constant.HASH + name;
	}
}
