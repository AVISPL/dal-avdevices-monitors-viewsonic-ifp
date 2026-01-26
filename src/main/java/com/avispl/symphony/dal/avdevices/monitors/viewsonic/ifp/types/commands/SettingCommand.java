/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseCommand;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;

/**
 * Represents the commands for general setting properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SettingCommand implements BaseCommand {
	GET_INPUT_SOURCE(Constant.GET_COMMAND_TYPE, "j", true),
	SET_INPUT_SOURCE(Constant.SET_COMMAND_TYPE, "\"", false),
	GET_PIP_MODE(Constant.GET_COMMAND_TYPE, "t", true),
	SET_PIP_MODE(Constant.SET_COMMAND_TYPE, "9", false),
	GET_TILING_MODE(Constant.GET_COMMAND_TYPE, "v", true),
	SET_TILING_MODE(Constant.SET_COMMAND_TYPE, "P", false),
	GET_VOLUME(Constant.GET_COMMAND_TYPE, "f", true),
	SET_VOLUME(Constant.SET_COMMAND_TYPE, "5", false),
	GET_MUTE(Constant.GET_COMMAND_TYPE, "g", true),
	SET_MUTE(Constant.SET_COMMAND_TYPE, "6", false);

	String type;
	String code;
	boolean isGetCommand;
}
