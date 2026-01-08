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
	SET_BACKLIGHT_STATUS(Constant.SET_COMMAND_TYPE, "("),
	GET_BACKLIGHT("a", "B"),
	SET_BACKLIGHT("A", "B"),
	GET_BLUE_LIGHT_FILTER(Constant.GET_COMMAND_TYPE, ""),
	SET_BLUE_LIGHT_FILTER(Constant.SET_COMMAND_TYPE, ""),
	GET_BRIGHTNESS(Constant.GET_COMMAND_TYPE, "b"),
	SET_BRIGHTNESS(Constant.SET_COMMAND_TYPE, "$"),
	GET_COLOR(Constant.GET_COMMAND_TYPE, "d"),
	SET_COLOR(Constant.SET_COMMAND_TYPE, "&"),
	GET_COLOR_MODE(Constant.GET_COMMAND_TYPE, ""),
	SET_COLOR_MODE(Constant.SET_COMMAND_TYPE, ")"),
	GET_CONTRAST(Constant.GET_COMMAND_TYPE, "a"),
	SET_CONTRAST(Constant.SET_COMMAND_TYPE, "#"),
	GET_TINT(Constant.GET_COMMAND_TYPE, "e"),
	SET_TINT(Constant.SET_COMMAND_TYPE, "'"),
	GET_SHARPNESS(Constant.GET_COMMAND_TYPE, "c"),
	SET_SHARPNESS(Constant.SET_COMMAND_TYPE, "%");

	String type;
	String code;
}
