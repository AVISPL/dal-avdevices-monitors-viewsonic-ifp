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
	BACKLIGHT_VALUE("BacklightCurrentValue(%)"),
	//	BLUE_LIGHT_FILTER("BlueLightFilter(%)"),
	//	BLUE_LIGHT_FILTER_VALUE("BlueLightFilterCurrentValue(%)"),
	BRIGHTNESS("Brightness(%)"),
	BRIGHTNESS_VALUE("BrightnessCurrentValue(%)"),
	COLOR("Color(%)"),
	COLOR_VALUE("ColorCurrentValue(%)"),
	//	COLOR_MODE("ColorMode"),
	CONTRAST("Contrast(%)"),
	CONTRAST_VALUE("ContrastCurrentValue(%)"),
	//	HUE("Hue(%)"),
	//	HUE_VALUE("HueCurrentValue(%)"),
	//	SHARPNESS("Sharpness(%)")
	//	SHARPNESS_VALUE("SharpnessCurrentValue(%)")
	;

	String name;

	public String getPropertyName() {
		return Constant.DISPLAY_GROUP + Constant.HASH + name;
	}
}
