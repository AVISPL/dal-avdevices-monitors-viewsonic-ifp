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
public enum GeneralSettingCommand implements BaseCommand {
	GET_INPUT_SOURCE(Constant.GET_COMMAND_TYPE, "j"),
	GET_PIP_MODE(Constant.GET_COMMAND_TYPE, "t"),
	GET_TILING_MODE(Constant.GET_COMMAND_TYPE, "v"),
	GET_VOLUME(Constant.GET_COMMAND_TYPE, "f"),
	GET_MUTE(Constant.GET_COMMAND_TYPE, "g");

	String type;
	String code;
}
