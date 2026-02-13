/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseCommand;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;

/**
 * Represents the commands for general properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum GeneralCommand implements BaseCommand {
	GET_DEVICE_NAME(Constant.GET_COMMAND_TYPE, "4", true),
	GET_FIRMWARE_VERSION(Constant.GET_COMMAND_TYPE, "8", true),
	GET_IP_ADDRESS(Constant.GET_COMMAND_TYPE, "6", true),
	GET_MAC_ADDRESS(Constant.GET_COMMAND_TYPE, "5", true),
	GET_POWER_STATUS(Constant.GET_COMMAND_TYPE, "l", true),
	SET_POWER_STATUS(Constant.SET_COMMAND_TYPE, "!", false),
	GET_SERIAL_NUMBER(Constant.GET_COMMAND_TYPE, "7", true);

	String type;
	String code;
	boolean isGetCommand;
}
