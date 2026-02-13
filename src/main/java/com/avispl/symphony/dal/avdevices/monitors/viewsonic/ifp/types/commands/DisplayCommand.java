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
	GET_BACKLIGHT_STATUS(Constant.GET_COMMAND_TYPE, "h", true),
	SET_BACKLIGHT_STATUS(Constant.SET_COMMAND_TYPE, "(", false),
	GET_BACKLIGHT("a", "B", true),
	SET_BACKLIGHT("A", "B", false),
	GET_BLUE_LIGHT_FILTER(Constant.GET_COMMAND_TYPE, "", true),
	SET_BLUE_LIGHT_FILTER(Constant.SET_COMMAND_TYPE, "", false),
	GET_BRIGHTNESS(Constant.GET_COMMAND_TYPE, "b", true),
	SET_BRIGHTNESS(Constant.SET_COMMAND_TYPE, "$", false),
	GET_COLOR(Constant.GET_COMMAND_TYPE, "d", true),
	SET_COLOR(Constant.SET_COMMAND_TYPE, "&", false),
	GET_COLOR_MODE(Constant.GET_COMMAND_TYPE, "", true),
	SET_COLOR_MODE(Constant.SET_COMMAND_TYPE, ")", false),
	GET_CONTRAST(Constant.GET_COMMAND_TYPE, "a", true),
	SET_CONTRAST(Constant.SET_COMMAND_TYPE, "#", false),
	GET_TINT(Constant.GET_COMMAND_TYPE, "e", true),
	SET_TINT(Constant.SET_COMMAND_TYPE, "'", false),
	GET_SHARPNESS(Constant.GET_COMMAND_TYPE, "c", true),
	SET_SHARPNESS(Constant.SET_COMMAND_TYPE, "%", false);

	String type;
	String code;
	boolean isGetCommand;
}
