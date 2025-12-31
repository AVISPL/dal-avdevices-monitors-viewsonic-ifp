/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseCommand;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;

/**
 * Represents the commands for display setting properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum DisplayCommand implements BaseCommand {
	GET_BACKLIGHT_STATUS(Constant.GET_COMMAND_TYPE, "h"),
	GET_BACKLIGHT("a", "B"),
	GET_BLUE_LIGHT_FILTER(Constant.GET_COMMAND_TYPE, ""),
	GET_BRIGHTNESS(Constant.GET_COMMAND_TYPE, "b"),
	GET_COLOR(Constant.GET_COMMAND_TYPE, "d"),
	GET_COLOR_MODE(Constant.GET_COMMAND_TYPE, ""),
	GET_CONTRAST(Constant.GET_COMMAND_TYPE, "a"),
	GET_TINT(Constant.GET_COMMAND_TYPE, "e"),
	GET_SHARPNESS(Constant.GET_COMMAND_TYPE, "c");

	String type;
	String code;
}
